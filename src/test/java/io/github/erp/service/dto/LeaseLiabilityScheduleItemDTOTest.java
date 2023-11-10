package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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

class LeaseLiabilityScheduleItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseLiabilityScheduleItemDTO.class);
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO1 = new LeaseLiabilityScheduleItemDTO();
        leaseLiabilityScheduleItemDTO1.setId(1L);
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO2 = new LeaseLiabilityScheduleItemDTO();
        assertThat(leaseLiabilityScheduleItemDTO1).isNotEqualTo(leaseLiabilityScheduleItemDTO2);
        leaseLiabilityScheduleItemDTO2.setId(leaseLiabilityScheduleItemDTO1.getId());
        assertThat(leaseLiabilityScheduleItemDTO1).isEqualTo(leaseLiabilityScheduleItemDTO2);
        leaseLiabilityScheduleItemDTO2.setId(2L);
        assertThat(leaseLiabilityScheduleItemDTO1).isNotEqualTo(leaseLiabilityScheduleItemDTO2);
        leaseLiabilityScheduleItemDTO1.setId(null);
        assertThat(leaseLiabilityScheduleItemDTO1).isNotEqualTo(leaseLiabilityScheduleItemDTO2);
    }
}
