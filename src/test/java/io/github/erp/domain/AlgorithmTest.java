package io.github.erp.domain;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
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

class AlgorithmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Algorithm.class);
        Algorithm algorithm1 = new Algorithm();
        algorithm1.setId(1L);
        Algorithm algorithm2 = new Algorithm();
        algorithm2.setId(algorithm1.getId());
        assertThat(algorithm1).isEqualTo(algorithm2);
        algorithm2.setId(2L);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
        algorithm1.setId(null);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
    }
}
