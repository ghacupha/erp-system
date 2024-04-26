package io.github.erp.domain;

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

class RouModelMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouModelMetadata.class);
        RouModelMetadata rouModelMetadata1 = new RouModelMetadata();
        rouModelMetadata1.setId(1L);
        RouModelMetadata rouModelMetadata2 = new RouModelMetadata();
        rouModelMetadata2.setId(rouModelMetadata1.getId());
        assertThat(rouModelMetadata1).isEqualTo(rouModelMetadata2);
        rouModelMetadata2.setId(2L);
        assertThat(rouModelMetadata1).isNotEqualTo(rouModelMetadata2);
        rouModelMetadata1.setId(null);
        assertThat(rouModelMetadata1).isNotEqualTo(rouModelMetadata2);
    }
}
