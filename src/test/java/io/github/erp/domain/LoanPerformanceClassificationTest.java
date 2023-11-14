package io.github.erp.domain;

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

class LoanPerformanceClassificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanPerformanceClassification.class);
        LoanPerformanceClassification loanPerformanceClassification1 = new LoanPerformanceClassification();
        loanPerformanceClassification1.setId(1L);
        LoanPerformanceClassification loanPerformanceClassification2 = new LoanPerformanceClassification();
        loanPerformanceClassification2.setId(loanPerformanceClassification1.getId());
        assertThat(loanPerformanceClassification1).isEqualTo(loanPerformanceClassification2);
        loanPerformanceClassification2.setId(2L);
        assertThat(loanPerformanceClassification1).isNotEqualTo(loanPerformanceClassification2);
        loanPerformanceClassification1.setId(null);
        assertThat(loanPerformanceClassification1).isNotEqualTo(loanPerformanceClassification2);
    }
}
