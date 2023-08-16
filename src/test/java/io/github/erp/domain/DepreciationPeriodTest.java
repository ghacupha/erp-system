package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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

class DepreciationPeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationPeriod.class);
        DepreciationPeriod depreciationPeriod1 = new DepreciationPeriod();
        depreciationPeriod1.setId(1L);
        DepreciationPeriod depreciationPeriod2 = new DepreciationPeriod();
        depreciationPeriod2.setId(depreciationPeriod1.getId());
        assertThat(depreciationPeriod1).isEqualTo(depreciationPeriod2);
        depreciationPeriod2.setId(2L);
        assertThat(depreciationPeriod1).isNotEqualTo(depreciationPeriod2);
        depreciationPeriod1.setId(null);
        assertThat(depreciationPeriod1).isNotEqualTo(depreciationPeriod2);
    }
}
