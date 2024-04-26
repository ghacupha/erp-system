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

class AccountTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountType.class);
        AccountType accountType1 = new AccountType();
        accountType1.setId(1L);
        AccountType accountType2 = new AccountType();
        accountType2.setId(accountType1.getId());
        assertThat(accountType1).isEqualTo(accountType2);
        accountType2.setId(2L);
        assertThat(accountType1).isNotEqualTo(accountType2);
        accountType1.setId(null);
        assertThat(accountType1).isNotEqualTo(accountType2);
    }
}
