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

class CategoryOfSecurityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryOfSecurity.class);
        CategoryOfSecurity categoryOfSecurity1 = new CategoryOfSecurity();
        categoryOfSecurity1.setId(1L);
        CategoryOfSecurity categoryOfSecurity2 = new CategoryOfSecurity();
        categoryOfSecurity2.setId(categoryOfSecurity1.getId());
        assertThat(categoryOfSecurity1).isEqualTo(categoryOfSecurity2);
        categoryOfSecurity2.setId(2L);
        assertThat(categoryOfSecurity1).isNotEqualTo(categoryOfSecurity2);
        categoryOfSecurity1.setId(null);
        assertThat(categoryOfSecurity1).isNotEqualTo(categoryOfSecurity2);
    }
}
