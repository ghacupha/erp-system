package io.github.erp.modules;

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;

public interface PaymentComputation {

    PaymentCalculationInt calculate(PaymentRequisitionInt paymentRequisition);
}
