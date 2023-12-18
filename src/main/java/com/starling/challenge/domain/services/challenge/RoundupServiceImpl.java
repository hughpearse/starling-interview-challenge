package com.starling.challenge.domain.services.challenge;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.FeedItem;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.domain.services.starling.AccountsService;
import com.starling.challenge.domain.services.starling.SavingsGoalService;
import com.starling.challenge.domain.services.starling.TransactionFeedService;

import lombok.extern.slf4j.Slf4j;

/**
 * Roundup service implementation. Calls the appropriate starling APIs to perform roundup tasks.
 */
@Service
@Slf4j
public class RoundupServiceImpl implements RoundupServiceInt {

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
    public RoundupResponse roundupForWeek(
        RoundupRequest roundupRequest
    ){
        log.info("Roundup request received.");
        // Get account details
        AccountV2 accountFound = accountsService.getAccount(roundupRequest.getAccountname());

        // Get savings goal or create new savings goal if not exist yet
        UUID savingsGoalUUID = null;
        if(null != accountFound){
            savingsGoalUUID = savingsGoalService.getOrCreateSavingsGoal(accountFound.getAccountUid(), roundupRequest.getGoalName());
        }

        // Get settled transactions
        FeedItems transactionFeed = transactionFeedService.getTransactionFeedForWeek(accountFound.getAccountUid(), roundupRequest.getWeekStarting());
        if(transactionFeed.getFeedItems().size()>0) log.info("Found {} transactions.", transactionFeed.getFeedItems().size());
        
        // Sum the roundup of transations
        Long roundupSum = sumFeedItems(transactionFeed);

        // Add roundup sum to savings goal
        RoundupResponse roundupResponse = new RoundupResponse();
        if(roundupSum == 0l){
            SavingsGoalTransferResponseV2 savingsGoalTransferResponseV2 = new SavingsGoalTransferResponseV2();
            savingsGoalTransferResponseV2.setSuccess(false);
            UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
            savingsGoalTransferResponseV2.setTransferUid(emptyUUID);
            log.info("Transfer cancelled.");
            roundupResponse.setTransferToSavingsGoal(savingsGoalTransferResponseV2);
            roundupResponse.setCurrencyAndAmount(new CurrencyAndAmount("GBP", 0));
            return roundupResponse;
        }
        CurrencyAndAmount currencyAndAmount = new CurrencyAndAmount("GBP", roundupSum.intValue());
        TopUpRequestV2 topUpRequestV2 = new TopUpRequestV2(currencyAndAmount);
        SavingsGoalTransferResponseV2 transferToSavingsGoal = savingsGoalService.transferToSavingsGoal(accountFound.getAccountUid(), savingsGoalUUID, topUpRequestV2);
        roundupResponse.setTransferToSavingsGoal(transferToSavingsGoal);
        roundupResponse.setCurrencyAndAmount(currencyAndAmount);
        return roundupResponse;
    }

    /**
     * Sum the list of roundups in a list of feed items.
     * @param transactionFeed FeedItems object
     * @return Long of sum of roundups
     */
    private Long sumFeedItems(FeedItems transactionFeed){
        List<FeedItem> feedItems = transactionFeed.getFeedItems();
        Long roundupSum = 0l;
        for(FeedItem feedItem : feedItems){
            if(feedItem.getDirection().equals("OUT") && feedItem.getStatus().equals("SETTLED")){
                CurrencyAndAmount amount = feedItem.getAmount();
                if(amount.getCurrency().equals("GBP")){
                    roundupSum = roundupSum + roundup(amount.getMinorUnits());
                }
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
    private int roundup(int transaction) {
        int remainder = transaction % 100;
        if (remainder == 0) {
            return 0;
        } else {
            return 100 - remainder;
        }
    }
}
