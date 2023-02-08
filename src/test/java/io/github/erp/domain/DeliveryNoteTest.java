package io.github.erp.domain;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
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

import io.github.erp.erp.resources.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryNote.class);
        DeliveryNote deliveryNote1 = new DeliveryNote();
        deliveryNote1.setId(1L);
        DeliveryNote deliveryNote2 = new DeliveryNote();
        deliveryNote2.setId(deliveryNote1.getId());
        assertThat(deliveryNote1).isEqualTo(deliveryNote2);
        deliveryNote2.setId(2L);
        assertThat(deliveryNote1).isNotEqualTo(deliveryNote2);
        deliveryNote1.setId(null);
        assertThat(deliveryNote1).isNotEqualTo(deliveryNote2);
    }
}
