package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonthlyPrepaymentOutstandingReportItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonthlyPrepaymentOutstandingReportItem.class);
        MonthlyPrepaymentOutstandingReportItem monthlyPrepaymentOutstandingReportItem1 = new MonthlyPrepaymentOutstandingReportItem();
        monthlyPrepaymentOutstandingReportItem1.setId(1L);
        MonthlyPrepaymentOutstandingReportItem monthlyPrepaymentOutstandingReportItem2 = new MonthlyPrepaymentOutstandingReportItem();
        monthlyPrepaymentOutstandingReportItem2.setId(monthlyPrepaymentOutstandingReportItem1.getId());
        assertThat(monthlyPrepaymentOutstandingReportItem1).isEqualTo(monthlyPrepaymentOutstandingReportItem2);
        monthlyPrepaymentOutstandingReportItem2.setId(2L);
        assertThat(monthlyPrepaymentOutstandingReportItem1).isNotEqualTo(monthlyPrepaymentOutstandingReportItem2);
        monthlyPrepaymentOutstandingReportItem1.setId(null);
        assertThat(monthlyPrepaymentOutstandingReportItem1).isNotEqualTo(monthlyPrepaymentOutstandingReportItem2);
    }
}
