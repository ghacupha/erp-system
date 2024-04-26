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

class SystemContentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemContentType.class);
        SystemContentType systemContentType1 = new SystemContentType();
        systemContentType1.setId(1L);
        SystemContentType systemContentType2 = new SystemContentType();
        systemContentType2.setId(systemContentType1.getId());
        assertThat(systemContentType1).isEqualTo(systemContentType2);
        systemContentType2.setId(2L);
        assertThat(systemContentType1).isNotEqualTo(systemContentType2);
        systemContentType1.setId(null);
        assertThat(systemContentType1).isNotEqualTo(systemContentType2);
    }
}
