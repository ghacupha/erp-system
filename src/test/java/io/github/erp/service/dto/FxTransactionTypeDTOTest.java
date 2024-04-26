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

class FxTransactionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxTransactionTypeDTO.class);
        FxTransactionTypeDTO fxTransactionTypeDTO1 = new FxTransactionTypeDTO();
        fxTransactionTypeDTO1.setId(1L);
        FxTransactionTypeDTO fxTransactionTypeDTO2 = new FxTransactionTypeDTO();
        assertThat(fxTransactionTypeDTO1).isNotEqualTo(fxTransactionTypeDTO2);
        fxTransactionTypeDTO2.setId(fxTransactionTypeDTO1.getId());
        assertThat(fxTransactionTypeDTO1).isEqualTo(fxTransactionTypeDTO2);
        fxTransactionTypeDTO2.setId(2L);
        assertThat(fxTransactionTypeDTO1).isNotEqualTo(fxTransactionTypeDTO2);
        fxTransactionTypeDTO1.setId(null);
        assertThat(fxTransactionTypeDTO1).isNotEqualTo(fxTransactionTypeDTO2);
    }
}
