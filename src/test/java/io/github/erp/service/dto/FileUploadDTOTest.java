package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileUploadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
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
