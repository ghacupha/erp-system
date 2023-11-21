package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

class AssetRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRegistration.class);
        AssetRegistration assetRegistration1 = new AssetRegistration();
        assetRegistration1.setId(1L);
        AssetRegistration assetRegistration2 = new AssetRegistration();
        assetRegistration2.setId(assetRegistration1.getId());
        assertThat(assetRegistration1).isEqualTo(assetRegistration2);
        assetRegistration2.setId(2L);
        assertThat(assetRegistration1).isNotEqualTo(assetRegistration2);
        assetRegistration1.setId(null);
        assertThat(assetRegistration1).isNotEqualTo(assetRegistration2);
    }
}
