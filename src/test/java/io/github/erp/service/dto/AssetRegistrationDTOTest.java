package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
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

class AssetRegistrationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRegistrationDTO.class);
        AssetRegistrationDTO assetRegistrationDTO1 = new AssetRegistrationDTO();
        assetRegistrationDTO1.setId(1L);
        AssetRegistrationDTO assetRegistrationDTO2 = new AssetRegistrationDTO();
        assertThat(assetRegistrationDTO1).isNotEqualTo(assetRegistrationDTO2);
        assetRegistrationDTO2.setId(assetRegistrationDTO1.getId());
        assertThat(assetRegistrationDTO1).isEqualTo(assetRegistrationDTO2);
        assetRegistrationDTO2.setId(2L);
        assertThat(assetRegistrationDTO1).isNotEqualTo(assetRegistrationDTO2);
        assetRegistrationDTO1.setId(null);
        assertThat(assetRegistrationDTO1).isNotEqualTo(assetRegistrationDTO2);
    }
}
