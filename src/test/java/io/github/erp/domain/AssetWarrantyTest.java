package io.github.erp.domain;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

class AssetWarrantyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetWarranty.class);
        AssetWarranty assetWarranty1 = new AssetWarranty();
        assetWarranty1.setId(1L);
        AssetWarranty assetWarranty2 = new AssetWarranty();
        assetWarranty2.setId(assetWarranty1.getId());
        assertThat(assetWarranty1).isEqualTo(assetWarranty2);
        assetWarranty2.setId(2L);
        assertThat(assetWarranty1).isNotEqualTo(assetWarranty2);
        assetWarranty1.setId(null);
        assertThat(assetWarranty1).isNotEqualTo(assetWarranty2);
    }
}
