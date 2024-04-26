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

class CrbSourceOfInformationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbSourceOfInformationType.class);
        CrbSourceOfInformationType crbSourceOfInformationType1 = new CrbSourceOfInformationType();
        crbSourceOfInformationType1.setId(1L);
        CrbSourceOfInformationType crbSourceOfInformationType2 = new CrbSourceOfInformationType();
        crbSourceOfInformationType2.setId(crbSourceOfInformationType1.getId());
        assertThat(crbSourceOfInformationType1).isEqualTo(crbSourceOfInformationType2);
        crbSourceOfInformationType2.setId(2L);
        assertThat(crbSourceOfInformationType1).isNotEqualTo(crbSourceOfInformationType2);
        crbSourceOfInformationType1.setId(null);
        assertThat(crbSourceOfInformationType1).isNotEqualTo(crbSourceOfInformationType2);
    }
}
