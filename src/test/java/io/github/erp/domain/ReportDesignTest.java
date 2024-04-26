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

class ReportDesignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDesign.class);
        ReportDesign reportDesign1 = new ReportDesign();
        reportDesign1.setId(1L);
        ReportDesign reportDesign2 = new ReportDesign();
        reportDesign2.setId(reportDesign1.getId());
        assertThat(reportDesign1).isEqualTo(reportDesign2);
        reportDesign2.setId(2L);
        assertThat(reportDesign1).isNotEqualTo(reportDesign2);
        reportDesign1.setId(null);
        assertThat(reportDesign1).isNotEqualTo(reportDesign2);
    }
}
