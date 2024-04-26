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

class AcademicQualificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicQualificationDTO.class);
        AcademicQualificationDTO academicQualificationDTO1 = new AcademicQualificationDTO();
        academicQualificationDTO1.setId(1L);
        AcademicQualificationDTO academicQualificationDTO2 = new AcademicQualificationDTO();
        assertThat(academicQualificationDTO1).isNotEqualTo(academicQualificationDTO2);
        academicQualificationDTO2.setId(academicQualificationDTO1.getId());
        assertThat(academicQualificationDTO1).isEqualTo(academicQualificationDTO2);
        academicQualificationDTO2.setId(2L);
        assertThat(academicQualificationDTO1).isNotEqualTo(academicQualificationDTO2);
        academicQualificationDTO1.setId(null);
        assertThat(academicQualificationDTO1).isNotEqualTo(academicQualificationDTO2);
    }
}
