package io.github.erp.domain;

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

class CardCategoryTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardCategoryType.class);
        CardCategoryType cardCategoryType1 = new CardCategoryType();
        cardCategoryType1.setId(1L);
        CardCategoryType cardCategoryType2 = new CardCategoryType();
        cardCategoryType2.setId(cardCategoryType1.getId());
        assertThat(cardCategoryType1).isEqualTo(cardCategoryType2);
        cardCategoryType2.setId(2L);
        assertThat(cardCategoryType1).isNotEqualTo(cardCategoryType2);
        cardCategoryType1.setId(null);
        assertThat(cardCategoryType1).isNotEqualTo(cardCategoryType2);
    }
}
