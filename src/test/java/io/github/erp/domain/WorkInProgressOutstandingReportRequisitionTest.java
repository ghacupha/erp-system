package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

class WorkInProgressOutstandingReportRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressOutstandingReportRequisition.class);
        WorkInProgressOutstandingReportRequisition workInProgressOutstandingReportRequisition1 = new WorkInProgressOutstandingReportRequisition();
        workInProgressOutstandingReportRequisition1.setId(1L);
        WorkInProgressOutstandingReportRequisition workInProgressOutstandingReportRequisition2 = new WorkInProgressOutstandingReportRequisition();
        workInProgressOutstandingReportRequisition2.setId(workInProgressOutstandingReportRequisition1.getId());
        assertThat(workInProgressOutstandingReportRequisition1).isEqualTo(workInProgressOutstandingReportRequisition2);
        workInProgressOutstandingReportRequisition2.setId(2L);
        assertThat(workInProgressOutstandingReportRequisition1).isNotEqualTo(workInProgressOutstandingReportRequisition2);
        workInProgressOutstandingReportRequisition1.setId(null);
        assertThat(workInProgressOutstandingReportRequisition1).isNotEqualTo(workInProgressOutstandingReportRequisition2);
    }
}
