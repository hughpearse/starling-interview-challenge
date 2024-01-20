package com.starling.challenge.domain.services.challenge;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.FeedItem;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.domain.model.starling.FeedItem.Direction;
import com.starling.challenge.domain.services.starling.AccountsService;
import com.starling.challenge.domain.services.starling.SavingsGoalService;
import com.starling.challenge.domain.services.starling.TransactionFeedService;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * Roundup service implementation. Calls the appropriate starling APIs to perform roundup tasks.
 */
@ApplicationScoped
@Slf4j
public class RoundupServiceImpl implements RoundupService {

    private AccountsService accountsService;
    private SavingsGoalService savingsGoalService;
    private TransactionFeedService transactionFeedService;

    /**
     * Constructor for roundup application.
     * @param accountsService injected service for interacting with accounts.
     * @param savingsGoalsClient injected service for interacting with savings goals.
     * @param transactionFeedClient injected service for interacting with transaction feed.
     */
    public RoundupServiceImpl(
        AccountsService accountsService,
        SavingsGoalService savingsGoalService,
        TransactionFeedService transactionFeedService
    ){
        this.accountsService = accountsService;
        this.savingsGoalService = savingsGoalService;
        this.transactionFeedService = transactionFeedService;
    }

    /**
     * Main logic of application to perform roundup on accounts.
     */
    public Uni<RoundupResponse> roundupForWeek(
        RoundupRequest roundupRequest
    ){
        log.info("Roundup request received.");

        // Get account details
        Uni<AccountV2> accountFoundUni = accountsService.getAccount(roundupRequest.getAccountname()).memoize().indefinitely();

        // Get or create savings goal
        Uni<UUID> savingsGoalUUIDUni = accountFoundUni.onItem().transformToUni(accountFound -> {
            log.info("Get or create savings goal");
            return savingsGoalService.getOrCreateSavingsGoal(
                accountFound.getAccountUid(), 
                roundupRequest.getGoalName(), 
                roundupRequest.getOptionalSavingsGoalTarget(),
                accountFound.getCurrency()
            );
        });

        // Get settled transactions
        Uni<FeedItems> transactionFeedUni = accountFoundUni.onItem().transformToUni(account -> {
            log.info("Get settled transactions");
            return transactionFeedService.getTransactionFeedForWeek(
                account.getAccountUid(), 
                roundupRequest.getWeekStarting()
            );
        });

        // Sum the roundup of transactions
        Uni<Tuple2<FeedItems, AccountV2>> waitRoundupSum = Uni.combine().all()
            .unis(transactionFeedUni, accountFoundUni)
            .asTuple();
        Uni<BigInteger> roundupSumUni = waitRoundupSum
            .map(tuple -> {
                log.info("Sum roundup of transactions");
                return sumFeedItems(tuple.getItem1(), tuple.getItem2());
            })
            .memoize().indefinitely();

        // Check account if funds are present
        Uni<Tuple2<AccountV2, BigInteger>> waitConfirmationOfFundsResponse = Uni.combine().all()
            .unis(accountFoundUni, roundupSumUni)
            .asTuple();
        Uni<ConfirmationOfFundsResponse> confirmationOfFundsResponseUni = waitConfirmationOfFundsResponse
            .flatMap(tuple -> 
                accountsService.getConfirmationOfFunds(tuple.getItem1().getAccountUid(), tuple.getItem2())
        );
        Uni<Tuple2<Boolean, Boolean>> confirmationOfFundsResponseUniTuple = confirmationOfFundsResponseUni.map(confirmationOfFunds -> {
            log.info("Check account if funds are present");
            Boolean amountAvailable = confirmationOfFunds.isRequestedAmountAvailableToSpend();
            Boolean overdraftCaused = confirmationOfFunds.isAccountWouldBeInOverdraftIfRequestedAmountSpent();
            return Tuple2.of(amountAvailable, overdraftCaused);
        });

        // Transfer to savings goal
        Uni<Tuple4<AccountV2, UUID, BigInteger, Tuple2<Boolean, Boolean>>> waitRoundupResponse = Uni.combine().all()
        .unis(
            accountFoundUni, 
            savingsGoalUUIDUni, 
            roundupSumUni, 
            confirmationOfFundsResponseUniTuple
        ).asTuple();
        Uni<RoundupResponse> roundupResponseUni = waitRoundupResponse.flatMap(tuple -> {
            log.info("Transfer to savings goal");
            return roundupTransfer(
                tuple.getItem1(), 
                tuple.getItem2(), 
                tuple.getItem3(), 
                tuple.getItem4().getItem1(), 
                tuple.getItem4().getItem2());
        });
    
        return roundupResponseUni;
    }

    /**
     * Transfer money to savings goal
     * @param accountFound the AccountV2 object
     * @param savingsGoalUUID the UUID object
     * @param roundupSum the BigInteger object
     * @param amountAvailable the boolean primitive
     * @param overdraftCaused the boolean primitive
     * @return
     */
    public Uni<RoundupResponse> roundupTransfer(
        AccountV2 accountFound, 
        UUID savingsGoalUUID,
        BigInteger roundupSum, 
        boolean amountAvailable, 
        boolean overdraftCaused
        ) {
        CurrencyAndAmount currencyAndAmount = new CurrencyAndAmount(accountFound.getCurrency(), roundupSum);
        Uni<SavingsGoalTransferResponseV2> savingsGoalTransferResponseV2Uni;
        if(roundupSum.equals(BigInteger.ZERO) || !amountAvailable || overdraftCaused){
            SavingsGoalTransferResponseV2 savingsGoalTransferResponseV2 = new SavingsGoalTransferResponseV2();
            savingsGoalTransferResponseV2.setSuccess(false);
            savingsGoalTransferResponseV2.setTransferUid(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            savingsGoalTransferResponseV2Uni = Uni.createFrom().item(savingsGoalTransferResponseV2);
            currencyAndAmount.setMinorUnits(BigInteger.ZERO);
            log.info("Transfer cancelled.");
        } else {
            TopUpRequestV2 topUpRequestV2 = new TopUpRequestV2(currencyAndAmount);
            savingsGoalTransferResponseV2Uni = savingsGoalService.transferToSavingsGoal(
                accountFound.getAccountUid(), 
                savingsGoalUUID, 
                topUpRequestV2
            );
        }
        return savingsGoalTransferResponseV2Uni.map(savingsGoalTransferResponseV2 -> {
            RoundupResponse roundupResponse = new RoundupResponse();
            roundupResponse.setTransferToSavingsGoal(savingsGoalTransferResponseV2);
            roundupResponse.setCurrencyAndAmount(currencyAndAmount);
            return roundupResponse;
        });
    }

    /**
     * Sum the list of roundups in a list of feed items.
     * @param transactionFeed FeedItems object as a FeedItems object
     * @param account the account detils as a AccountV2 object
     * @return sum of roundups as a BigInteger
     */
    private BigInteger sumFeedItems(FeedItems transactionFeed, AccountV2 account){
        List<FeedItem> feedItems = transactionFeed.getFeedItems();
        BigInteger roundupSum = BigInteger.ZERO;
        for(FeedItem feedItem : feedItems){
            if(feedItem.getDirection().equals(Direction.OUT) ){
                // Find correct amount based on currency in account settings
                CurrencyAndAmount amount = null;
                if(feedItem.getAmount().getCurrency().equals(account.getCurrency()))
                amount = feedItem.getAmount();
                if(feedItem.getSourceAmount().getCurrency().equals(account.getCurrency()))
                amount = feedItem.getSourceAmount();

                roundupSum = roundupSum.add(roundup(amount.getMinorUnits()));
            }
        }
        log.info("Sum of roundup is {} minor units.", roundupSum);
        return roundupSum;
    }

    /**
     * Roundup logic using modulo.
     * @param transaction input the minor units.
     * @return the rounded up value.
     */
    private BigInteger roundup(BigInteger transaction) {
        BigInteger hundred = new BigInteger("100");
        BigInteger remainder = transaction.mod(hundred);
        if (remainder.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        } else {
            return hundred.subtract(remainder);
        }
    }
}