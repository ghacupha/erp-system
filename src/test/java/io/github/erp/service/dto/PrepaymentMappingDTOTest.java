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

class PrepaymentMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentMappingDTO.class);
        PrepaymentMappingDTO prepaymentMappingDTO1 = new PrepaymentMappingDTO();
        prepaymentMappingDTO1.setId(1L);
        PrepaymentMappingDTO prepaymentMappingDTO2 = new PrepaymentMappingDTO();
        assertThat(prepaymentMappingDTO1).isNotEqualTo(prepaymentMappingDTO2);
        prepaymentMappingDTO2.setId(prepaymentMappingDTO1.getId());
        assertThat(prepaymentMappingDTO1).isEqualTo(prepaymentMappingDTO2);
        prepaymentMappingDTO2.setId(2L);
        assertThat(prepaymentMappingDTO1).isNotEqualTo(prepaymentMappingDTO2);
        prepaymentMappingDTO1.setId(null);
        assertThat(prepaymentMappingDTO1).isNotEqualTo(prepaymentMappingDTO2);
    }
}
