package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

class AmortizationSequenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmortizationSequenceDTO.class);
        AmortizationSequenceDTO amortizationSequenceDTO1 = new AmortizationSequenceDTO();
        amortizationSequenceDTO1.setId(1L);
        AmortizationSequenceDTO amortizationSequenceDTO2 = new AmortizationSequenceDTO();
        assertThat(amortizationSequenceDTO1).isNotEqualTo(amortizationSequenceDTO2);
        amortizationSequenceDTO2.setId(amortizationSequenceDTO1.getId());
        assertThat(amortizationSequenceDTO1).isEqualTo(amortizationSequenceDTO2);
        amortizationSequenceDTO2.setId(2L);
        assertThat(amortizationSequenceDTO1).isNotEqualTo(amortizationSequenceDTO2);
        amortizationSequenceDTO1.setId(null);
        assertThat(amortizationSequenceDTO1).isNotEqualTo(amortizationSequenceDTO2);
    }
}
