package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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

class TaxReferenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxReferenceDTO.class);
        TaxReferenceDTO taxReferenceDTO1 = new TaxReferenceDTO();
        taxReferenceDTO1.setId(1L);
        TaxReferenceDTO taxReferenceDTO2 = new TaxReferenceDTO();
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
        taxReferenceDTO2.setId(taxReferenceDTO1.getId());
        assertThat(taxReferenceDTO1).isEqualTo(taxReferenceDTO2);
        taxReferenceDTO2.setId(2L);
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
        taxReferenceDTO1.setId(null);
        assertThat(taxReferenceDTO1).isNotEqualTo(taxReferenceDTO2);
    }
}
