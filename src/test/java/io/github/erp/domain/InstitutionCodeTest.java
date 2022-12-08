package io.github.erp.domain;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
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

class InstitutionCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionCode.class);
        InstitutionCode institutionCode1 = new InstitutionCode();
        institutionCode1.setId(1L);
        InstitutionCode institutionCode2 = new InstitutionCode();
        institutionCode2.setId(institutionCode1.getId());
        assertThat(institutionCode1).isEqualTo(institutionCode2);
        institutionCode2.setId(2L);
        assertThat(institutionCode1).isNotEqualTo(institutionCode2);
        institutionCode1.setId(null);
        assertThat(institutionCode1).isNotEqualTo(institutionCode2);
    }
}
