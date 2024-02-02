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

class AccountAttributeMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountAttributeMetadataDTO.class);
        AccountAttributeMetadataDTO accountAttributeMetadataDTO1 = new AccountAttributeMetadataDTO();
        accountAttributeMetadataDTO1.setId(1L);
        AccountAttributeMetadataDTO accountAttributeMetadataDTO2 = new AccountAttributeMetadataDTO();
        assertThat(accountAttributeMetadataDTO1).isNotEqualTo(accountAttributeMetadataDTO2);
        accountAttributeMetadataDTO2.setId(accountAttributeMetadataDTO1.getId());
        assertThat(accountAttributeMetadataDTO1).isEqualTo(accountAttributeMetadataDTO2);
        accountAttributeMetadataDTO2.setId(2L);
        assertThat(accountAttributeMetadataDTO1).isNotEqualTo(accountAttributeMetadataDTO2);
        accountAttributeMetadataDTO1.setId(null);
        assertThat(accountAttributeMetadataDTO1).isNotEqualTo(accountAttributeMetadataDTO2);
    }
}
