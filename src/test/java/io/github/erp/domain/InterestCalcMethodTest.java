package io.github.erp.domain;

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

class InterestCalcMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterestCalcMethod.class);
        InterestCalcMethod interestCalcMethod1 = new InterestCalcMethod();
        interestCalcMethod1.setId(1L);
        InterestCalcMethod interestCalcMethod2 = new InterestCalcMethod();
        interestCalcMethod2.setId(interestCalcMethod1.getId());
        assertThat(interestCalcMethod1).isEqualTo(interestCalcMethod2);
        interestCalcMethod2.setId(2L);
        assertThat(interestCalcMethod1).isNotEqualTo(interestCalcMethod2);
        interestCalcMethod1.setId(null);
        assertThat(interestCalcMethod1).isNotEqualTo(interestCalcMethod2);
    }
}
