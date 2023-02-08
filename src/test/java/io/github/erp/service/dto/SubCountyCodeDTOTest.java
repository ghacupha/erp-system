package io.github.erp.service.dto;

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

import io.github.erp.erp.resources.TestUtil;
import org.junit.jupiter.api.Test;

class SubCountyCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCountyCodeDTO.class);
        SubCountyCodeDTO subCountyCodeDTO1 = new SubCountyCodeDTO();
        subCountyCodeDTO1.setId(1L);
        SubCountyCodeDTO subCountyCodeDTO2 = new SubCountyCodeDTO();
        assertThat(subCountyCodeDTO1).isNotEqualTo(subCountyCodeDTO2);
        subCountyCodeDTO2.setId(subCountyCodeDTO1.getId());
        assertThat(subCountyCodeDTO1).isEqualTo(subCountyCodeDTO2);
        subCountyCodeDTO2.setId(2L);
        assertThat(subCountyCodeDTO1).isNotEqualTo(subCountyCodeDTO2);
        subCountyCodeDTO1.setId(null);
        assertThat(subCountyCodeDTO1).isNotEqualTo(subCountyCodeDTO2);
    }
}
