package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class AssetCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCategoryDTO.class);
        AssetCategoryDTO assetCategoryDTO1 = new AssetCategoryDTO();
        assetCategoryDTO1.setId(1L);
        AssetCategoryDTO assetCategoryDTO2 = new AssetCategoryDTO();
        assertThat(assetCategoryDTO1).isNotEqualTo(assetCategoryDTO2);
        assetCategoryDTO2.setId(assetCategoryDTO1.getId());
        assertThat(assetCategoryDTO1).isEqualTo(assetCategoryDTO2);
        assetCategoryDTO2.setId(2L);
        assertThat(assetCategoryDTO1).isNotEqualTo(assetCategoryDTO2);
        assetCategoryDTO1.setId(null);
        assertThat(assetCategoryDTO1).isNotEqualTo(assetCategoryDTO2);
    }
}
