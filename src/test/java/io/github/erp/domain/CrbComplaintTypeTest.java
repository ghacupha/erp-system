package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

class CrbComplaintTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbComplaintType.class);
        CrbComplaintType crbComplaintType1 = new CrbComplaintType();
        crbComplaintType1.setId(1L);
        CrbComplaintType crbComplaintType2 = new CrbComplaintType();
        crbComplaintType2.setId(crbComplaintType1.getId());
        assertThat(crbComplaintType1).isEqualTo(crbComplaintType2);
        crbComplaintType2.setId(2L);
        assertThat(crbComplaintType1).isNotEqualTo(crbComplaintType2);
        crbComplaintType1.setId(null);
        assertThat(crbComplaintType1).isNotEqualTo(crbComplaintType2);
    }
}
