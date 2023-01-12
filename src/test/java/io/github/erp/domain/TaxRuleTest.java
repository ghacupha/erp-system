package io.github.erp.domain;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.4.0
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

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxRule.class);
        TaxRule taxRule1 = new TaxRule();
        taxRule1.setId(1L);
        TaxRule taxRule2 = new TaxRule();
        taxRule2.setId(taxRule1.getId());
        assertThat(taxRule1).isEqualTo(taxRule2);
        taxRule2.setId(2L);
        assertThat(taxRule1).isNotEqualTo(taxRule2);
        taxRule1.setId(null);
        assertThat(taxRule1).isNotEqualTo(taxRule2);
    }
}
