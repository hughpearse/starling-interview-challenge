package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.BatchPaymentDetails;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.FeedItem;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.domain.model.starling.FeedItem.Direction;

@ExtendWith(MockitoExtension.class)
public class TransactionFeedServiceTest {
    private TransactionFeedService transactionService = mock(TransactionFeedService.class);

    @Test
    public void testGetTransactionFeedForWeek() {
        // Arrange: configure mock responses
        AccountV2 account = new AccountV2();
        account.setAccountUid(UUID.randomUUID());
        account.setAccountType("Savings");
        account.setDefaultCategory("Personal");
        account.setCurrency(Currency.getInstance("GBP"));
        account.setCreatedAt("2023-12-19T12:30:28Z");
        account.setName("Test Account");
        Date weekStarting = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(weekStarting);
        calendar.add(Calendar.DATE, 7);

        FeedItem testFeedItem = new FeedItem();
        testFeedItem.setFeedItemUid(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        testFeedItem.setCategoryUid(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        testFeedItem.setDirection(Direction.OUT);
        testFeedItem.setUpdatedAt("2023-01-01T00:00:00Z");
        testFeedItem.setTransactionTime("2023-01-01T00:00:00Z");
        testFeedItem.setSettlementTime("2023-01-01T00:00:00Z");
        testFeedItem.setSource("CARD_PAYMENT");
        testFeedItem.setSourceSubType("CONTACTLESS");
        testFeedItem.setStatus("SETTLED");
        testFeedItem.setTransactingApplicationUserUid(UUID.fromString("00000000-0000-0000-0000-000000000003"));
        testFeedItem.setCounterPartyType("MERCHANT");
        testFeedItem.setCounterPartyUid(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        testFeedItem.setCounterPartyName("Test Merchant");
        testFeedItem.setCounterPartySubEntityUid(UUID.fromString("00000000-0000-0000-0000-000000000005"));
        testFeedItem.setCounterPartySubEntityName("Test SubEntity");
        testFeedItem.setCounterPartySubEntityIdentifier("Test Identifier");
        testFeedItem.setCounterPartySubEntitySubIdentifier("Test SubIdentifier");
        testFeedItem.setExchangeRate(1.0);
        testFeedItem.setTotalFees(0);
        testFeedItem.setReference("Test Reference");
        testFeedItem.setCountry("GB");
        testFeedItem.setSpendingCategory("GROCERIES");
        testFeedItem.setUserNote("Test Note");

        CurrencyAndAmount amount = new CurrencyAndAmount();
        amount.setCurrency(Currency.getInstance("GBP"));
        amount.setMinorUnits(BigInteger.valueOf(1000));
        testFeedItem.setAmount(amount);

        CurrencyAndAmount sourceAmount = new CurrencyAndAmount();
        sourceAmount.setCurrency(Currency.getInstance("GBP"));
        sourceAmount.setMinorUnits(BigInteger.valueOf(1000));
        testFeedItem.setSourceAmount(sourceAmount);

        BatchPaymentDetails batchPaymentDetails = new BatchPaymentDetails();
        batchPaymentDetails.setBatchPaymentUid(UUID.fromString("00000000-0000-0000-0000-000000000006"));
        batchPaymentDetails.setBatchPaymentType("PAYMENT");
        testFeedItem.setBatchPaymentDetails(batchPaymentDetails);

        testFeedItem.setHasAttachment(false);
        testFeedItem.setHasReceipt(false);

        FeedItems testFeedItems = new FeedItems(Arrays.asList(testFeedItem));
        when(transactionService.getTransactionFeedForWeek(account.getAccountUid(), weekStarting)).thenReturn(testFeedItems);

        // Act: perform the test
        FeedItems result = transactionService.getTransactionFeedForWeek(account.getAccountUid(), weekStarting);

        // Assert: check results
        assertNotNull(result, "FeedItems should not be null");
        assertEquals(testFeedItems, result, "FeedItems should match");
    }

}
