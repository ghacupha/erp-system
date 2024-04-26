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

class BusinessTeamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessTeamDTO.class);
        BusinessTeamDTO businessTeamDTO1 = new BusinessTeamDTO();
        businessTeamDTO1.setId(1L);
        BusinessTeamDTO businessTeamDTO2 = new BusinessTeamDTO();
        assertThat(businessTeamDTO1).isNotEqualTo(businessTeamDTO2);
        businessTeamDTO2.setId(businessTeamDTO1.getId());
        assertThat(businessTeamDTO1).isEqualTo(businessTeamDTO2);
        businessTeamDTO2.setId(2L);
        assertThat(businessTeamDTO1).isNotEqualTo(businessTeamDTO2);
        businessTeamDTO1.setId(null);
        assertThat(businessTeamDTO1).isNotEqualTo(businessTeamDTO2);
    }
}
