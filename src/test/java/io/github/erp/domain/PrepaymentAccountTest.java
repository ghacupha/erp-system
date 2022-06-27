package io.github.erp.domain;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
