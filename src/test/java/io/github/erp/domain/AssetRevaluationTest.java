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

class AssetRevaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRevaluation.class);
        AssetRevaluation assetRevaluation1 = new AssetRevaluation();
        assetRevaluation1.setId(1L);
        AssetRevaluation assetRevaluation2 = new AssetRevaluation();
        assetRevaluation2.setId(assetRevaluation1.getId());
        assertThat(assetRevaluation1).isEqualTo(assetRevaluation2);
        assetRevaluation2.setId(2L);
        assertThat(assetRevaluation1).isNotEqualTo(assetRevaluation2);
        assetRevaluation1.setId(null);
        assertThat(assetRevaluation1).isNotEqualTo(assetRevaluation2);
    }
}
