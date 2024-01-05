package io.github.erp.domain;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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

class InsiderCategoryTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsiderCategoryTypes.class);
        InsiderCategoryTypes insiderCategoryTypes1 = new InsiderCategoryTypes();
        insiderCategoryTypes1.setId(1L);
        InsiderCategoryTypes insiderCategoryTypes2 = new InsiderCategoryTypes();
        insiderCategoryTypes2.setId(insiderCategoryTypes1.getId());
        assertThat(insiderCategoryTypes1).isEqualTo(insiderCategoryTypes2);
        insiderCategoryTypes2.setId(2L);
        assertThat(insiderCategoryTypes1).isNotEqualTo(insiderCategoryTypes2);
        insiderCategoryTypes1.setId(null);
        assertThat(insiderCategoryTypes1).isNotEqualTo(insiderCategoryTypes2);
    }
}
