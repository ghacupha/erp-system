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

class GdiMasterDataIndexTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GdiMasterDataIndex.class);
        GdiMasterDataIndex gdiMasterDataIndex1 = new GdiMasterDataIndex();
        gdiMasterDataIndex1.setId(1L);
        GdiMasterDataIndex gdiMasterDataIndex2 = new GdiMasterDataIndex();
        gdiMasterDataIndex2.setId(gdiMasterDataIndex1.getId());
        assertThat(gdiMasterDataIndex1).isEqualTo(gdiMasterDataIndex2);
        gdiMasterDataIndex2.setId(2L);
        assertThat(gdiMasterDataIndex1).isNotEqualTo(gdiMasterDataIndex2);
        gdiMasterDataIndex1.setId(null);
        assertThat(gdiMasterDataIndex1).isNotEqualTo(gdiMasterDataIndex2);
    }
}
