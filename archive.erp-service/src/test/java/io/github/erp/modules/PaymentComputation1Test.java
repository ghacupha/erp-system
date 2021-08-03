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

import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

class PaymentComputation1Test extends PaymentComputationTests  {

    private PaymentComputation1 computation;

    private PaymentCalculationInt calculation;

    @BeforeEach
    void setUp() {
        computation = new PaymentComputation1(taxRule);
        calculation = computation.calculate(requisition);
    }

    @Test
    void calculate() {
        testEquality(calculation.getPaymentExpense(), Money.of(40000, BASE_SYSTEM_CURRENCY_CODE));
        testEquality(calculation.getWithholdingVAT(), Money.of(690, BASE_SYSTEM_CURRENCY_CODE));
        testEquality(calculation.getWithholdingTax(), Money.of(0, BASE_SYSTEM_CURRENCY_CODE));
        testEquality(calculation.getPaymentAmount(), Money.of(39310, BASE_SYSTEM_CURRENCY_CODE));
    }
}
