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

class JobSheetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobSheetDTO.class);
        JobSheetDTO jobSheetDTO1 = new JobSheetDTO();
        jobSheetDTO1.setId(1L);
        JobSheetDTO jobSheetDTO2 = new JobSheetDTO();
        assertThat(jobSheetDTO1).isNotEqualTo(jobSheetDTO2);
        jobSheetDTO2.setId(jobSheetDTO1.getId());
        assertThat(jobSheetDTO1).isEqualTo(jobSheetDTO2);
        jobSheetDTO2.setId(2L);
        assertThat(jobSheetDTO1).isNotEqualTo(jobSheetDTO2);
        jobSheetDTO1.setId(null);
        assertThat(jobSheetDTO1).isNotEqualTo(jobSheetDTO2);
    }
}
