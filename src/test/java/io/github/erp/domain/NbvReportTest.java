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

class NbvReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NbvReport.class);
        NbvReport nbvReport1 = new NbvReport();
        nbvReport1.setId(1L);
        NbvReport nbvReport2 = new NbvReport();
        nbvReport2.setId(nbvReport1.getId());
        assertThat(nbvReport1).isEqualTo(nbvReport2);
        nbvReport2.setId(2L);
        assertThat(nbvReport1).isNotEqualTo(nbvReport2);
        nbvReport1.setId(null);
        assertThat(nbvReport1).isNotEqualTo(nbvReport2);
    }
}
