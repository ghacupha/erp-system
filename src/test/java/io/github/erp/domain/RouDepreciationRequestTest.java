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

class RouDepreciationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouDepreciationRequest.class);
        RouDepreciationRequest rouDepreciationRequest1 = new RouDepreciationRequest();
        rouDepreciationRequest1.setId(1L);
        RouDepreciationRequest rouDepreciationRequest2 = new RouDepreciationRequest();
        rouDepreciationRequest2.setId(rouDepreciationRequest1.getId());
        assertThat(rouDepreciationRequest1).isEqualTo(rouDepreciationRequest2);
        rouDepreciationRequest2.setId(2L);
        assertThat(rouDepreciationRequest1).isNotEqualTo(rouDepreciationRequest2);
        rouDepreciationRequest1.setId(null);
        assertThat(rouDepreciationRequest1).isNotEqualTo(rouDepreciationRequest2);
    }
}
