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

class PrepaymentAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAccount.class);
        PrepaymentAccount prepaymentAccount1 = new PrepaymentAccount();
        prepaymentAccount1.setId(1L);
        PrepaymentAccount prepaymentAccount2 = new PrepaymentAccount();
        prepaymentAccount2.setId(prepaymentAccount1.getId());
        assertThat(prepaymentAccount1).isEqualTo(prepaymentAccount2);
        prepaymentAccount2.setId(2L);
        assertThat(prepaymentAccount1).isNotEqualTo(prepaymentAccount2);
        prepaymentAccount1.setId(null);
        assertThat(prepaymentAccount1).isNotEqualTo(prepaymentAccount2);
    }
}
