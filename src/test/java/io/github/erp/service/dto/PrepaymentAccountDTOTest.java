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

class PrepaymentAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentAccountDTO.class);
        PrepaymentAccountDTO prepaymentAccountDTO1 = new PrepaymentAccountDTO();
        prepaymentAccountDTO1.setId(1L);
        PrepaymentAccountDTO prepaymentAccountDTO2 = new PrepaymentAccountDTO();
        assertThat(prepaymentAccountDTO1).isNotEqualTo(prepaymentAccountDTO2);
        prepaymentAccountDTO2.setId(prepaymentAccountDTO1.getId());
        assertThat(prepaymentAccountDTO1).isEqualTo(prepaymentAccountDTO2);
        prepaymentAccountDTO2.setId(2L);
        assertThat(prepaymentAccountDTO1).isNotEqualTo(prepaymentAccountDTO2);
        prepaymentAccountDTO1.setId(null);
        assertThat(prepaymentAccountDTO1).isNotEqualTo(prepaymentAccountDTO2);
    }
}
