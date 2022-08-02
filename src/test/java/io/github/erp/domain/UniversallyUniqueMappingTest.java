package io.github.erp.domain;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
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

class UniversallyUniqueMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversallyUniqueMapping.class);
        UniversallyUniqueMapping universallyUniqueMapping1 = new UniversallyUniqueMapping();
        universallyUniqueMapping1.setId(1L);
        UniversallyUniqueMapping universallyUniqueMapping2 = new UniversallyUniqueMapping();
        universallyUniqueMapping2.setId(universallyUniqueMapping1.getId());
        assertThat(universallyUniqueMapping1).isEqualTo(universallyUniqueMapping2);
        universallyUniqueMapping2.setId(2L);
        assertThat(universallyUniqueMapping1).isNotEqualTo(universallyUniqueMapping2);
        universallyUniqueMapping1.setId(null);
        assertThat(universallyUniqueMapping1).isNotEqualTo(universallyUniqueMapping2);
    }
}
