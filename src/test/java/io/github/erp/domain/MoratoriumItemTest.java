package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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

class MoratoriumItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoratoriumItem.class);
        MoratoriumItem moratoriumItem1 = new MoratoriumItem();
        moratoriumItem1.setId(1L);
        MoratoriumItem moratoriumItem2 = new MoratoriumItem();
        moratoriumItem2.setId(moratoriumItem1.getId());
        assertThat(moratoriumItem1).isEqualTo(moratoriumItem2);
        moratoriumItem2.setId(2L);
        assertThat(moratoriumItem1).isNotEqualTo(moratoriumItem2);
        moratoriumItem1.setId(null);
        assertThat(moratoriumItem1).isNotEqualTo(moratoriumItem2);
    }
}
