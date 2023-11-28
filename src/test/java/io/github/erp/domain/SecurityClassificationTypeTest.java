package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

class SecurityClassificationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityClassificationType.class);
        SecurityClassificationType securityClassificationType1 = new SecurityClassificationType();
        securityClassificationType1.setId(1L);
        SecurityClassificationType securityClassificationType2 = new SecurityClassificationType();
        securityClassificationType2.setId(securityClassificationType1.getId());
        assertThat(securityClassificationType1).isEqualTo(securityClassificationType2);
        securityClassificationType2.setId(2L);
        assertThat(securityClassificationType1).isNotEqualTo(securityClassificationType2);
        securityClassificationType1.setId(null);
        assertThat(securityClassificationType1).isNotEqualTo(securityClassificationType2);
    }
}
