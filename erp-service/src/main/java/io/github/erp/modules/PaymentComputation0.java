package io.github.erp.modules;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.erp.modules.models.PaymentCalculationInt;
import io.github.erp.modules.models.PaymentRequisitionInt;
import org.javamoney.moneta.Money;

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

public class PaymentComputation0 implements PaymentComputation {

    private final String currencyCode;

    public PaymentComputation0() {
        this(BASE_SYSTEM_CURRENCY_CODE);
    }

    public PaymentComputation0(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public PaymentCalculationInt calculate(PaymentRequisitionInt paymentRequisitionInt) {

        return PaymentCalculationInt.builder()
            .paymentAmount(paymentRequisitionInt.getInvoicedAmount())
            .paymentExpense(paymentRequisitionInt.getInvoicedAmount())
            .withholdingTax(Money.zero(PaymentComputationUtils.currencyUnit(currencyCode)))
            .withholdingVAT(Money.zero(PaymentComputationUtils.currencyUnit(currencyCode)))
            .build();
    }
}
