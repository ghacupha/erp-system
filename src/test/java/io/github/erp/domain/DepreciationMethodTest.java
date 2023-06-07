package io.github.erp.domain;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
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

class DepreciationMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationMethod.class);
        DepreciationMethod depreciationMethod1 = new DepreciationMethod();
        depreciationMethod1.setId(1L);
        DepreciationMethod depreciationMethod2 = new DepreciationMethod();
        depreciationMethod2.setId(depreciationMethod1.getId());
        assertThat(depreciationMethod1).isEqualTo(depreciationMethod2);
        depreciationMethod2.setId(2L);
        assertThat(depreciationMethod1).isNotEqualTo(depreciationMethod2);
        depreciationMethod1.setId(null);
        assertThat(depreciationMethod1).isNotEqualTo(depreciationMethod2);
    }
}
