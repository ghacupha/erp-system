package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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

class CrbAccountHolderTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbAccountHolderTypeDTO.class);
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO1 = new CrbAccountHolderTypeDTO();
        crbAccountHolderTypeDTO1.setId(1L);
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO2 = new CrbAccountHolderTypeDTO();
        assertThat(crbAccountHolderTypeDTO1).isNotEqualTo(crbAccountHolderTypeDTO2);
        crbAccountHolderTypeDTO2.setId(crbAccountHolderTypeDTO1.getId());
        assertThat(crbAccountHolderTypeDTO1).isEqualTo(crbAccountHolderTypeDTO2);
        crbAccountHolderTypeDTO2.setId(2L);
        assertThat(crbAccountHolderTypeDTO1).isNotEqualTo(crbAccountHolderTypeDTO2);
        crbAccountHolderTypeDTO1.setId(null);
        assertThat(crbAccountHolderTypeDTO1).isNotEqualTo(crbAccountHolderTypeDTO2);
    }
}
