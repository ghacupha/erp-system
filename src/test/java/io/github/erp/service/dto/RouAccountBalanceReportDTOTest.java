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

class RouAccountBalanceReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouAccountBalanceReportDTO.class);
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO1 = new RouAccountBalanceReportDTO();
        rouAccountBalanceReportDTO1.setId(1L);
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO2 = new RouAccountBalanceReportDTO();
        assertThat(rouAccountBalanceReportDTO1).isNotEqualTo(rouAccountBalanceReportDTO2);
        rouAccountBalanceReportDTO2.setId(rouAccountBalanceReportDTO1.getId());
        assertThat(rouAccountBalanceReportDTO1).isEqualTo(rouAccountBalanceReportDTO2);
        rouAccountBalanceReportDTO2.setId(2L);
        assertThat(rouAccountBalanceReportDTO1).isNotEqualTo(rouAccountBalanceReportDTO2);
        rouAccountBalanceReportDTO1.setId(null);
        assertThat(rouAccountBalanceReportDTO1).isNotEqualTo(rouAccountBalanceReportDTO2);
    }
}
