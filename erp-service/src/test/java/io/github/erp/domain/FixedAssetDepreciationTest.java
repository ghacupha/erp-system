package io.github.erp.domain;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class FixedAssetDepreciationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetDepreciation.class);
        FixedAssetDepreciation fixedAssetDepreciation1 = new FixedAssetDepreciation();
        fixedAssetDepreciation1.setId(1L);
        FixedAssetDepreciation fixedAssetDepreciation2 = new FixedAssetDepreciation();
        fixedAssetDepreciation2.setId(fixedAssetDepreciation1.getId());
        assertThat(fixedAssetDepreciation1).isEqualTo(fixedAssetDepreciation2);
        fixedAssetDepreciation2.setId(2L);
        assertThat(fixedAssetDepreciation1).isNotEqualTo(fixedAssetDepreciation2);
        fixedAssetDepreciation1.setId(null);
        assertThat(fixedAssetDepreciation1).isNotEqualTo(fixedAssetDepreciation2);
    }
}
