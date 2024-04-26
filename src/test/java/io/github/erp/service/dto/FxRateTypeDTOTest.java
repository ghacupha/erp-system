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

class FxRateTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxRateTypeDTO.class);
        FxRateTypeDTO fxRateTypeDTO1 = new FxRateTypeDTO();
        fxRateTypeDTO1.setId(1L);
        FxRateTypeDTO fxRateTypeDTO2 = new FxRateTypeDTO();
        assertThat(fxRateTypeDTO1).isNotEqualTo(fxRateTypeDTO2);
        fxRateTypeDTO2.setId(fxRateTypeDTO1.getId());
        assertThat(fxRateTypeDTO1).isEqualTo(fxRateTypeDTO2);
        fxRateTypeDTO2.setId(2L);
        assertThat(fxRateTypeDTO1).isNotEqualTo(fxRateTypeDTO2);
        fxRateTypeDTO1.setId(null);
        assertThat(fxRateTypeDTO1).isNotEqualTo(fxRateTypeDTO2);
    }
}
