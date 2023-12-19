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

class CrbAmountCategoryBandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbAmountCategoryBandDTO.class);
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO1 = new CrbAmountCategoryBandDTO();
        crbAmountCategoryBandDTO1.setId(1L);
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO2 = new CrbAmountCategoryBandDTO();
        assertThat(crbAmountCategoryBandDTO1).isNotEqualTo(crbAmountCategoryBandDTO2);
        crbAmountCategoryBandDTO2.setId(crbAmountCategoryBandDTO1.getId());
        assertThat(crbAmountCategoryBandDTO1).isEqualTo(crbAmountCategoryBandDTO2);
        crbAmountCategoryBandDTO2.setId(2L);
        assertThat(crbAmountCategoryBandDTO1).isNotEqualTo(crbAmountCategoryBandDTO2);
        crbAmountCategoryBandDTO1.setId(null);
        assertThat(crbAmountCategoryBandDTO1).isNotEqualTo(crbAmountCategoryBandDTO2);
    }
}
