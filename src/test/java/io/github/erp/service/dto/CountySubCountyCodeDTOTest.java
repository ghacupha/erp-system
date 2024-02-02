package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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

class CountySubCountyCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountySubCountyCodeDTO.class);
        CountySubCountyCodeDTO countySubCountyCodeDTO1 = new CountySubCountyCodeDTO();
        countySubCountyCodeDTO1.setId(1L);
        CountySubCountyCodeDTO countySubCountyCodeDTO2 = new CountySubCountyCodeDTO();
        assertThat(countySubCountyCodeDTO1).isNotEqualTo(countySubCountyCodeDTO2);
        countySubCountyCodeDTO2.setId(countySubCountyCodeDTO1.getId());
        assertThat(countySubCountyCodeDTO1).isEqualTo(countySubCountyCodeDTO2);
        countySubCountyCodeDTO2.setId(2L);
        assertThat(countySubCountyCodeDTO1).isNotEqualTo(countySubCountyCodeDTO2);
        countySubCountyCodeDTO1.setId(null);
        assertThat(countySubCountyCodeDTO1).isNotEqualTo(countySubCountyCodeDTO2);
    }
}
