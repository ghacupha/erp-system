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

class RouModelMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouModelMetadataDTO.class);
        RouModelMetadataDTO rouModelMetadataDTO1 = new RouModelMetadataDTO();
        rouModelMetadataDTO1.setId(1L);
        RouModelMetadataDTO rouModelMetadataDTO2 = new RouModelMetadataDTO();
        assertThat(rouModelMetadataDTO1).isNotEqualTo(rouModelMetadataDTO2);
        rouModelMetadataDTO2.setId(rouModelMetadataDTO1.getId());
        assertThat(rouModelMetadataDTO1).isEqualTo(rouModelMetadataDTO2);
        rouModelMetadataDTO2.setId(2L);
        assertThat(rouModelMetadataDTO1).isNotEqualTo(rouModelMetadataDTO2);
        rouModelMetadataDTO1.setId(null);
        assertThat(rouModelMetadataDTO1).isNotEqualTo(rouModelMetadataDTO2);
    }
}
