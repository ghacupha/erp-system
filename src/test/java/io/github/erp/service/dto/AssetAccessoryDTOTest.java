package io.github.erp.service.dto;

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

class AssetAccessoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAccessoryDTO.class);
        AssetAccessoryDTO assetAccessoryDTO1 = new AssetAccessoryDTO();
        assetAccessoryDTO1.setId(1L);
        AssetAccessoryDTO assetAccessoryDTO2 = new AssetAccessoryDTO();
        assertThat(assetAccessoryDTO1).isNotEqualTo(assetAccessoryDTO2);
        assetAccessoryDTO2.setId(assetAccessoryDTO1.getId());
        assertThat(assetAccessoryDTO1).isEqualTo(assetAccessoryDTO2);
        assetAccessoryDTO2.setId(2L);
        assertThat(assetAccessoryDTO1).isNotEqualTo(assetAccessoryDTO2);
        assetAccessoryDTO1.setId(null);
        assertThat(assetAccessoryDTO1).isNotEqualTo(assetAccessoryDTO2);
    }
}
