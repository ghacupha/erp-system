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

class DerivativeUnderlyingAssetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DerivativeUnderlyingAssetDTO.class);
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO1 = new DerivativeUnderlyingAssetDTO();
        derivativeUnderlyingAssetDTO1.setId(1L);
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO2 = new DerivativeUnderlyingAssetDTO();
        assertThat(derivativeUnderlyingAssetDTO1).isNotEqualTo(derivativeUnderlyingAssetDTO2);
        derivativeUnderlyingAssetDTO2.setId(derivativeUnderlyingAssetDTO1.getId());
        assertThat(derivativeUnderlyingAssetDTO1).isEqualTo(derivativeUnderlyingAssetDTO2);
        derivativeUnderlyingAssetDTO2.setId(2L);
        assertThat(derivativeUnderlyingAssetDTO1).isNotEqualTo(derivativeUnderlyingAssetDTO2);
        derivativeUnderlyingAssetDTO1.setId(null);
        assertThat(derivativeUnderlyingAssetDTO1).isNotEqualTo(derivativeUnderlyingAssetDTO2);
    }
}
