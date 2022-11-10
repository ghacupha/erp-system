package io.github.erp.domain;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
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

class PaymentLabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentLabel.class);
        PaymentLabel paymentLabel1 = new PaymentLabel();
        paymentLabel1.setId(1L);
        PaymentLabel paymentLabel2 = new PaymentLabel();
        paymentLabel2.setId(paymentLabel1.getId());
        assertThat(paymentLabel1).isEqualTo(paymentLabel2);
        paymentLabel2.setId(2L);
        assertThat(paymentLabel1).isNotEqualTo(paymentLabel2);
        paymentLabel1.setId(null);
        assertThat(paymentLabel1).isNotEqualTo(paymentLabel2);
    }
}
