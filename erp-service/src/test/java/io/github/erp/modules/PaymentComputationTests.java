package io.github.erp.modules;

import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

public class PaymentComputationTests {

    protected MonetaryAmount INVOICE_AMOUNT = Money.of(40000,BASE_SYSTEM_CURRENCY_CODE);

    protected MonetaryAmount DISBURSEMENT_AMOUNT = Money.of(7000,BASE_SYSTEM_CURRENCY_CODE);

    protected MonetaryAmount VATABLE_AMOUNT = Money.of(33000,BASE_SYSTEM_CURRENCY_CODE);

    protected PaymentRequisitionInt requisition = PaymentRequisitionInt.builder()
        .invoicedAmount(INVOICE_AMOUNT)
        .disbursementCost(DISBURSEMENT_AMOUNT)
        .vatableAmount(VATABLE_AMOUNT)
        .build();

    protected BigDecimal TELCO_EXCISE_DUTY = BigDecimal.valueOf(0.15);
    protected BigDecimal VALUE_ADDED_TAX = BigDecimal.valueOf(0.16);
    protected BigDecimal WITHHOLDING_VAT = BigDecimal.valueOf(0.02);
    protected BigDecimal WITHHOLDING_TAX_ON_CONSULTANCY = BigDecimal.valueOf(0.05);
    protected BigDecimal WITHHOLDING_TAX_ON_RENT = BigDecimal.valueOf(0.1);
    protected BigDecimal CATERING_LEVY = BigDecimal.valueOf(0.02);
    protected BigDecimal SERVICE_CHARGE = BigDecimal.valueOf(0.07);
    protected BigDecimal WITHHOLDING_TX_IMPORTED_SERVICE = BigDecimal.valueOf(0.2);

    protected TaxRuleInt taxRule = TaxRuleInt.builder()
        .telcoExciseDuty(TELCO_EXCISE_DUTY)
        .valueAddedTax(VALUE_ADDED_TAX)
        .withholdingVAT(WITHHOLDING_VAT)
        .withholdingTaxConsultancy(WITHHOLDING_TAX_ON_CONSULTANCY)
        .withholdingTaxRent(WITHHOLDING_TAX_ON_RENT)
        .cateringLevy(CATERING_LEVY)
        .serviceCharge(SERVICE_CHARGE)
        .withholdingTaxImportedService(WITHHOLDING_TX_IMPORTED_SERVICE)
        .build();
}
