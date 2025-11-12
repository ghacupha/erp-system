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

class LeasePaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeasePayment.class);
        LeasePayment leasePayment1 = new LeasePayment();
        leasePayment1.setId(1L);
        LeasePayment leasePayment2 = new LeasePayment();
        leasePayment2.setId(leasePayment1.getId());
        assertThat(leasePayment1).isEqualTo(leasePayment2);
        leasePayment2.setId(2L);
        assertThat(leasePayment1).isNotEqualTo(leasePayment2);
        leasePayment1.setId(null);
        assertThat(leasePayment1).isNotEqualTo(leasePayment2);
    }
}
