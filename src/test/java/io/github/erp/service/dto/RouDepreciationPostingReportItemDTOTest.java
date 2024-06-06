package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

class RouDepreciationPostingReportItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouDepreciationPostingReportItemDTO.class);
        RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO1 = new RouDepreciationPostingReportItemDTO();
        rouDepreciationPostingReportItemDTO1.setId(1L);
        RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO2 = new RouDepreciationPostingReportItemDTO();
        assertThat(rouDepreciationPostingReportItemDTO1).isNotEqualTo(rouDepreciationPostingReportItemDTO2);
        rouDepreciationPostingReportItemDTO2.setId(rouDepreciationPostingReportItemDTO1.getId());
        assertThat(rouDepreciationPostingReportItemDTO1).isEqualTo(rouDepreciationPostingReportItemDTO2);
        rouDepreciationPostingReportItemDTO2.setId(2L);
        assertThat(rouDepreciationPostingReportItemDTO1).isNotEqualTo(rouDepreciationPostingReportItemDTO2);
        rouDepreciationPostingReportItemDTO1.setId(null);
        assertThat(rouDepreciationPostingReportItemDTO1).isNotEqualTo(rouDepreciationPostingReportItemDTO2);
    }
}
