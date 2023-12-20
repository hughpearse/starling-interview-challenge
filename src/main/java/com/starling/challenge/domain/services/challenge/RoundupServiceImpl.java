package com.starling.challenge.domain.services.challenge;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.ErrorResponse;
import com.starling.challenge.domain.model.starling.FeedItem;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.domain.model.starling.FeedItem.Direction;
import com.starling.challenge.domain.services.starling.AccountsService;
import com.starling.challenge.domain.services.starling.SavingsGoalService;
import com.starling.challenge.domain.services.starling.TransactionFeedService;
import com.starling.challenge.exceptions.StarlingRuntimeException;

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
    private ObjectMapper objectMapper = new ObjectMapper();

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
    public ResponseEntity<?> roundupForWeek(
        RoundupRequest roundupRequest
    ){
        log.info("Roundup request received.");
        ResponseEntity<?> responseEntity = null;
        try {

            // Get account details
            AccountV2 accountFound = accountsService.getAccount(roundupRequest.getAccountname());

            // Get savings goal or create new savings goal if it does not exist yet
            UUID savingsGoalUUID = null;
            if(null != accountFound){
                savingsGoalUUID = savingsGoalService.getOrCreateSavingsGoal(accountFound, roundupRequest.getGoalName());
            }

            // Get settled transactions
            FeedItems transactionFeed = transactionFeedService.getTransactionFeedForWeek(accountFound, roundupRequest.getWeekStarting());
            if(transactionFeed.getFeedItems().size()>0) log.info("Found {} transactions.", transactionFeed.getFeedItems().size());
            
            // Sum the roundup of transations
            BigInteger roundupSum = sumFeedItems(transactionFeed, accountFound);

            // Add roundup sum to savings goal
            RoundupResponse roundupResponse = new RoundupResponse();
            if(roundupSum.equals(BigInteger.ZERO)){
                SavingsGoalTransferResponseV2 savingsGoalTransferResponseV2 = new SavingsGoalTransferResponseV2();
                savingsGoalTransferResponseV2.setSuccess(false);
                UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
                savingsGoalTransferResponseV2.setTransferUid(emptyUUID);
                log.info("Transfer cancelled.");
                roundupResponse.setTransferToSavingsGoal(savingsGoalTransferResponseV2);
                roundupResponse.setCurrencyAndAmount(new CurrencyAndAmount(accountFound.getCurrency(), BigInteger.ZERO));
            } else {
                CurrencyAndAmount currencyAndAmount = new CurrencyAndAmount(accountFound.getCurrency(), roundupSum);
                TopUpRequestV2 topUpRequestV2 = new TopUpRequestV2(currencyAndAmount);
                SavingsGoalTransferResponseV2 transferToSavingsGoal = savingsGoalService.transferToSavingsGoal(accountFound, savingsGoalUUID, topUpRequestV2);
                roundupResponse.setTransferToSavingsGoal(transferToSavingsGoal);
                roundupResponse.setCurrencyAndAmount(currencyAndAmount);
            }
            responseEntity = new ResponseEntity<>(roundupResponse, HttpStatus.OK);
        } catch (StarlingRuntimeException ex) {
            ErrorResponse errorMsg = null;
            try {
                errorMsg = objectMapper.readValue(ex.getResponse().getBody(), ErrorResponse.class);
                responseEntity = new ResponseEntity<>(errorMsg, ex.getResponse().getStatusCode());
            } catch (Exception e) {
                log.error("Error parsing client response: " + e.getMessage());
            }
            log.error("Client response error message: " + errorMsg.toString());
        }
        return responseEntity;
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
                if(feedItem.getAmount().getCurrency().equals(account.getCurrency()));
                amount = feedItem.getAmount();
                if(feedItem.getSourceAmount().getCurrency().equals(account.getCurrency()));
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