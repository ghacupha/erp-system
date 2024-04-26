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

class CrbRecordFileTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbRecordFileType.class);
        CrbRecordFileType crbRecordFileType1 = new CrbRecordFileType();
        crbRecordFileType1.setId(1L);
        CrbRecordFileType crbRecordFileType2 = new CrbRecordFileType();
        crbRecordFileType2.setId(crbRecordFileType1.getId());
        assertThat(crbRecordFileType1).isEqualTo(crbRecordFileType2);
        crbRecordFileType2.setId(2L);
        assertThat(crbRecordFileType1).isNotEqualTo(crbRecordFileType2);
        crbRecordFileType1.setId(null);
        assertThat(crbRecordFileType1).isNotEqualTo(crbRecordFileType2);
    }
}
