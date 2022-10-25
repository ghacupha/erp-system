package io.github.erp.domain;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
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

class AssetCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCategory.class);
        AssetCategory assetCategory1 = new AssetCategory();
        assetCategory1.setId(1L);
        AssetCategory assetCategory2 = new AssetCategory();
        assetCategory2.setId(assetCategory1.getId());
        assertThat(assetCategory1).isEqualTo(assetCategory2);
        assetCategory2.setId(2L);
        assertThat(assetCategory1).isNotEqualTo(assetCategory2);
        assetCategory1.setId(null);
        assertThat(assetCategory1).isNotEqualTo(assetCategory2);
    }
}
