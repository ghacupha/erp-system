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

class AnticipatedMaturityPerioodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnticipatedMaturityPerioodDTO.class);
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO1 = new AnticipatedMaturityPerioodDTO();
        anticipatedMaturityPerioodDTO1.setId(1L);
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO2 = new AnticipatedMaturityPerioodDTO();
        assertThat(anticipatedMaturityPerioodDTO1).isNotEqualTo(anticipatedMaturityPerioodDTO2);
        anticipatedMaturityPerioodDTO2.setId(anticipatedMaturityPerioodDTO1.getId());
        assertThat(anticipatedMaturityPerioodDTO1).isEqualTo(anticipatedMaturityPerioodDTO2);
        anticipatedMaturityPerioodDTO2.setId(2L);
        assertThat(anticipatedMaturityPerioodDTO1).isNotEqualTo(anticipatedMaturityPerioodDTO2);
        anticipatedMaturityPerioodDTO1.setId(null);
        assertThat(anticipatedMaturityPerioodDTO1).isNotEqualTo(anticipatedMaturityPerioodDTO2);
    }
}
