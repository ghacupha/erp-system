package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

class SourcesOfFundsTypeCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourcesOfFundsTypeCode.class);
        SourcesOfFundsTypeCode sourcesOfFundsTypeCode1 = new SourcesOfFundsTypeCode();
        sourcesOfFundsTypeCode1.setId(1L);
        SourcesOfFundsTypeCode sourcesOfFundsTypeCode2 = new SourcesOfFundsTypeCode();
        sourcesOfFundsTypeCode2.setId(sourcesOfFundsTypeCode1.getId());
        assertThat(sourcesOfFundsTypeCode1).isEqualTo(sourcesOfFundsTypeCode2);
        sourcesOfFundsTypeCode2.setId(2L);
        assertThat(sourcesOfFundsTypeCode1).isNotEqualTo(sourcesOfFundsTypeCode2);
        sourcesOfFundsTypeCode1.setId(null);
        assertThat(sourcesOfFundsTypeCode1).isNotEqualTo(sourcesOfFundsTypeCode2);
    }
}
