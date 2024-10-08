package io.github.erp.domain;

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

class AccountAttributeMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountAttributeMetadata.class);
        AccountAttributeMetadata accountAttributeMetadata1 = new AccountAttributeMetadata();
        accountAttributeMetadata1.setId(1L);
        AccountAttributeMetadata accountAttributeMetadata2 = new AccountAttributeMetadata();
        accountAttributeMetadata2.setId(accountAttributeMetadata1.getId());
        assertThat(accountAttributeMetadata1).isEqualTo(accountAttributeMetadata2);
        accountAttributeMetadata2.setId(2L);
        assertThat(accountAttributeMetadata1).isNotEqualTo(accountAttributeMetadata2);
        accountAttributeMetadata1.setId(null);
        assertThat(accountAttributeMetadata1).isNotEqualTo(accountAttributeMetadata2);
    }
}
