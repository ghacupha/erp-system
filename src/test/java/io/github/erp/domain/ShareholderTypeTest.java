package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

class ShareholderTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareholderType.class);
        ShareholderType shareholderType1 = new ShareholderType();
        shareholderType1.setId(1L);
        ShareholderType shareholderType2 = new ShareholderType();
        shareholderType2.setId(shareholderType1.getId());
        assertThat(shareholderType1).isEqualTo(shareholderType2);
        shareholderType2.setId(2L);
        assertThat(shareholderType1).isNotEqualTo(shareholderType2);
        shareholderType1.setId(null);
        assertThat(shareholderType1).isNotEqualTo(shareholderType2);
    }
}
