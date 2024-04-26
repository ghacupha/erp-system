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

class CrbProductServiceFeeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbProductServiceFeeType.class);
        CrbProductServiceFeeType crbProductServiceFeeType1 = new CrbProductServiceFeeType();
        crbProductServiceFeeType1.setId(1L);
        CrbProductServiceFeeType crbProductServiceFeeType2 = new CrbProductServiceFeeType();
        crbProductServiceFeeType2.setId(crbProductServiceFeeType1.getId());
        assertThat(crbProductServiceFeeType1).isEqualTo(crbProductServiceFeeType2);
        crbProductServiceFeeType2.setId(2L);
        assertThat(crbProductServiceFeeType1).isNotEqualTo(crbProductServiceFeeType2);
        crbProductServiceFeeType1.setId(null);
        assertThat(crbProductServiceFeeType1).isNotEqualTo(crbProductServiceFeeType2);
    }
}
