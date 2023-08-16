package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.5
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

class FixedAssetNetBookValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetNetBookValueDTO.class);
        FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO1 = new FixedAssetNetBookValueDTO();
        fixedAssetNetBookValueDTO1.setId(1L);
        FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO2 = new FixedAssetNetBookValueDTO();
        assertThat(fixedAssetNetBookValueDTO1).isNotEqualTo(fixedAssetNetBookValueDTO2);
        fixedAssetNetBookValueDTO2.setId(fixedAssetNetBookValueDTO1.getId());
        assertThat(fixedAssetNetBookValueDTO1).isEqualTo(fixedAssetNetBookValueDTO2);
        fixedAssetNetBookValueDTO2.setId(2L);
        assertThat(fixedAssetNetBookValueDTO1).isNotEqualTo(fixedAssetNetBookValueDTO2);
        fixedAssetNetBookValueDTO1.setId(null);
        assertThat(fixedAssetNetBookValueDTO1).isNotEqualTo(fixedAssetNetBookValueDTO2);
    }
}
