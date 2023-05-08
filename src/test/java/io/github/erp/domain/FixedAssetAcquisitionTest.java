package io.github.erp.domain;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
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

class FixedAssetAcquisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetAcquisition.class);
        FixedAssetAcquisition fixedAssetAcquisition1 = new FixedAssetAcquisition();
        fixedAssetAcquisition1.setId(1L);
        FixedAssetAcquisition fixedAssetAcquisition2 = new FixedAssetAcquisition();
        fixedAssetAcquisition2.setId(fixedAssetAcquisition1.getId());
        assertThat(fixedAssetAcquisition1).isEqualTo(fixedAssetAcquisition2);
        fixedAssetAcquisition2.setId(2L);
        assertThat(fixedAssetAcquisition1).isNotEqualTo(fixedAssetAcquisition2);
        fixedAssetAcquisition1.setId(null);
        assertThat(fixedAssetAcquisition1).isNotEqualTo(fixedAssetAcquisition2);
    }
}
