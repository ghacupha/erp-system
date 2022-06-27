package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

class SystemContentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemContentTypeDTO.class);
        SystemContentTypeDTO systemContentTypeDTO1 = new SystemContentTypeDTO();
        systemContentTypeDTO1.setId(1L);
        SystemContentTypeDTO systemContentTypeDTO2 = new SystemContentTypeDTO();
        assertThat(systemContentTypeDTO1).isNotEqualTo(systemContentTypeDTO2);
        systemContentTypeDTO2.setId(systemContentTypeDTO1.getId());
        assertThat(systemContentTypeDTO1).isEqualTo(systemContentTypeDTO2);
        systemContentTypeDTO2.setId(2L);
        assertThat(systemContentTypeDTO1).isNotEqualTo(systemContentTypeDTO2);
        systemContentTypeDTO1.setId(null);
        assertThat(systemContentTypeDTO1).isNotEqualTo(systemContentTypeDTO2);
    }
}
