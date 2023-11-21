package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

class CrbReportRequestReasonsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbReportRequestReasons.class);
        CrbReportRequestReasons crbReportRequestReasons1 = new CrbReportRequestReasons();
        crbReportRequestReasons1.setId(1L);
        CrbReportRequestReasons crbReportRequestReasons2 = new CrbReportRequestReasons();
        crbReportRequestReasons2.setId(crbReportRequestReasons1.getId());
        assertThat(crbReportRequestReasons1).isEqualTo(crbReportRequestReasons2);
        crbReportRequestReasons2.setId(2L);
        assertThat(crbReportRequestReasons1).isNotEqualTo(crbReportRequestReasons2);
        crbReportRequestReasons1.setId(null);
        assertThat(crbReportRequestReasons1).isNotEqualTo(crbReportRequestReasons2);
    }
}
