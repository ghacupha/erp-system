package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
