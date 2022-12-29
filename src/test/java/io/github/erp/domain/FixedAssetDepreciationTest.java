package io.github.erp.domain;

/*-
 * Erp System - Mark III No 7 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class FixedAssetDepreciationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetDepreciation.class);
        FixedAssetDepreciation fixedAssetDepreciation1 = new FixedAssetDepreciation();
        fixedAssetDepreciation1.setId(1L);
        FixedAssetDepreciation fixedAssetDepreciation2 = new FixedAssetDepreciation();
        fixedAssetDepreciation2.setId(fixedAssetDepreciation1.getId());
        assertThat(fixedAssetDepreciation1).isEqualTo(fixedAssetDepreciation2);
        fixedAssetDepreciation2.setId(2L);
        assertThat(fixedAssetDepreciation1).isNotEqualTo(fixedAssetDepreciation2);
        fixedAssetDepreciation1.setId(null);
        assertThat(fixedAssetDepreciation1).isNotEqualTo(fixedAssetDepreciation2);
    }
}
