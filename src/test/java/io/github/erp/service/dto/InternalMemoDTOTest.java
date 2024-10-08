package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

class InternalMemoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternalMemoDTO.class);
        InternalMemoDTO internalMemoDTO1 = new InternalMemoDTO();
        internalMemoDTO1.setId(1L);
        InternalMemoDTO internalMemoDTO2 = new InternalMemoDTO();
        assertThat(internalMemoDTO1).isNotEqualTo(internalMemoDTO2);
        internalMemoDTO2.setId(internalMemoDTO1.getId());
        assertThat(internalMemoDTO1).isEqualTo(internalMemoDTO2);
        internalMemoDTO2.setId(2L);
        assertThat(internalMemoDTO1).isNotEqualTo(internalMemoDTO2);
        internalMemoDTO1.setId(null);
        assertThat(internalMemoDTO1).isNotEqualTo(internalMemoDTO2);
    }
}
