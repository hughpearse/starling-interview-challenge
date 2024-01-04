package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * An item from the account holders's transaction feed
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedItem {
    public enum Direction {
        IN, OUT
    }
    public enum Source {
        BRITISH_BUSINESS_BANK_FEES, CARD_FEE_CHARGE, CASH_DEPOSIT, CASH_DEPOSIT_CHARGE, CASH_WITHDRAWAL, CASH_WITHDRAWAL_CHARGE, CHAPS, CHEQUE, CICS_CHEQUE, CURRENCY_CLOUD, DIRECT_CREDIT, DIRECT_DEBIT, DIRECT_DEBIT_DISPUTE, INTERNAL_TRANSFER, MASTER_CARD, MASTERCARD_MONEYSEND, MASTERCARD_CHARGEBACK, MISSED_PAYMENT_FEE, FASTER_PAYMENTS_IN, FASTER_PAYMENTS_OUT, FASTER_PAYMENTS_REVERSAL, STRIPE_FUNDING, INTEREST_PAYMENT, NOSTRO_DEPOSIT, OVERDRAFT, OVERDRAFT_INTEREST_WAIVED, FASTER_PAYMENTS_REFUND, STARLING_PAY_STRIPE, ON_US_PAY_ME, LOAN_PRINCIPAL_PAYMENT, LOAN_REPAYMENT, LOAN_OVERPAYMENT, LOAN_LATE_PAYMENT, LOAN_FEE_PAYMENT, LOAN_INTEREST_CHARGE, SEPA_CREDIT_TRANSFER, SEPA_DIRECT_DEBIT, TARGET2_CUSTOMER_PAYMENT, FX_TRANSFER, ISS_PAYMENT, STARLING_PAYMENT, SUBSCRIPTION_CHARGE, OVERDRAFT_FEE, WITHHELD_TAX, ERRORS_AND_OMISSIONS, INTEREST_V2_PAYMENT
    }
    public enum SourceSubType {
        CONTACTLESS, MAGNETIC_STRIP, MANUAL_KEY_ENTRY, CHIP_AND_PIN, ONLINE, ATM, CREDIT_AUTH, APPLE_PAY, APPLE_PAY_ONLINE, ANDROID_PAY, ANDROID_PAY_ONLINE, FITBIT_PAY, GARMIN_PAY, SAMSUNG_PAY, OTHER_WALLET, CARD_SUBSCRIPTION, NOT_APPLICABLE, UNKNOWN, DEPOSIT, OVERDRAFT, SETTLE_UP, NEARBY, TRANSFER_SAME_CURRENCY, NEW_CARD, NEW_CARD_OVERSEAS
    }
    public enum Status {
        UPCOMING, PENDING, REVERSED, SETTLED, DECLINED, REFUNDED, RETRYING, ACCOUNT_CHECK
    }
    public enum CounterPartyType {
        CATEGORY, CHEQUE, CUSTOMER, PAYEE, MERCHANT, SENDER, STARLING, LOAN
    }
    public enum Country {
        UNDEFINED, AC, AD, AE, AF, AG, AI, AL, AM, AN, AO, AQ, AR, AS, AT, AU, AW, AX, AZ, BA, BB, BD, BE, BF, BG, BH, BI, BJ, BL, BM, BN, BO, BQ, BR, BS, BT, BU, BV, BW, BY, BZ, CA, CC, CD, CF, CG, CH, CI, CK, CL, CM, CN, CO, CP, CR, CS, CU, CV, CW, CX, CY, CZ, DE, DG, DJ, DK, DM, DO, DZ, EA, EC, EE, EG, EH, ER, ES, ET, EU, EZ, FI, FJ, FK, FM, FO, FR, FX, GA, GB, GD, GE, GF, GG, GH, GI, GL, GM, GN, GP, GQ, GR, GS, GT, GU, GW, GY, HK, HM, HN, HR, HT, HU, IC, ID, IE, IL, IM, IN, IO, IQ, IR, IS, IT, JE, JM, JO, JP, KE, KG, KH, KI, KM, KN, KP, KR, KW, KY, KZ, LA, LB, LC, LI, LK, LR, LS, LT, LU, LV, LY, MA, MC, MD, ME, MF, MG, MH, MK, ML, MM, MN, MO, MP, MQ, MR, MS, MT, MU, MV, MW, MX, MY, MZ, NA, NC, NE, NF, NG, NI, NL, NO, NP, NR, NT, NU, NZ, OM, PA, PE, PF, PG, PH, PK, PL, PM, PN, PR, PS, PT, PW, PY, QA, RE, RO, RS, RU, RW, SA, SB, SC, SD, SE, SF, SG, SH, SI, SJ, SK, SL, SM, SN, SO, SR, SS, ST, SU, SV, SX, SY, SZ, TA, TC, TD, TF, TG, TH, TJ, TK, TL, TM, TN, TO, TP, TR, TT, TV, TW, TZ, UA, UG, UK, UM, US, UY, UZ, VA, VC, VE, VG, VI, VN, VU, WF, WS, XI, XU, XK, XX, YE, YT, YU, ZA, ZM, ZR, ZW
    }
    public enum SpendingCategory {
        BIKE, BILLS_AND_SERVICES, BUCKET_LIST, CAR, CASH, CELEBRATION, CHARITY, CHILDREN, CLOTHES, COFFEE, DEBT_REPAYMENT, DIY, DRINKS, EATING_OUT, EDUCATION, EMERGENCY, ENTERTAINMENT, ESSENTIAL_SPEND, EXPENSES, FAMILY, FITNESS, FUEL, GAMBLING, GAMING, GARDEN, GENERAL, GIFTS, GROCERIES, HOBBY, HOLIDAYS, HOME, IMPULSE_BUY, INCOME, INSURANCE, INVESTMENTS, LIFESTYLE, MAINTENANCE_AND_REPAIRS, MEDICAL, MORTGAGE, NON_ESSENTIAL_SPEND, PAYMENTS, PERSONAL_CARE, PERSONAL_TRANSFERS, PETS, PROJECTS, RELATIONSHIPS, RENT, SAVING, SHOPPING, SUBSCRIPTIONS, TAKEAWAY, TAXI, TRANSPORT, TREATS, WEDDING, WELLBEING, NONE, REVENUE, OTHER_INCOME, CLIENT_REFUNDS, INVENTORY, STAFF, TRAVEL, WORKPLACE, REPAIRS_AND_MAINTENANCE, ADMIN, MARKETING, BUSINESS_ENTERTAINMENT, INTEREST_PAYMENTS, BANK_CHARGES, OTHER, FOOD_AND_DRINK, EQUIPMENT, PROFESSIONAL_SERVICES, PHONE_AND_INTERNET, VEHICLES, DIRECTORS_WAGES, VAT, CORPORATION_TAX, SELF_ASSESSMENT_TAX, INVESTMENT_CAPITAL, TRANSFERS, LOAN_PRINCIPAL, PERSONAL, DIVIDENDS
    }
    private UUID feedItemUid;
    private UUID categoryUid;
    private CurrencyAndAmount amount;
    private CurrencyAndAmount sourceAmount;
    private Direction direction;
    private Date updatedAt;
    private Date transactionTime;
    private Date settlementTime;
    private Date retryAllocationUntilTime;
    private Source source;
    private SourceSubType sourceSubType;
    private Status status;
    private UUID transactingApplicationUserUid;
    private CounterPartyType counterPartyType;
    private UUID counterPartyUid;
    private String counterPartyName;
    private UUID counterPartySubEntityUid;
    private String counterPartySubEntityName;
    private String counterPartySubEntityIdentifier;
    private String counterPartySubEntitySubIdentifier;
    private double exchangeRate;
    private int totalFees;
    private CurrencyAndAmount totalFeeAmount;
    private String reference;
    private Country country;
    private SpendingCategory spendingCategory;
    private String userNote;
    private AssociatedFeedRoundUp roundUp;
    private boolean hasAttachment;
    private boolean hasReceipt;
    private BatchPaymentDetails batchPaymentDetails;
}
