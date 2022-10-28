package io.github.erp.domain;

/*-
 * Erp System - Mark III No 3 (Caleb Series) Server ver 0.1.3-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

class IsoCountryCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsoCountryCode.class);
        IsoCountryCode isoCountryCode1 = new IsoCountryCode();
        isoCountryCode1.setId(1L);
        IsoCountryCode isoCountryCode2 = new IsoCountryCode();
        isoCountryCode2.setId(isoCountryCode1.getId());
        assertThat(isoCountryCode1).isEqualTo(isoCountryCode2);
        isoCountryCode2.setId(2L);
        assertThat(isoCountryCode1).isNotEqualTo(isoCountryCode2);
        isoCountryCode1.setId(null);
        assertThat(isoCountryCode1).isNotEqualTo(isoCountryCode2);
    }
}
