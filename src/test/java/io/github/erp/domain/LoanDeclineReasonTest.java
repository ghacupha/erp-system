package io.github.erp.domain;

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

class LoanDeclineReasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanDeclineReason.class);
        LoanDeclineReason loanDeclineReason1 = new LoanDeclineReason();
        loanDeclineReason1.setId(1L);
        LoanDeclineReason loanDeclineReason2 = new LoanDeclineReason();
        loanDeclineReason2.setId(loanDeclineReason1.getId());
        assertThat(loanDeclineReason1).isEqualTo(loanDeclineReason2);
        loanDeclineReason2.setId(2L);
        assertThat(loanDeclineReason1).isNotEqualTo(loanDeclineReason2);
        loanDeclineReason1.setId(null);
        assertThat(loanDeclineReason1).isNotEqualTo(loanDeclineReason2);
    }
}
