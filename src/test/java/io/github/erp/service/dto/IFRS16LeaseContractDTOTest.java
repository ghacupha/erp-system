package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

class IFRS16LeaseContractDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IFRS16LeaseContractDTO.class);
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO1 = new IFRS16LeaseContractDTO();
        iFRS16LeaseContractDTO1.setId(1L);
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO2 = new IFRS16LeaseContractDTO();
        assertThat(iFRS16LeaseContractDTO1).isNotEqualTo(iFRS16LeaseContractDTO2);
        iFRS16LeaseContractDTO2.setId(iFRS16LeaseContractDTO1.getId());
        assertThat(iFRS16LeaseContractDTO1).isEqualTo(iFRS16LeaseContractDTO2);
        iFRS16LeaseContractDTO2.setId(2L);
        assertThat(iFRS16LeaseContractDTO1).isNotEqualTo(iFRS16LeaseContractDTO2);
        iFRS16LeaseContractDTO1.setId(null);
        assertThat(iFRS16LeaseContractDTO1).isNotEqualTo(iFRS16LeaseContractDTO2);
    }
}
