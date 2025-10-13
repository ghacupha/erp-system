package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

class WIPListReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WIPListReportDTO.class);
        WIPListReportDTO wIPListReportDTO1 = new WIPListReportDTO();
        wIPListReportDTO1.setId(1L);
        WIPListReportDTO wIPListReportDTO2 = new WIPListReportDTO();
        assertThat(wIPListReportDTO1).isNotEqualTo(wIPListReportDTO2);
        wIPListReportDTO2.setId(wIPListReportDTO1.getId());
        assertThat(wIPListReportDTO1).isEqualTo(wIPListReportDTO2);
        wIPListReportDTO2.setId(2L);
        assertThat(wIPListReportDTO1).isNotEqualTo(wIPListReportDTO2);
        wIPListReportDTO1.setId(null);
        assertThat(wIPListReportDTO1).isNotEqualTo(wIPListReportDTO2);
    }
}
