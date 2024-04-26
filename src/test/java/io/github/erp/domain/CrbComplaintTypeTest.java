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

class CrbComplaintTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbComplaintType.class);
        CrbComplaintType crbComplaintType1 = new CrbComplaintType();
        crbComplaintType1.setId(1L);
        CrbComplaintType crbComplaintType2 = new CrbComplaintType();
        crbComplaintType2.setId(crbComplaintType1.getId());
        assertThat(crbComplaintType1).isEqualTo(crbComplaintType2);
        crbComplaintType2.setId(2L);
        assertThat(crbComplaintType1).isNotEqualTo(crbComplaintType2);
        crbComplaintType1.setId(null);
        assertThat(crbComplaintType1).isNotEqualTo(crbComplaintType2);
    }
}
