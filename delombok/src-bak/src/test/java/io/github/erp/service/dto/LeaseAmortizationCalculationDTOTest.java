package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

class LeaseAmortizationCalculationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseAmortizationCalculationDTO.class);
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO1 = new LeaseAmortizationCalculationDTO();
        leaseAmortizationCalculationDTO1.setId(1L);
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO2 = new LeaseAmortizationCalculationDTO();
        assertThat(leaseAmortizationCalculationDTO1).isNotEqualTo(leaseAmortizationCalculationDTO2);
        leaseAmortizationCalculationDTO2.setId(leaseAmortizationCalculationDTO1.getId());
        assertThat(leaseAmortizationCalculationDTO1).isEqualTo(leaseAmortizationCalculationDTO2);
        leaseAmortizationCalculationDTO2.setId(2L);
        assertThat(leaseAmortizationCalculationDTO1).isNotEqualTo(leaseAmortizationCalculationDTO2);
        leaseAmortizationCalculationDTO1.setId(null);
        assertThat(leaseAmortizationCalculationDTO1).isNotEqualTo(leaseAmortizationCalculationDTO2);
    }
}
