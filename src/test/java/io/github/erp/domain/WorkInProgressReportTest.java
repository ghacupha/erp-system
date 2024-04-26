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
