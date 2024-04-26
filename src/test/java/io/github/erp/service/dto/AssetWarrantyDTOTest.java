package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetWarrantyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetWarrantyDTO.class);
        AssetWarrantyDTO assetWarrantyDTO1 = new AssetWarrantyDTO();
        assetWarrantyDTO1.setId(1L);
        AssetWarrantyDTO assetWarrantyDTO2 = new AssetWarrantyDTO();
        assertThat(assetWarrantyDTO1).isNotEqualTo(assetWarrantyDTO2);
        assetWarrantyDTO2.setId(assetWarrantyDTO1.getId());
        assertThat(assetWarrantyDTO1).isEqualTo(assetWarrantyDTO2);
        assetWarrantyDTO2.setId(2L);
        assertThat(assetWarrantyDTO1).isNotEqualTo(assetWarrantyDTO2);
        assetWarrantyDTO1.setId(null);
        assertThat(assetWarrantyDTO1).isNotEqualTo(assetWarrantyDTO2);
    }
}
