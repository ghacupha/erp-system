package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class CardAcquiringTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardAcquiringTransactionDTO.class);
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO1 = new CardAcquiringTransactionDTO();
        cardAcquiringTransactionDTO1.setId(1L);
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO2 = new CardAcquiringTransactionDTO();
        assertThat(cardAcquiringTransactionDTO1).isNotEqualTo(cardAcquiringTransactionDTO2);
        cardAcquiringTransactionDTO2.setId(cardAcquiringTransactionDTO1.getId());
        assertThat(cardAcquiringTransactionDTO1).isEqualTo(cardAcquiringTransactionDTO2);
        cardAcquiringTransactionDTO2.setId(2L);
        assertThat(cardAcquiringTransactionDTO1).isNotEqualTo(cardAcquiringTransactionDTO2);
        cardAcquiringTransactionDTO1.setId(null);
        assertThat(cardAcquiringTransactionDTO1).isNotEqualTo(cardAcquiringTransactionDTO2);
    }
}
