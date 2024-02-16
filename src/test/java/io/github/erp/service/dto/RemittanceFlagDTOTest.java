package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class RemittanceFlagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RemittanceFlagDTO.class);
        RemittanceFlagDTO remittanceFlagDTO1 = new RemittanceFlagDTO();
        remittanceFlagDTO1.setId(1L);
        RemittanceFlagDTO remittanceFlagDTO2 = new RemittanceFlagDTO();
        assertThat(remittanceFlagDTO1).isNotEqualTo(remittanceFlagDTO2);
        remittanceFlagDTO2.setId(remittanceFlagDTO1.getId());
        assertThat(remittanceFlagDTO1).isEqualTo(remittanceFlagDTO2);
        remittanceFlagDTO2.setId(2L);
        assertThat(remittanceFlagDTO1).isNotEqualTo(remittanceFlagDTO2);
        remittanceFlagDTO1.setId(null);
        assertThat(remittanceFlagDTO1).isNotEqualTo(remittanceFlagDTO2);
    }
}
