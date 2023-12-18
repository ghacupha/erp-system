package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

class CardChargesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardCharges.class);
        CardCharges cardCharges1 = new CardCharges();
        cardCharges1.setId(1L);
        CardCharges cardCharges2 = new CardCharges();
        cardCharges2.setId(cardCharges1.getId());
        assertThat(cardCharges1).isEqualTo(cardCharges2);
        cardCharges2.setId(2L);
        assertThat(cardCharges1).isNotEqualTo(cardCharges2);
        cardCharges1.setId(null);
        assertThat(cardCharges1).isNotEqualTo(cardCharges2);
    }
}
