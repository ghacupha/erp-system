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

class RouAccountBalanceReportItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouAccountBalanceReportItemDTO.class);
        RouAccountBalanceReportItemDTO rouAccountBalanceReportItemDTO1 = new RouAccountBalanceReportItemDTO();
        rouAccountBalanceReportItemDTO1.setId(1L);
        RouAccountBalanceReportItemDTO rouAccountBalanceReportItemDTO2 = new RouAccountBalanceReportItemDTO();
        assertThat(rouAccountBalanceReportItemDTO1).isNotEqualTo(rouAccountBalanceReportItemDTO2);
        rouAccountBalanceReportItemDTO2.setId(rouAccountBalanceReportItemDTO1.getId());
        assertThat(rouAccountBalanceReportItemDTO1).isEqualTo(rouAccountBalanceReportItemDTO2);
        rouAccountBalanceReportItemDTO2.setId(2L);
        assertThat(rouAccountBalanceReportItemDTO1).isNotEqualTo(rouAccountBalanceReportItemDTO2);
        rouAccountBalanceReportItemDTO1.setId(null);
        assertThat(rouAccountBalanceReportItemDTO1).isNotEqualTo(rouAccountBalanceReportItemDTO2);
    }
}
