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

class TaxReferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxReference.class);
        TaxReference taxReference1 = new TaxReference();
        taxReference1.setId(1L);
        TaxReference taxReference2 = new TaxReference();
        taxReference2.setId(taxReference1.getId());
        assertThat(taxReference1).isEqualTo(taxReference2);
        taxReference2.setId(2L);
        assertThat(taxReference1).isNotEqualTo(taxReference2);
        taxReference1.setId(null);
        assertThat(taxReference1).isNotEqualTo(taxReference2);
    }
}
