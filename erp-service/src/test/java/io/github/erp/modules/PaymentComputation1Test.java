package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;
import static org.junit.jupiter.api.Assertions.*;

class PaymentComputation1Test {

    private PaymentComputation computation;

    static final MonetaryAmount INVOICE_AMOUNT =
        Money.of(BigDecimal.valueOf(Long.parseLong(String.valueOf(40000))),BASE_SYSTEM_CURRENCY_CODE);

    static final MonetaryAmount DISBURSEMENT_AMOUNT =
        Money.of(BigDecimal.valueOf(Long.parseLong(String.valueOf(27500))),BASE_SYSTEM_CURRENCY_CODE);

    static final MonetaryAmount VATABLE_AMOUNT =
        Money.of(BigDecimal.valueOf(Long.parseLong(String.valueOf(513.63))),BASE_SYSTEM_CURRENCY_CODE);

    PaymentRequisitionInt requisition = PaymentRequisitionInt.builder()
        .invoicedAmount(INVOICE_AMOUNT)
        .disbursementCost(DISBURSEMENT_AMOUNT)
        .vatableAmount(VATABLE_AMOUNT)
        .build();



    @BeforeEach
    void setUp() {

        // todo define tax rules
        TaxRuleInt taxRule = TaxRuleInt.builder().build();

        computation = new PaymentComputation1(taxRule);
    }

    @Test
    void calculate() {



        PaymentCalculationInt calculation = computation.calculate(requisition);

        // 
    }
}
