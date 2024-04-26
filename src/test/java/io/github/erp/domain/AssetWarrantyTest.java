package io.github.erp.domain;

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
