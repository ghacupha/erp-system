package io.github.erp.domain;

/*-
 * Erp System - Mark II No 22 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

class BankBranchCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankBranchCode.class);
        BankBranchCode bankBranchCode1 = new BankBranchCode();
        bankBranchCode1.setId(1L);
        BankBranchCode bankBranchCode2 = new BankBranchCode();
        bankBranchCode2.setId(bankBranchCode1.getId());
        assertThat(bankBranchCode1).isEqualTo(bankBranchCode2);
        bankBranchCode2.setId(2L);
        assertThat(bankBranchCode1).isNotEqualTo(bankBranchCode2);
        bankBranchCode1.setId(null);
        assertThat(bankBranchCode1).isNotEqualTo(bankBranchCode2);
    }
}
