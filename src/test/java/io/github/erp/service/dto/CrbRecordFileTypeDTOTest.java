package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

class CrbRecordFileTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbRecordFileTypeDTO.class);
        CrbRecordFileTypeDTO crbRecordFileTypeDTO1 = new CrbRecordFileTypeDTO();
        crbRecordFileTypeDTO1.setId(1L);
        CrbRecordFileTypeDTO crbRecordFileTypeDTO2 = new CrbRecordFileTypeDTO();
        assertThat(crbRecordFileTypeDTO1).isNotEqualTo(crbRecordFileTypeDTO2);
        crbRecordFileTypeDTO2.setId(crbRecordFileTypeDTO1.getId());
        assertThat(crbRecordFileTypeDTO1).isEqualTo(crbRecordFileTypeDTO2);
        crbRecordFileTypeDTO2.setId(2L);
        assertThat(crbRecordFileTypeDTO1).isNotEqualTo(crbRecordFileTypeDTO2);
        crbRecordFileTypeDTO1.setId(null);
        assertThat(crbRecordFileTypeDTO1).isNotEqualTo(crbRecordFileTypeDTO2);
    }
}
