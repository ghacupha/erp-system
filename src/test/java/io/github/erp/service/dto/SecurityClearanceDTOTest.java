package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.1-SNAPSHOT
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

class SecurityClearanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityClearanceDTO.class);
        SecurityClearanceDTO securityClearanceDTO1 = new SecurityClearanceDTO();
        securityClearanceDTO1.setId(1L);
        SecurityClearanceDTO securityClearanceDTO2 = new SecurityClearanceDTO();
        assertThat(securityClearanceDTO1).isNotEqualTo(securityClearanceDTO2);
        securityClearanceDTO2.setId(securityClearanceDTO1.getId());
        assertThat(securityClearanceDTO1).isEqualTo(securityClearanceDTO2);
        securityClearanceDTO2.setId(2L);
        assertThat(securityClearanceDTO1).isNotEqualTo(securityClearanceDTO2);
        securityClearanceDTO1.setId(null);
        assertThat(securityClearanceDTO1).isNotEqualTo(securityClearanceDTO2);
    }
}
