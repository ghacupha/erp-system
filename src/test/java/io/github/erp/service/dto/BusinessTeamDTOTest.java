package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

class BusinessTeamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessTeamDTO.class);
        BusinessTeamDTO businessTeamDTO1 = new BusinessTeamDTO();
        businessTeamDTO1.setId(1L);
        BusinessTeamDTO businessTeamDTO2 = new BusinessTeamDTO();
        assertThat(businessTeamDTO1).isNotEqualTo(businessTeamDTO2);
        businessTeamDTO2.setId(businessTeamDTO1.getId());
        assertThat(businessTeamDTO1).isEqualTo(businessTeamDTO2);
        businessTeamDTO2.setId(2L);
        assertThat(businessTeamDTO1).isNotEqualTo(businessTeamDTO2);
        businessTeamDTO1.setId(null);
        assertThat(businessTeamDTO1).isNotEqualTo(businessTeamDTO2);
    }
}