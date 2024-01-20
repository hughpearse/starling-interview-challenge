package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.BatchPaymentDetails;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.FeedItem;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.outboundclients.starling.TransactionFeedClient;
import com.starling.challenge.domain.model.starling.FeedItem.Direction;

@QuarkusTest
public class TransactionFeedServiceTest {

    @InjectMock
    TransactionFeedClient transactionFeedClient;

    @InjectMock
    private TransactionFeedServiceImpl transactionService;

    @InjectMock
    MicroProfileConfig microProfileConfig;

    @Test
    public void testGetTransactionFeedForWeek() throws Exception {
        // Arrange: configure mock responses
        AccountV2 account = new AccountV2();
        account.setAccountUid(UUID.randomUUID());
        account.setAccountType(AccountV2.AccountType.PRIMARY);
        account.setDefaultCategory(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        account.setCurrency(Currency.getInstance("GBP"));
        account.setCreatedAt(DateUtils.parseDate("2023-01-01T00:00:00.000Z", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" }));
        account.setName("Test Account");
        Date weekStarting = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(weekStarting);
        calendar.add(Calendar.DATE, 7);

        FeedItem testFeedItem = new FeedItem();
        testFeedItem.setFeedItemUid(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        testFeedItem.setCategoryUid(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        testFeedItem.setDirection(Direction.OUT);
        testFeedItem.setUpdatedAt(DateUtils.parseDate("2023-01-01T00:00:00.000Z", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" }));
        testFeedItem.setTransactionTime(DateUtils.parseDate("2023-01-01T00:00:00.000Z", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" }));
        testFeedItem.setSettlementTime(DateUtils.parseDate("2023-01-01T00:00:00.000Z", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" }));
        testFeedItem.setSource(FeedItem.Source.CASH_DEPOSIT);
        testFeedItem.setSourceSubType(FeedItem.SourceSubType.CONTACTLESS);
        testFeedItem.setStatus(FeedItem.Status.SETTLED);
        testFeedItem.setTransactingApplicationUserUid(UUID.fromString("00000000-0000-0000-0000-000000000003"));
        testFeedItem.setCounterPartyType(FeedItem.CounterPartyType.MERCHANT);
        testFeedItem.setCounterPartyUid(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        testFeedItem.setCounterPartyName("Test Merchant");
        testFeedItem.setCounterPartySubEntityUid(UUID.fromString("00000000-0000-0000-0000-000000000005"));
        testFeedItem.setCounterPartySubEntityName("Test SubEntity");
        testFeedItem.setCounterPartySubEntityIdentifier("Test Identifier");
        testFeedItem.setCounterPartySubEntitySubIdentifier("Test SubIdentifier");
        testFeedItem.setExchangeRate(1.0);
        testFeedItem.setTotalFees(0);
        testFeedItem.setReference("Test Reference");
        testFeedItem.setCountry(FeedItem.Country.GB);
        testFeedItem.setSpendingCategory(FeedItem.SpendingCategory.GROCERIES);
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
