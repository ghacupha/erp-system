package io.github.erp.service.dto;

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

class CrbReportRequestReasonsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbReportRequestReasonsDTO.class);
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO1 = new CrbReportRequestReasonsDTO();
        crbReportRequestReasonsDTO1.setId(1L);
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO2 = new CrbReportRequestReasonsDTO();
        assertThat(crbReportRequestReasonsDTO1).isNotEqualTo(crbReportRequestReasonsDTO2);
        crbReportRequestReasonsDTO2.setId(crbReportRequestReasonsDTO1.getId());
        assertThat(crbReportRequestReasonsDTO1).isEqualTo(crbReportRequestReasonsDTO2);
        crbReportRequestReasonsDTO2.setId(2L);
        assertThat(crbReportRequestReasonsDTO1).isNotEqualTo(crbReportRequestReasonsDTO2);
        crbReportRequestReasonsDTO1.setId(null);
        assertThat(crbReportRequestReasonsDTO1).isNotEqualTo(crbReportRequestReasonsDTO2);
    }
}
