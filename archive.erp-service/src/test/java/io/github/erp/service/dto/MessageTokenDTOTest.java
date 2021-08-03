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

public class MessageTokenDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageTokenDTO.class);
        MessageTokenDTO messageTokenDTO1 = new MessageTokenDTO();
        messageTokenDTO1.setId(1L);
        MessageTokenDTO messageTokenDTO2 = new MessageTokenDTO();
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
        messageTokenDTO2.setId(messageTokenDTO1.getId());
        assertThat(messageTokenDTO1).isEqualTo(messageTokenDTO2);
        messageTokenDTO2.setId(2L);
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
        messageTokenDTO1.setId(null);
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
    }
}
