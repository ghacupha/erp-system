package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

class LoanApplicationTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanApplicationTypeDTO.class);
        LoanApplicationTypeDTO loanApplicationTypeDTO1 = new LoanApplicationTypeDTO();
        loanApplicationTypeDTO1.setId(1L);
        LoanApplicationTypeDTO loanApplicationTypeDTO2 = new LoanApplicationTypeDTO();
        assertThat(loanApplicationTypeDTO1).isNotEqualTo(loanApplicationTypeDTO2);
        loanApplicationTypeDTO2.setId(loanApplicationTypeDTO1.getId());
        assertThat(loanApplicationTypeDTO1).isEqualTo(loanApplicationTypeDTO2);
        loanApplicationTypeDTO2.setId(2L);
        assertThat(loanApplicationTypeDTO1).isNotEqualTo(loanApplicationTypeDTO2);
        loanApplicationTypeDTO1.setId(null);
        assertThat(loanApplicationTypeDTO1).isNotEqualTo(loanApplicationTypeDTO2);
    }
}
