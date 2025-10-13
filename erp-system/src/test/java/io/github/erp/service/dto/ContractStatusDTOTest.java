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

class ContractStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractStatusDTO.class);
        ContractStatusDTO contractStatusDTO1 = new ContractStatusDTO();
        contractStatusDTO1.setId(1L);
        ContractStatusDTO contractStatusDTO2 = new ContractStatusDTO();
        assertThat(contractStatusDTO1).isNotEqualTo(contractStatusDTO2);
        contractStatusDTO2.setId(contractStatusDTO1.getId());
        assertThat(contractStatusDTO1).isEqualTo(contractStatusDTO2);
        contractStatusDTO2.setId(2L);
        assertThat(contractStatusDTO1).isNotEqualTo(contractStatusDTO2);
        contractStatusDTO1.setId(null);
        assertThat(contractStatusDTO1).isNotEqualTo(contractStatusDTO2);
    }
}
