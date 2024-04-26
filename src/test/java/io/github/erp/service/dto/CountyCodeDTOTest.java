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

class CountyCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyCodeDTO.class);
        CountyCodeDTO countyCodeDTO1 = new CountyCodeDTO();
        countyCodeDTO1.setId(1L);
        CountyCodeDTO countyCodeDTO2 = new CountyCodeDTO();
        assertThat(countyCodeDTO1).isNotEqualTo(countyCodeDTO2);
        countyCodeDTO2.setId(countyCodeDTO1.getId());
        assertThat(countyCodeDTO1).isEqualTo(countyCodeDTO2);
        countyCodeDTO2.setId(2L);
        assertThat(countyCodeDTO1).isNotEqualTo(countyCodeDTO2);
        countyCodeDTO1.setId(null);
        assertThat(countyCodeDTO1).isNotEqualTo(countyCodeDTO2);
    }
}
