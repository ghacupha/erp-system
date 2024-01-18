package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

class CrbFileTransmissionStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrbFileTransmissionStatus.class);
        CrbFileTransmissionStatus crbFileTransmissionStatus1 = new CrbFileTransmissionStatus();
        crbFileTransmissionStatus1.setId(1L);
        CrbFileTransmissionStatus crbFileTransmissionStatus2 = new CrbFileTransmissionStatus();
        crbFileTransmissionStatus2.setId(crbFileTransmissionStatus1.getId());
        assertThat(crbFileTransmissionStatus1).isEqualTo(crbFileTransmissionStatus2);
        crbFileTransmissionStatus2.setId(2L);
        assertThat(crbFileTransmissionStatus1).isNotEqualTo(crbFileTransmissionStatus2);
        crbFileTransmissionStatus1.setId(null);
        assertThat(crbFileTransmissionStatus1).isNotEqualTo(crbFileTransmissionStatus2);
    }
}
