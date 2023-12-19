package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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

class TerminalsAndPOSTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TerminalsAndPOS.class);
        TerminalsAndPOS terminalsAndPOS1 = new TerminalsAndPOS();
        terminalsAndPOS1.setId(1L);
        TerminalsAndPOS terminalsAndPOS2 = new TerminalsAndPOS();
        terminalsAndPOS2.setId(terminalsAndPOS1.getId());
        assertThat(terminalsAndPOS1).isEqualTo(terminalsAndPOS2);
        terminalsAndPOS2.setId(2L);
        assertThat(terminalsAndPOS1).isNotEqualTo(terminalsAndPOS2);
        terminalsAndPOS1.setId(null);
        assertThat(terminalsAndPOS1).isNotEqualTo(terminalsAndPOS2);
    }
}
