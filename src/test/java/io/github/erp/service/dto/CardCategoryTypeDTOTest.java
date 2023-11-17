package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

class CardCategoryTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardCategoryTypeDTO.class);
        CardCategoryTypeDTO cardCategoryTypeDTO1 = new CardCategoryTypeDTO();
        cardCategoryTypeDTO1.setId(1L);
        CardCategoryTypeDTO cardCategoryTypeDTO2 = new CardCategoryTypeDTO();
        assertThat(cardCategoryTypeDTO1).isNotEqualTo(cardCategoryTypeDTO2);
        cardCategoryTypeDTO2.setId(cardCategoryTypeDTO1.getId());
        assertThat(cardCategoryTypeDTO1).isEqualTo(cardCategoryTypeDTO2);
        cardCategoryTypeDTO2.setId(2L);
        assertThat(cardCategoryTypeDTO1).isNotEqualTo(cardCategoryTypeDTO2);
        cardCategoryTypeDTO1.setId(null);
        assertThat(cardCategoryTypeDTO1).isNotEqualTo(cardCategoryTypeDTO2);
    }
}
