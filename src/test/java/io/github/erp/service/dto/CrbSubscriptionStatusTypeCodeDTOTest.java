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

class CrbSubscriptionStatusTypeCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbSubscriptionStatusTypeCodeDTO.class);
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO1 = new CrbSubscriptionStatusTypeCodeDTO();
        crbSubscriptionStatusTypeCodeDTO1.setId(1L);
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO2 = new CrbSubscriptionStatusTypeCodeDTO();
        assertThat(crbSubscriptionStatusTypeCodeDTO1).isNotEqualTo(crbSubscriptionStatusTypeCodeDTO2);
        crbSubscriptionStatusTypeCodeDTO2.setId(crbSubscriptionStatusTypeCodeDTO1.getId());
        assertThat(crbSubscriptionStatusTypeCodeDTO1).isEqualTo(crbSubscriptionStatusTypeCodeDTO2);
        crbSubscriptionStatusTypeCodeDTO2.setId(2L);
        assertThat(crbSubscriptionStatusTypeCodeDTO1).isNotEqualTo(crbSubscriptionStatusTypeCodeDTO2);
        crbSubscriptionStatusTypeCodeDTO1.setId(null);
        assertThat(crbSubscriptionStatusTypeCodeDTO1).isNotEqualTo(crbSubscriptionStatusTypeCodeDTO2);
    }
}
