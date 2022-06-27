package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

class PrepaymentAmortizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAmortizationDTO.class);
        PrepaymentAmortizationDTO prepaymentAmortizationDTO1 = new PrepaymentAmortizationDTO();
        prepaymentAmortizationDTO1.setId(1L);
        PrepaymentAmortizationDTO prepaymentAmortizationDTO2 = new PrepaymentAmortizationDTO();
        assertThat(prepaymentAmortizationDTO1).isNotEqualTo(prepaymentAmortizationDTO2);
        prepaymentAmortizationDTO2.setId(prepaymentAmortizationDTO1.getId());
        assertThat(prepaymentAmortizationDTO1).isEqualTo(prepaymentAmortizationDTO2);
        prepaymentAmortizationDTO2.setId(2L);
        assertThat(prepaymentAmortizationDTO1).isNotEqualTo(prepaymentAmortizationDTO2);
        prepaymentAmortizationDTO1.setId(null);
        assertThat(prepaymentAmortizationDTO1).isNotEqualTo(prepaymentAmortizationDTO2);
    }
}
