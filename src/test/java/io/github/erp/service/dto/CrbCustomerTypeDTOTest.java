package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

class CrbCustomerTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbCustomerTypeDTO.class);
        CrbCustomerTypeDTO crbCustomerTypeDTO1 = new CrbCustomerTypeDTO();
        crbCustomerTypeDTO1.setId(1L);
        CrbCustomerTypeDTO crbCustomerTypeDTO2 = new CrbCustomerTypeDTO();
        assertThat(crbCustomerTypeDTO1).isNotEqualTo(crbCustomerTypeDTO2);
        crbCustomerTypeDTO2.setId(crbCustomerTypeDTO1.getId());
        assertThat(crbCustomerTypeDTO1).isEqualTo(crbCustomerTypeDTO2);
        crbCustomerTypeDTO2.setId(2L);
        assertThat(crbCustomerTypeDTO1).isNotEqualTo(crbCustomerTypeDTO2);
        crbCustomerTypeDTO1.setId(null);
        assertThat(crbCustomerTypeDTO1).isNotEqualTo(crbCustomerTypeDTO2);
    }
}
