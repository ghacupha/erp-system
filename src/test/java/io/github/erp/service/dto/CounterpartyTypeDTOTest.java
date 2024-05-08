package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

class CounterpartyTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounterpartyTypeDTO.class);
        CounterpartyTypeDTO counterpartyTypeDTO1 = new CounterpartyTypeDTO();
        counterpartyTypeDTO1.setId(1L);
        CounterpartyTypeDTO counterpartyTypeDTO2 = new CounterpartyTypeDTO();
        assertThat(counterpartyTypeDTO1).isNotEqualTo(counterpartyTypeDTO2);
        counterpartyTypeDTO2.setId(counterpartyTypeDTO1.getId());
        assertThat(counterpartyTypeDTO1).isEqualTo(counterpartyTypeDTO2);
        counterpartyTypeDTO2.setId(2L);
        assertThat(counterpartyTypeDTO1).isNotEqualTo(counterpartyTypeDTO2);
        counterpartyTypeDTO1.setId(null);
        assertThat(counterpartyTypeDTO1).isNotEqualTo(counterpartyTypeDTO2);
    }
}
