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

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkInProgressReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressReport.class);
        WorkInProgressReport workInProgressReport1 = new WorkInProgressReport();
        workInProgressReport1.setId(1L);
        WorkInProgressReport workInProgressReport2 = new WorkInProgressReport();
        workInProgressReport2.setId(workInProgressReport1.getId());
        assertThat(workInProgressReport1).isEqualTo(workInProgressReport2);
        workInProgressReport2.setId(2L);
        assertThat(workInProgressReport1).isNotEqualTo(workInProgressReport2);
        workInProgressReport1.setId(null);
        assertThat(workInProgressReport1).isNotEqualTo(workInProgressReport2);
    }
}
