package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class StaffCurrentEmploymentStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaffCurrentEmploymentStatusDTO.class);
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO1 = new StaffCurrentEmploymentStatusDTO();
        staffCurrentEmploymentStatusDTO1.setId(1L);
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO2 = new StaffCurrentEmploymentStatusDTO();
        assertThat(staffCurrentEmploymentStatusDTO1).isNotEqualTo(staffCurrentEmploymentStatusDTO2);
        staffCurrentEmploymentStatusDTO2.setId(staffCurrentEmploymentStatusDTO1.getId());
        assertThat(staffCurrentEmploymentStatusDTO1).isEqualTo(staffCurrentEmploymentStatusDTO2);
        staffCurrentEmploymentStatusDTO2.setId(2L);
        assertThat(staffCurrentEmploymentStatusDTO1).isNotEqualTo(staffCurrentEmploymentStatusDTO2);
        staffCurrentEmploymentStatusDTO1.setId(null);
        assertThat(staffCurrentEmploymentStatusDTO1).isNotEqualTo(staffCurrentEmploymentStatusDTO2);
    }
}
