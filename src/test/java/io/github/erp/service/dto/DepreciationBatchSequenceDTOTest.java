package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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

class DepreciationBatchSequenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationBatchSequenceDTO.class);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO1 = new DepreciationBatchSequenceDTO();
        depreciationBatchSequenceDTO1.setId(1L);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO2 = new DepreciationBatchSequenceDTO();
        assertThat(depreciationBatchSequenceDTO1).isNotEqualTo(depreciationBatchSequenceDTO2);
        depreciationBatchSequenceDTO2.setId(depreciationBatchSequenceDTO1.getId());
        assertThat(depreciationBatchSequenceDTO1).isEqualTo(depreciationBatchSequenceDTO2);
        depreciationBatchSequenceDTO2.setId(2L);
        assertThat(depreciationBatchSequenceDTO1).isNotEqualTo(depreciationBatchSequenceDTO2);
        depreciationBatchSequenceDTO1.setId(null);
        assertThat(depreciationBatchSequenceDTO1).isNotEqualTo(depreciationBatchSequenceDTO2);
    }
}
