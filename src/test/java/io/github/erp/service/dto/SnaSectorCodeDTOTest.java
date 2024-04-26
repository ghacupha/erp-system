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

class SnaSectorCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SnaSectorCodeDTO.class);
        SnaSectorCodeDTO snaSectorCodeDTO1 = new SnaSectorCodeDTO();
        snaSectorCodeDTO1.setId(1L);
        SnaSectorCodeDTO snaSectorCodeDTO2 = new SnaSectorCodeDTO();
        assertThat(snaSectorCodeDTO1).isNotEqualTo(snaSectorCodeDTO2);
        snaSectorCodeDTO2.setId(snaSectorCodeDTO1.getId());
        assertThat(snaSectorCodeDTO1).isEqualTo(snaSectorCodeDTO2);
        snaSectorCodeDTO2.setId(2L);
        assertThat(snaSectorCodeDTO1).isNotEqualTo(snaSectorCodeDTO2);
        snaSectorCodeDTO1.setId(null);
        assertThat(snaSectorCodeDTO1).isNotEqualTo(snaSectorCodeDTO2);
    }
}
