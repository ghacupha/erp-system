package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
