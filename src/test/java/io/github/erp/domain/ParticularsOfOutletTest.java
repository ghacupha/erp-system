package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

class ParticularsOfOutletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticularsOfOutlet.class);
        ParticularsOfOutlet particularsOfOutlet1 = new ParticularsOfOutlet();
        particularsOfOutlet1.setId(1L);
        ParticularsOfOutlet particularsOfOutlet2 = new ParticularsOfOutlet();
        particularsOfOutlet2.setId(particularsOfOutlet1.getId());
        assertThat(particularsOfOutlet1).isEqualTo(particularsOfOutlet2);
        particularsOfOutlet2.setId(2L);
        assertThat(particularsOfOutlet1).isNotEqualTo(particularsOfOutlet2);
        particularsOfOutlet1.setId(null);
        assertThat(particularsOfOutlet1).isNotEqualTo(particularsOfOutlet2);
    }
}
