package io.github.erp.domain;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.1-SNAPSHOT
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

class SystemModuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemModule.class);
        SystemModule systemModule1 = new SystemModule();
        systemModule1.setId(1L);
        SystemModule systemModule2 = new SystemModule();
        systemModule2.setId(systemModule1.getId());
        assertThat(systemModule1).isEqualTo(systemModule2);
        systemModule2.setId(2L);
        assertThat(systemModule1).isNotEqualTo(systemModule2);
        systemModule1.setId(null);
        assertThat(systemModule1).isNotEqualTo(systemModule2);
    }
}
