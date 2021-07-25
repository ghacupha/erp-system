package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import io.github.erp.modules.models.TaxRuleInt;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.github.erp.modules.PaymentComputationUtils.ZERO;

public interface PaymentComputation {

    PaymentCalculationInt calculate(PaymentRequisitionInt paymentRequisition);
}
