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

class DepreciationEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationEntry.class);
        DepreciationEntry depreciationEntry1 = new DepreciationEntry();
        depreciationEntry1.setId(1L);
        DepreciationEntry depreciationEntry2 = new DepreciationEntry();
        depreciationEntry2.setId(depreciationEntry1.getId());
        assertThat(depreciationEntry1).isEqualTo(depreciationEntry2);
        depreciationEntry2.setId(2L);
        assertThat(depreciationEntry1).isNotEqualTo(depreciationEntry2);
        depreciationEntry1.setId(null);
        assertThat(depreciationEntry1).isNotEqualTo(depreciationEntry2);
    }
}
