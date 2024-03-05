package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

class AutonomousReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutonomousReportDTO.class);
        AutonomousReportDTO autonomousReportDTO1 = new AutonomousReportDTO();
        autonomousReportDTO1.setId(1L);
        AutonomousReportDTO autonomousReportDTO2 = new AutonomousReportDTO();
        assertThat(autonomousReportDTO1).isNotEqualTo(autonomousReportDTO2);
        autonomousReportDTO2.setId(autonomousReportDTO1.getId());
        assertThat(autonomousReportDTO1).isEqualTo(autonomousReportDTO2);
        autonomousReportDTO2.setId(2L);
        assertThat(autonomousReportDTO1).isNotEqualTo(autonomousReportDTO2);
        autonomousReportDTO1.setId(null);
        assertThat(autonomousReportDTO1).isNotEqualTo(autonomousReportDTO2);
    }
}
