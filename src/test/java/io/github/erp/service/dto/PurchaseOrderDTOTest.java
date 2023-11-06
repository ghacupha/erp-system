package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

class PurchaseOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderDTO.class);
        PurchaseOrderDTO purchaseOrderDTO1 = new PurchaseOrderDTO();
        purchaseOrderDTO1.setId(1L);
        PurchaseOrderDTO purchaseOrderDTO2 = new PurchaseOrderDTO();
        assertThat(purchaseOrderDTO1).isNotEqualTo(purchaseOrderDTO2);
        purchaseOrderDTO2.setId(purchaseOrderDTO1.getId());
        assertThat(purchaseOrderDTO1).isEqualTo(purchaseOrderDTO2);
        purchaseOrderDTO2.setId(2L);
        assertThat(purchaseOrderDTO1).isNotEqualTo(purchaseOrderDTO2);
        purchaseOrderDTO1.setId(null);
        assertThat(purchaseOrderDTO1).isNotEqualTo(purchaseOrderDTO2);
    }
}
