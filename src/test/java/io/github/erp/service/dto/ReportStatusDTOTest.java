package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 14 (Caleb Series) Server ver 1.1.4-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

class ReportStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportStatusDTO.class);
        ReportStatusDTO reportStatusDTO1 = new ReportStatusDTO();
        reportStatusDTO1.setId(1L);
        ReportStatusDTO reportStatusDTO2 = new ReportStatusDTO();
        assertThat(reportStatusDTO1).isNotEqualTo(reportStatusDTO2);
        reportStatusDTO2.setId(reportStatusDTO1.getId());
        assertThat(reportStatusDTO1).isEqualTo(reportStatusDTO2);
        reportStatusDTO2.setId(2L);
        assertThat(reportStatusDTO1).isNotEqualTo(reportStatusDTO2);
        reportStatusDTO1.setId(null);
        assertThat(reportStatusDTO1).isNotEqualTo(reportStatusDTO2);
    }
}
