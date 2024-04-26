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

class MfbBranchCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MfbBranchCode.class);
        MfbBranchCode mfbBranchCode1 = new MfbBranchCode();
        mfbBranchCode1.setId(1L);
        MfbBranchCode mfbBranchCode2 = new MfbBranchCode();
        mfbBranchCode2.setId(mfbBranchCode1.getId());
        assertThat(mfbBranchCode1).isEqualTo(mfbBranchCode2);
        mfbBranchCode2.setId(2L);
        assertThat(mfbBranchCode1).isNotEqualTo(mfbBranchCode2);
        mfbBranchCode1.setId(null);
        assertThat(mfbBranchCode1).isNotEqualTo(mfbBranchCode2);
    }
}
