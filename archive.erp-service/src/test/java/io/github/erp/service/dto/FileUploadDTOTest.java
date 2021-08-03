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

public class FileUploadDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileUploadDTO.class);
        FileUploadDTO fileUploadDTO1 = new FileUploadDTO();
        fileUploadDTO1.setId(1L);
        FileUploadDTO fileUploadDTO2 = new FileUploadDTO();
        assertThat(fileUploadDTO1).isNotEqualTo(fileUploadDTO2);
        fileUploadDTO2.setId(fileUploadDTO1.getId());
        assertThat(fileUploadDTO1).isEqualTo(fileUploadDTO2);
        fileUploadDTO2.setId(2L);
        assertThat(fileUploadDTO1).isNotEqualTo(fileUploadDTO2);
        fileUploadDTO1.setId(null);
        assertThat(fileUploadDTO1).isNotEqualTo(fileUploadDTO2);
    }
}
