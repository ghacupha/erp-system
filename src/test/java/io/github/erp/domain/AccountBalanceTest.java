package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
