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

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.erp.modules.PaymentReferenceCodes.BASE_SYSTEM_CURRENCY_CODE;

class BaseComputationTest extends PaymentComputationTests {

    private BaseComputation bs;

    @BeforeEach
    void setUp() {
        bs = new BaseComputation();
    }

    @Test
    void calculateWithholdingVATAmount() {

        assertThat(bs.calculateWithholdingVATAmount(taxRule, Money.of(34482.76,BASE_SYSTEM_CURRENCY_CODE)).getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.HALF_EVEN))
            .isEqualTo(BigDecimal.valueOf(689.66));

    }
}
