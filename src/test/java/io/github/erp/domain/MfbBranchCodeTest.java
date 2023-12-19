package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MfbBranchCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MfbBranchCode.class);
        MfbBranchCode mfbBranchCode1 = new MfbBranchCode();
        mfbBranchCode1.setId(1L);
        MfbBranchCode mfbBranchCode2 = new MfbBranchCode();
        mfbBranchCode2.setId(mfbBranchCode1.getId());
        assertThat(mfbBranchCode1).isEqualTo(mfbBranchCode2);
        mfbBranchCode2.setId(2L);
        assertThat(mfbBranchCode1).isNotEqualTo(mfbBranchCode2);
        mfbBranchCode1.setId(null);
        assertThat(mfbBranchCode1).isNotEqualTo(mfbBranchCode2);
    }
}
