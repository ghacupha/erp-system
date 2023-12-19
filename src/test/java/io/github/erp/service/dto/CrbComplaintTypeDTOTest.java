package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class CrbComplaintTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbComplaintTypeDTO.class);
        CrbComplaintTypeDTO crbComplaintTypeDTO1 = new CrbComplaintTypeDTO();
        crbComplaintTypeDTO1.setId(1L);
        CrbComplaintTypeDTO crbComplaintTypeDTO2 = new CrbComplaintTypeDTO();
        assertThat(crbComplaintTypeDTO1).isNotEqualTo(crbComplaintTypeDTO2);
        crbComplaintTypeDTO2.setId(crbComplaintTypeDTO1.getId());
        assertThat(crbComplaintTypeDTO1).isEqualTo(crbComplaintTypeDTO2);
        crbComplaintTypeDTO2.setId(2L);
        assertThat(crbComplaintTypeDTO1).isNotEqualTo(crbComplaintTypeDTO2);
        crbComplaintTypeDTO1.setId(null);
        assertThat(crbComplaintTypeDTO1).isNotEqualTo(crbComplaintTypeDTO2);
    }
}
