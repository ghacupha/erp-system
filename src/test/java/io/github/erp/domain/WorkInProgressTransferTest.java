package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

class WorkInProgressTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressTransfer.class);
        WorkInProgressTransfer workInProgressTransfer1 = new WorkInProgressTransfer();
        workInProgressTransfer1.setId(1L);
        WorkInProgressTransfer workInProgressTransfer2 = new WorkInProgressTransfer();
        workInProgressTransfer2.setId(workInProgressTransfer1.getId());
        assertThat(workInProgressTransfer1).isEqualTo(workInProgressTransfer2);
        workInProgressTransfer2.setId(2L);
        assertThat(workInProgressTransfer1).isNotEqualTo(workInProgressTransfer2);
        workInProgressTransfer1.setId(null);
        assertThat(workInProgressTransfer1).isNotEqualTo(workInProgressTransfer2);
    }
}
