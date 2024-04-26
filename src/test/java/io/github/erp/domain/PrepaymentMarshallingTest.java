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

class PrepaymentMarshallingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentMarshalling.class);
        PrepaymentMarshalling prepaymentMarshalling1 = new PrepaymentMarshalling();
        prepaymentMarshalling1.setId(1L);
        PrepaymentMarshalling prepaymentMarshalling2 = new PrepaymentMarshalling();
        prepaymentMarshalling2.setId(prepaymentMarshalling1.getId());
        assertThat(prepaymentMarshalling1).isEqualTo(prepaymentMarshalling2);
        prepaymentMarshalling2.setId(2L);
        assertThat(prepaymentMarshalling1).isNotEqualTo(prepaymentMarshalling2);
        prepaymentMarshalling1.setId(null);
        assertThat(prepaymentMarshalling1).isNotEqualTo(prepaymentMarshalling2);
    }
}
