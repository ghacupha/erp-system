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

class AccountBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBalance.class);
        AccountBalance accountBalance1 = new AccountBalance();
        accountBalance1.setId(1L);
        AccountBalance accountBalance2 = new AccountBalance();
        accountBalance2.setId(accountBalance1.getId());
        assertThat(accountBalance1).isEqualTo(accountBalance2);
        accountBalance2.setId(2L);
        assertThat(accountBalance1).isNotEqualTo(accountBalance2);
        accountBalance1.setId(null);
        assertThat(accountBalance1).isNotEqualTo(accountBalance2);
    }
}
