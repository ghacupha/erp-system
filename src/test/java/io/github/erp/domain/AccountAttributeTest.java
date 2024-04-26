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

class AccountAttributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountAttribute.class);
        AccountAttribute accountAttribute1 = new AccountAttribute();
        accountAttribute1.setId(1L);
        AccountAttribute accountAttribute2 = new AccountAttribute();
        accountAttribute2.setId(accountAttribute1.getId());
        assertThat(accountAttribute1).isEqualTo(accountAttribute2);
        accountAttribute2.setId(2L);
        assertThat(accountAttribute1).isNotEqualTo(accountAttribute2);
        accountAttribute1.setId(null);
        assertThat(accountAttribute1).isNotEqualTo(accountAttribute2);
    }
}
