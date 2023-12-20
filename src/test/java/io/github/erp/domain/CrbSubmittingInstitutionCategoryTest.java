package io.github.erp.domain;

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

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CrbSubmittingInstitutionCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbSubmittingInstitutionCategory.class);
        CrbSubmittingInstitutionCategory crbSubmittingInstitutionCategory1 = new CrbSubmittingInstitutionCategory();
        crbSubmittingInstitutionCategory1.setId(1L);
        CrbSubmittingInstitutionCategory crbSubmittingInstitutionCategory2 = new CrbSubmittingInstitutionCategory();
        crbSubmittingInstitutionCategory2.setId(crbSubmittingInstitutionCategory1.getId());
        assertThat(crbSubmittingInstitutionCategory1).isEqualTo(crbSubmittingInstitutionCategory2);
        crbSubmittingInstitutionCategory2.setId(2L);
        assertThat(crbSubmittingInstitutionCategory1).isNotEqualTo(crbSubmittingInstitutionCategory2);
        crbSubmittingInstitutionCategory1.setId(null);
        assertThat(crbSubmittingInstitutionCategory1).isNotEqualTo(crbSubmittingInstitutionCategory2);
    }
}
