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

class CrbReportViewBandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbReportViewBand.class);
        CrbReportViewBand crbReportViewBand1 = new CrbReportViewBand();
        crbReportViewBand1.setId(1L);
        CrbReportViewBand crbReportViewBand2 = new CrbReportViewBand();
        crbReportViewBand2.setId(crbReportViewBand1.getId());
        assertThat(crbReportViewBand1).isEqualTo(crbReportViewBand2);
        crbReportViewBand2.setId(2L);
        assertThat(crbReportViewBand1).isNotEqualTo(crbReportViewBand2);
        crbReportViewBand1.setId(null);
        assertThat(crbReportViewBand1).isNotEqualTo(crbReportViewBand2);
    }
}
