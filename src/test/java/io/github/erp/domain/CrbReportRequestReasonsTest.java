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

class CrbReportRequestReasonsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbReportRequestReasons.class);
        CrbReportRequestReasons crbReportRequestReasons1 = new CrbReportRequestReasons();
        crbReportRequestReasons1.setId(1L);
        CrbReportRequestReasons crbReportRequestReasons2 = new CrbReportRequestReasons();
        crbReportRequestReasons2.setId(crbReportRequestReasons1.getId());
        assertThat(crbReportRequestReasons1).isEqualTo(crbReportRequestReasons2);
        crbReportRequestReasons2.setId(2L);
        assertThat(crbReportRequestReasons1).isNotEqualTo(crbReportRequestReasons2);
        crbReportRequestReasons1.setId(null);
        assertThat(crbReportRequestReasons1).isNotEqualTo(crbReportRequestReasons2);
    }
}
