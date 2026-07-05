package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class WIPListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WIPListItem.class);
        WIPListItem wIPListItem1 = new WIPListItem();
        wIPListItem1.setId(1L);
        WIPListItem wIPListItem2 = new WIPListItem();
        wIPListItem2.setId(wIPListItem1.getId());
        assertThat(wIPListItem1).isEqualTo(wIPListItem2);
        wIPListItem2.setId(2L);
        assertThat(wIPListItem1).isNotEqualTo(wIPListItem2);
        wIPListItem1.setId(null);
        assertThat(wIPListItem1).isNotEqualTo(wIPListItem2);
    }
}
