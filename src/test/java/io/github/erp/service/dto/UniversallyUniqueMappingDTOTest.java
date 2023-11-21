package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

class UniversallyUniqueMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversallyUniqueMappingDTO.class);
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO1 = new UniversallyUniqueMappingDTO();
        universallyUniqueMappingDTO1.setId(1L);
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO2 = new UniversallyUniqueMappingDTO();
        assertThat(universallyUniqueMappingDTO1).isNotEqualTo(universallyUniqueMappingDTO2);
        universallyUniqueMappingDTO2.setId(universallyUniqueMappingDTO1.getId());
        assertThat(universallyUniqueMappingDTO1).isEqualTo(universallyUniqueMappingDTO2);
        universallyUniqueMappingDTO2.setId(2L);
        assertThat(universallyUniqueMappingDTO1).isNotEqualTo(universallyUniqueMappingDTO2);
        universallyUniqueMappingDTO1.setId(null);
        assertThat(universallyUniqueMappingDTO1).isNotEqualTo(universallyUniqueMappingDTO2);
    }
}
