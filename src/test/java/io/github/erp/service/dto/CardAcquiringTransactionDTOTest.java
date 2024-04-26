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

class CardAcquiringTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardAcquiringTransactionDTO.class);
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO1 = new CardAcquiringTransactionDTO();
        cardAcquiringTransactionDTO1.setId(1L);
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO2 = new CardAcquiringTransactionDTO();
        assertThat(cardAcquiringTransactionDTO1).isNotEqualTo(cardAcquiringTransactionDTO2);
        cardAcquiringTransactionDTO2.setId(cardAcquiringTransactionDTO1.getId());
        assertThat(cardAcquiringTransactionDTO1).isEqualTo(cardAcquiringTransactionDTO2);
        cardAcquiringTransactionDTO2.setId(2L);
        assertThat(cardAcquiringTransactionDTO1).isNotEqualTo(cardAcquiringTransactionDTO2);
        cardAcquiringTransactionDTO1.setId(null);
        assertThat(cardAcquiringTransactionDTO1).isNotEqualTo(cardAcquiringTransactionDTO2);
    }
}
