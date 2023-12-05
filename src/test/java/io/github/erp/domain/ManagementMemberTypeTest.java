package io.github.erp.domain;

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

class ManagementMemberTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManagementMemberType.class);
        ManagementMemberType managementMemberType1 = new ManagementMemberType();
        managementMemberType1.setId(1L);
        ManagementMemberType managementMemberType2 = new ManagementMemberType();
        managementMemberType2.setId(managementMemberType1.getId());
        assertThat(managementMemberType1).isEqualTo(managementMemberType2);
        managementMemberType2.setId(2L);
        assertThat(managementMemberType1).isNotEqualTo(managementMemberType2);
        managementMemberType1.setId(null);
        assertThat(managementMemberType1).isNotEqualTo(managementMemberType2);
    }
}
