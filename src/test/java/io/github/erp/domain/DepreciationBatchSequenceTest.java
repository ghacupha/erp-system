package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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

class DepreciationBatchSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationBatchSequence.class);
        DepreciationBatchSequence depreciationBatchSequence1 = new DepreciationBatchSequence();
        depreciationBatchSequence1.setId(1L);
        DepreciationBatchSequence depreciationBatchSequence2 = new DepreciationBatchSequence();
        depreciationBatchSequence2.setId(depreciationBatchSequence1.getId());
        assertThat(depreciationBatchSequence1).isEqualTo(depreciationBatchSequence2);
        depreciationBatchSequence2.setId(2L);
        assertThat(depreciationBatchSequence1).isNotEqualTo(depreciationBatchSequence2);
        depreciationBatchSequence1.setId(null);
        assertThat(depreciationBatchSequence1).isNotEqualTo(depreciationBatchSequence2);
    }
}
