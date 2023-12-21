package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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

class LoanRestructureFlagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanRestructureFlagDTO.class);
        LoanRestructureFlagDTO loanRestructureFlagDTO1 = new LoanRestructureFlagDTO();
        loanRestructureFlagDTO1.setId(1L);
        LoanRestructureFlagDTO loanRestructureFlagDTO2 = new LoanRestructureFlagDTO();
        assertThat(loanRestructureFlagDTO1).isNotEqualTo(loanRestructureFlagDTO2);
        loanRestructureFlagDTO2.setId(loanRestructureFlagDTO1.getId());
        assertThat(loanRestructureFlagDTO1).isEqualTo(loanRestructureFlagDTO2);
        loanRestructureFlagDTO2.setId(2L);
        assertThat(loanRestructureFlagDTO1).isNotEqualTo(loanRestructureFlagDTO2);
        loanRestructureFlagDTO1.setId(null);
        assertThat(loanRestructureFlagDTO1).isNotEqualTo(loanRestructureFlagDTO2);
    }
}
