package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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

class FxCustomerTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxCustomerTypeDTO.class);
        FxCustomerTypeDTO fxCustomerTypeDTO1 = new FxCustomerTypeDTO();
        fxCustomerTypeDTO1.setId(1L);
        FxCustomerTypeDTO fxCustomerTypeDTO2 = new FxCustomerTypeDTO();
        assertThat(fxCustomerTypeDTO1).isNotEqualTo(fxCustomerTypeDTO2);
        fxCustomerTypeDTO2.setId(fxCustomerTypeDTO1.getId());
        assertThat(fxCustomerTypeDTO1).isEqualTo(fxCustomerTypeDTO2);
        fxCustomerTypeDTO2.setId(2L);
        assertThat(fxCustomerTypeDTO1).isNotEqualTo(fxCustomerTypeDTO2);
        fxCustomerTypeDTO1.setId(null);
        assertThat(fxCustomerTypeDTO1).isNotEqualTo(fxCustomerTypeDTO2);
    }
}
