package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

class GenderTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenderType.class);
        GenderType genderType1 = new GenderType();
        genderType1.setId(1L);
        GenderType genderType2 = new GenderType();
        genderType2.setId(genderType1.getId());
        assertThat(genderType1).isEqualTo(genderType2);
        genderType2.setId(2L);
        assertThat(genderType1).isNotEqualTo(genderType2);
        genderType1.setId(null);
        assertThat(genderType1).isNotEqualTo(genderType2);
    }
}
