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

class CrbAgingBandsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbAgingBands.class);
        CrbAgingBands crbAgingBands1 = new CrbAgingBands();
        crbAgingBands1.setId(1L);
        CrbAgingBands crbAgingBands2 = new CrbAgingBands();
        crbAgingBands2.setId(crbAgingBands1.getId());
        assertThat(crbAgingBands1).isEqualTo(crbAgingBands2);
        crbAgingBands2.setId(2L);
        assertThat(crbAgingBands1).isNotEqualTo(crbAgingBands2);
        crbAgingBands1.setId(null);
        assertThat(crbAgingBands1).isNotEqualTo(crbAgingBands2);
    }
}
