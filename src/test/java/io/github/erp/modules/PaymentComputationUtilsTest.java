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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.github.erp.modules.PaymentComputationUtils.onePlusVAT;
import static io.github.erp.modules.PaymentComputationUtils.queryNumerical;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentComputationUtilsTest extends PaymentComputationTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void onePlusVATTest() {

        assertThat(onePlusVAT(taxRule)).isEqualTo(BigDecimal.valueOf(1.16));
    }

    @Test
    void queryNumericalTest() {

        assertThat(queryNumerical(requisition.getInvoicedAmount())).isEqualTo(BigDecimal.valueOf(40000).setScale(2, RoundingMode.HALF_EVEN));
    }
}
