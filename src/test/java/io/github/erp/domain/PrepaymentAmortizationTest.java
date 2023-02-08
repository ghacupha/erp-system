package io.github.erp.domain;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.erp.resources.TestUtil;
import org.junit.jupiter.api.Test;

class PrepaymentAmortizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAmortization.class);
        PrepaymentAmortization prepaymentAmortization1 = new PrepaymentAmortization();
        prepaymentAmortization1.setId(1L);
        PrepaymentAmortization prepaymentAmortization2 = new PrepaymentAmortization();
        prepaymentAmortization2.setId(prepaymentAmortization1.getId());
        assertThat(prepaymentAmortization1).isEqualTo(prepaymentAmortization2);
        prepaymentAmortization2.setId(2L);
        assertThat(prepaymentAmortization1).isNotEqualTo(prepaymentAmortization2);
        prepaymentAmortization1.setId(null);
        assertThat(prepaymentAmortization1).isNotEqualTo(prepaymentAmortization2);
    }
}
