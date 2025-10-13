package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

class CollateralInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollateralInformationDTO.class);
        CollateralInformationDTO collateralInformationDTO1 = new CollateralInformationDTO();
        collateralInformationDTO1.setId(1L);
        CollateralInformationDTO collateralInformationDTO2 = new CollateralInformationDTO();
        assertThat(collateralInformationDTO1).isNotEqualTo(collateralInformationDTO2);
        collateralInformationDTO2.setId(collateralInformationDTO1.getId());
        assertThat(collateralInformationDTO1).isEqualTo(collateralInformationDTO2);
        collateralInformationDTO2.setId(2L);
        assertThat(collateralInformationDTO1).isNotEqualTo(collateralInformationDTO2);
        collateralInformationDTO1.setId(null);
        assertThat(collateralInformationDTO1).isNotEqualTo(collateralInformationDTO2);
    }
}
