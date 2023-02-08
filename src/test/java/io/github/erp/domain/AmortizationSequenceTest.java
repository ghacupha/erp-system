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

class AmortizationSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmortizationSequence.class);
        AmortizationSequence amortizationSequence1 = new AmortizationSequence();
        amortizationSequence1.setId(1L);
        AmortizationSequence amortizationSequence2 = new AmortizationSequence();
        amortizationSequence2.setId(amortizationSequence1.getId());
        assertThat(amortizationSequence1).isEqualTo(amortizationSequence2);
        amortizationSequence2.setId(2L);
        assertThat(amortizationSequence1).isNotEqualTo(amortizationSequence2);
        amortizationSequence1.setId(null);
        assertThat(amortizationSequence1).isNotEqualTo(amortizationSequence2);
    }
}
