package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

class CreditCardOwnershipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditCardOwnershipDTO.class);
        CreditCardOwnershipDTO creditCardOwnershipDTO1 = new CreditCardOwnershipDTO();
        creditCardOwnershipDTO1.setId(1L);
        CreditCardOwnershipDTO creditCardOwnershipDTO2 = new CreditCardOwnershipDTO();
        assertThat(creditCardOwnershipDTO1).isNotEqualTo(creditCardOwnershipDTO2);
        creditCardOwnershipDTO2.setId(creditCardOwnershipDTO1.getId());
        assertThat(creditCardOwnershipDTO1).isEqualTo(creditCardOwnershipDTO2);
        creditCardOwnershipDTO2.setId(2L);
        assertThat(creditCardOwnershipDTO1).isNotEqualTo(creditCardOwnershipDTO2);
        creditCardOwnershipDTO1.setId(null);
        assertThat(creditCardOwnershipDTO1).isNotEqualTo(creditCardOwnershipDTO2);
    }
}
