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

class AssetDisposalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDisposal.class);
        AssetDisposal assetDisposal1 = new AssetDisposal();
        assetDisposal1.setId(1L);
        AssetDisposal assetDisposal2 = new AssetDisposal();
        assetDisposal2.setId(assetDisposal1.getId());
        assertThat(assetDisposal1).isEqualTo(assetDisposal2);
        assetDisposal2.setId(2L);
        assertThat(assetDisposal1).isNotEqualTo(assetDisposal2);
        assetDisposal1.setId(null);
        assertThat(assetDisposal1).isNotEqualTo(assetDisposal2);
    }
}
