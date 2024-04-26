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

class PlaceholderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Placeholder.class);
        Placeholder placeholder1 = new Placeholder();
        placeholder1.setId(1L);
        Placeholder placeholder2 = new Placeholder();
        placeholder2.setId(placeholder1.getId());
        assertThat(placeholder1).isEqualTo(placeholder2);
        placeholder2.setId(2L);
        assertThat(placeholder1).isNotEqualTo(placeholder2);
        placeholder1.setId(null);
        assertThat(placeholder1).isNotEqualTo(placeholder2);
    }
}
