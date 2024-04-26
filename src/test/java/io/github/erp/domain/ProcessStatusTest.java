package io.github.erp.domain;

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

class ProcessStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessStatus.class);
        ProcessStatus processStatus1 = new ProcessStatus();
        processStatus1.setId(1L);
        ProcessStatus processStatus2 = new ProcessStatus();
        processStatus2.setId(processStatus1.getId());
        assertThat(processStatus1).isEqualTo(processStatus2);
        processStatus2.setId(2L);
        assertThat(processStatus1).isNotEqualTo(processStatus2);
        processStatus1.setId(null);
        assertThat(processStatus1).isNotEqualTo(processStatus2);
    }
}
