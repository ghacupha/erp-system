package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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

class FiscalYearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiscalYear.class);
        FiscalYear fiscalYear1 = new FiscalYear();
        fiscalYear1.setId(1L);
        FiscalYear fiscalYear2 = new FiscalYear();
        fiscalYear2.setId(fiscalYear1.getId());
        assertThat(fiscalYear1).isEqualTo(fiscalYear2);
        fiscalYear2.setId(2L);
        assertThat(fiscalYear1).isNotEqualTo(fiscalYear2);
        fiscalYear1.setId(null);
        assertThat(fiscalYear1).isNotEqualTo(fiscalYear2);
    }
}
