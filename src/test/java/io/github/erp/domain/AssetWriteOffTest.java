package io.github.erp.domain;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class AssetWriteOffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetWriteOff.class);
        AssetWriteOff assetWriteOff1 = new AssetWriteOff();
        assetWriteOff1.setId(1L);
        AssetWriteOff assetWriteOff2 = new AssetWriteOff();
        assetWriteOff2.setId(assetWriteOff1.getId());
        assertThat(assetWriteOff1).isEqualTo(assetWriteOff2);
        assetWriteOff2.setId(2L);
        assertThat(assetWriteOff1).isNotEqualTo(assetWriteOff2);
        assetWriteOff1.setId(null);
        assertThat(assetWriteOff1).isNotEqualTo(assetWriteOff2);
    }
}
