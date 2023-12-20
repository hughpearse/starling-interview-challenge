package com.starling.challenge.domain.services.starling;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Currency;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Savings Goal Domain Service
 */
@Service
@Slf4j
public class SavingsGoalService implements SavingsGoalServiceInt {

    private SavingsGoalsClient savingsGoalsClient;

    /**
     * Constructor for Savings Goal domain service.
     * @param savingsGoalsClient injected HTTP client for interacting with Savings goals.
     */
    public SavingsGoalService(
        SavingsGoalsClient savingsGoalsClient
    ){
        this.savingsGoalsClient = savingsGoalsClient;
    }

    public SavingsGoalV2 getSavingsGoal(
        UUID accountUid,
        String goalName
        ) {
        SavingsGoalV2 savingsGoalFound = null;
        SavingsGoalsV2 savingsGoals = savingsGoalsClient.getSavingsGoals(accountUid);
        for(SavingsGoalV2 savingsGoal : savingsGoals.getSavingsGoalList()){
            if(savingsGoal.getName().equals(goalName)){
                savingsGoalFound = savingsGoal;
                log.info("Savings goal found.");
                break;
            }
        }
        return savingsGoalFound;
    }

    public CreateOrUpdateSavingsGoalResponseV2 createSavingsGoal(
        UUID accountUid,
        SavingsGoalRequestV2 savingsGoalRequestV2
        ) {
        CreateOrUpdateSavingsGoalResponseV2 newSavingsGoal = null;
        CurrencyAndAmount request_CurrencyAndAmount = new CurrencyAndAmount(
            savingsGoalRequestV2.getCurrency(),
            savingsGoalRequestV2.getTarget().getMinorUnits()
            );
            SavingsGoalRequestV2 request = new SavingsGoalRequestV2(
                savingsGoalRequestV2.getName(), 
                savingsGoalRequestV2.getCurrency(),
                request_CurrencyAndAmount, 
                ""
                );
            newSavingsGoal = savingsGoalsClient.createSavingsGoal(
                accountUid,
                request);
            log.info("Savings goal created.");
            return newSavingsGoal;
    }
    
    public SavingsGoalTransferResponseV2 transferToSavingsGoal(
        UUID accountUid,
        UUID savingsGoalUUID, 
        TopUpRequestV2 topUpRequestV2
        ){
            SavingsGoalTransferResponseV2 transferToSavingsGoal = savingsGoalsClient.transferToSavingsGoal(
            accountUid, 
            savingsGoalUUID, 
            UUID.randomUUID(), 
            topUpRequestV2);
            log.info("Transfer completed.");
            return transferToSavingsGoal;
    }

    public UUID getOrCreateSavingsGoal(
        UUID accountUid,
        String goalName,
        BigInteger optionalSavingsGoalTarget,
        Currency currency
    ){
        SavingsGoalV2 savingsGoal = getSavingsGoal(accountUid, goalName);
        CreateOrUpdateSavingsGoalResponseV2 newSavingsGoal = null;
        if(null == savingsGoal){
            CurrencyAndAmount request_CurrencyAndAmount = new CurrencyAndAmount(
                currency, 
                optionalSavingsGoalTarget
            );
            SavingsGoalRequestV2 savingsGoalRequestV2 = new SavingsGoalRequestV2(
                goalName, 
                currency, 
                request_CurrencyAndAmount, 
                ""
            );
            newSavingsGoal = createSavingsGoal(accountUid, savingsGoalRequestV2);
        }
        UUID savingsGoalUUID = null != savingsGoal ? savingsGoal.getSavingsGoalUid() : newSavingsGoal.getSavingsGoalUid();
        return savingsGoalUUID;
    }
}
