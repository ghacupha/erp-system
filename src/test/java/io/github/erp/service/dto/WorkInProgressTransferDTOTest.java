package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class WorkInProgressTransferDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkInProgressTransferDTO.class);
        WorkInProgressTransferDTO workInProgressTransferDTO1 = new WorkInProgressTransferDTO();
        workInProgressTransferDTO1.setId(1L);
        WorkInProgressTransferDTO workInProgressTransferDTO2 = new WorkInProgressTransferDTO();
        assertThat(workInProgressTransferDTO1).isNotEqualTo(workInProgressTransferDTO2);
        workInProgressTransferDTO2.setId(workInProgressTransferDTO1.getId());
        assertThat(workInProgressTransferDTO1).isEqualTo(workInProgressTransferDTO2);
        workInProgressTransferDTO2.setId(2L);
        assertThat(workInProgressTransferDTO1).isNotEqualTo(workInProgressTransferDTO2);
        workInProgressTransferDTO1.setId(null);
        assertThat(workInProgressTransferDTO1).isNotEqualTo(workInProgressTransferDTO2);
    }
}
