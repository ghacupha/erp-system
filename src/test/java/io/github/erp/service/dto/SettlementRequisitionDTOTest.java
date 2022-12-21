package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.1.9-SNAPSHOT
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

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettlementRequisitionDTO.class);
        SettlementRequisitionDTO settlementRequisitionDTO1 = new SettlementRequisitionDTO();
        settlementRequisitionDTO1.setId(1L);
        SettlementRequisitionDTO settlementRequisitionDTO2 = new SettlementRequisitionDTO();
        assertThat(settlementRequisitionDTO1).isNotEqualTo(settlementRequisitionDTO2);
        settlementRequisitionDTO2.setId(settlementRequisitionDTO1.getId());
        assertThat(settlementRequisitionDTO1).isEqualTo(settlementRequisitionDTO2);
        settlementRequisitionDTO2.setId(2L);
        assertThat(settlementRequisitionDTO1).isNotEqualTo(settlementRequisitionDTO2);
        settlementRequisitionDTO1.setId(null);
        assertThat(settlementRequisitionDTO1).isNotEqualTo(settlementRequisitionDTO2);
    }
}
