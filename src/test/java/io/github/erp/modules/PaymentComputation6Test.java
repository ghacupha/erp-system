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
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentComputation6Test extends PaymentComputationTests{

    private PaymentComputation6 computation;

    private PaymentCalculationInt calculation;

    @BeforeEach
    void setUp() {
        computation = new PaymentComputation6(taxRule, "USD");
        calculation = computation.calculate(requisition$);
    }

    @Test
    void calculate() {

        testEquality(calculation.getPaymentExpense(), Money.of(40000, "USD"));
        testEquality(calculation.getWithholdingVAT(), Money.of(689.66, "USD"));
        testEquality(calculation.getWithholdingTax(), Money.of(0, "USD"));
        testEquality(calculation.getPaymentAmount(), Money.of(39310.34, "USD"));
    }
}
