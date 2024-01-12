package com.starling.challenge.domain.services.challenge;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.FeedItem;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.domain.model.starling.FeedItem.Direction;
import com.starling.challenge.domain.services.starling.AccountsService;
import com.starling.challenge.domain.services.starling.SavingsGoalService;
import com.starling.challenge.domain.services.starling.TransactionFeedService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Roundup service implementation. Calls the appropriate starling APIs to perform roundup tasks.
 */
@Service
@Slf4j
@AllArgsConstructor
public class RoundupServiceImpl implements RoundupService {

    private AccountsService accountsService;
    private SavingsGoalService savingsGoalService;
    private TransactionFeedService transactionFeedService;

    /**
     * Main logic of application to perform roundup on accounts.
     */
    public Mono<ResponseEntity<?>> roundupForWeek(
        RoundupRequest roundupRequest
    ){
        log.info("Roundup request received.");

        // Get account details
        Mono<AccountV2> accountFoundMono = accountsService.getAccount(roundupRequest.getAccountname()).single();

        // Get or create savings goal
        Mono<UUID> savingsGoalUUIDMono = accountFoundMono.flatMap(accountFound -> {
            log.info("Get or create savings goal");
            return savingsGoalService.getOrCreateSavingsGoal(
                accountFound.getAccountUid(), 
                roundupRequest.getGoalName(), 
                roundupRequest.getOptionalSavingsGoalTarget(),
                accountFound.getCurrency()
            );
        });

        // Get settled transactions
        Mono<FeedItems> transactionFeedMono = accountFoundMono.flatMap(account -> {
            log.info("Get settled transactions");
            return transactionFeedService.getTransactionFeedForWeek(
                account.getAccountUid(), 
                roundupRequest.getWeekStarting()
            );
        });
        
        // Sum the roundup of transations
        Mono<BigInteger> roundupSumMono = Mono.zip(transactionFeedMono, accountFoundMono)
        .map(tuple -> sumFeedItems(tuple.getT1(), tuple.getT2()));

        // Check account if funds are present
        Mono<Tuple2<Boolean, Boolean>> confirmationOfFundsMono = Mono.zip(accountFoundMono, roundupSumMono)
        .flatMap(tuple -> accountsService.getConfirmationOfFunds(tuple.getT1().getAccountUid(), tuple.getT2())
            .map(confirmationOfFunds -> {
                log.info("Check account if funds are present");
                boolean amountAvailable = confirmationOfFunds.isRequestedAmountAvailableToSpend();
                boolean overdraftCaused = confirmationOfFunds.isAccountWouldBeInOverdraftIfRequestedAmountSpent();
                return Tuples.of(amountAvailable, overdraftCaused);
            }));

        // Transfer to savings goal
        Mono<RoundupResponse> roundupResponseMono = Mono.zip(
            accountFoundMono, 
            savingsGoalUUIDMono, 
            roundupSumMono, 
            confirmationOfFundsMono
        ).flatMap(tuple -> {
            return roundupTransfer(
                tuple.getT1(), 
                tuple.getT2(), 
                tuple.getT3(), 
                tuple.getT4().getT1(), 
                tuple.getT4().getT2());
        });
    
        return roundupResponseMono
        .map(response -> ResponseEntity.ok(response));
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
    public Mono<RoundupResponse> roundupTransfer(
        AccountV2 accountFound, 
        UUID savingsGoalUUID,
        BigInteger roundupSum, 
        boolean amountAvailable, 
        boolean overdraftCaused
        ) {
        CurrencyAndAmount currencyAndAmount = new CurrencyAndAmount(accountFound.getCurrency(), roundupSum);
        Mono<SavingsGoalTransferResponseV2> savingsGoalTransferResponseV2Mono;
        if(roundupSum.equals(BigInteger.ZERO) || !amountAvailable || overdraftCaused){
            SavingsGoalTransferResponseV2 savingsGoalTransferResponseV2 = new SavingsGoalTransferResponseV2();
            savingsGoalTransferResponseV2.setSuccess(false);
            savingsGoalTransferResponseV2.setTransferUid(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            savingsGoalTransferResponseV2Mono = Mono.just(savingsGoalTransferResponseV2);
            currencyAndAmount.setMinorUnits(BigInteger.ZERO);
            log.info("Transfer cancelled.");
        } else {
            TopUpRequestV2 topUpRequestV2 = new TopUpRequestV2(currencyAndAmount);
            savingsGoalTransferResponseV2Mono = savingsGoalService.transferToSavingsGoal(
                accountFound.getAccountUid(), 
                savingsGoalUUID, 
                topUpRequestV2
            );
        }
        return savingsGoalTransferResponseV2Mono.map(savingsGoalTransferResponseV2 -> {
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