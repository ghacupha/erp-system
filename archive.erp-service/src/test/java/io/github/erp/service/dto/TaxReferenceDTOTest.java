package io.github.erp.service.dto;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.erp.web.rest.TestUtil;

public class TaxReferenceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxReferenceDTO.class);
        TaxReferenceDTO taxReferenceDTO1 = new TaxReferenceDTO();
        taxReferenceDTO1.setId(1L);
        TaxReferenceDTO taxReferenceDTO2 = new TaxReferenceDTO();
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
        taxReferenceDTO2.setId(taxReferenceDTO1.getId());
        assertThat(taxReferenceDTO1).isEqualTo(taxReferenceDTO2);
        taxReferenceDTO2.setId(2L);
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
        taxReferenceDTO1.setId(null);
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
    }
}
