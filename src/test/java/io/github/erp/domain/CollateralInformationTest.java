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

class CollateralInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollateralInformation.class);
        CollateralInformation collateralInformation1 = new CollateralInformation();
        collateralInformation1.setId(1L);
        CollateralInformation collateralInformation2 = new CollateralInformation();
        collateralInformation2.setId(collateralInformation1.getId());
        assertThat(collateralInformation1).isEqualTo(collateralInformation2);
        collateralInformation2.setId(2L);
        assertThat(collateralInformation1).isNotEqualTo(collateralInformation2);
        collateralInformation1.setId(null);
        assertThat(collateralInformation1).isNotEqualTo(collateralInformation2);
    }
}
