package io.github.erp.domain;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

class SecurityClearanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityClearance.class);
        SecurityClearance securityClearance1 = new SecurityClearance();
        securityClearance1.setId(1L);
        SecurityClearance securityClearance2 = new SecurityClearance();
        securityClearance2.setId(securityClearance1.getId());
        assertThat(securityClearance1).isEqualTo(securityClearance2);
        securityClearance2.setId(2L);
        assertThat(securityClearance1).isNotEqualTo(securityClearance2);
        securityClearance1.setId(null);
        assertThat(securityClearance1).isNotEqualTo(securityClearance2);
    }
}
