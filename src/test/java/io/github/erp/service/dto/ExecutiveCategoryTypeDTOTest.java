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

class ExecutiveCategoryTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutiveCategoryTypeDTO.class);
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO1 = new ExecutiveCategoryTypeDTO();
        executiveCategoryTypeDTO1.setId(1L);
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO2 = new ExecutiveCategoryTypeDTO();
        assertThat(executiveCategoryTypeDTO1).isNotEqualTo(executiveCategoryTypeDTO2);
        executiveCategoryTypeDTO2.setId(executiveCategoryTypeDTO1.getId());
        assertThat(executiveCategoryTypeDTO1).isEqualTo(executiveCategoryTypeDTO2);
        executiveCategoryTypeDTO2.setId(2L);
        assertThat(executiveCategoryTypeDTO1).isNotEqualTo(executiveCategoryTypeDTO2);
        executiveCategoryTypeDTO1.setId(null);
        assertThat(executiveCategoryTypeDTO1).isNotEqualTo(executiveCategoryTypeDTO2);
    }
}
