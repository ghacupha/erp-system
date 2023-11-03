package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

class PerformanceOfForeignSubsidiariesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformanceOfForeignSubsidiaries.class);
        PerformanceOfForeignSubsidiaries performanceOfForeignSubsidiaries1 = new PerformanceOfForeignSubsidiaries();
        performanceOfForeignSubsidiaries1.setId(1L);
        PerformanceOfForeignSubsidiaries performanceOfForeignSubsidiaries2 = new PerformanceOfForeignSubsidiaries();
        performanceOfForeignSubsidiaries2.setId(performanceOfForeignSubsidiaries1.getId());
        assertThat(performanceOfForeignSubsidiaries1).isEqualTo(performanceOfForeignSubsidiaries2);
        performanceOfForeignSubsidiaries2.setId(2L);
        assertThat(performanceOfForeignSubsidiaries1).isNotEqualTo(performanceOfForeignSubsidiaries2);
        performanceOfForeignSubsidiaries1.setId(null);
        assertThat(performanceOfForeignSubsidiaries1).isNotEqualTo(performanceOfForeignSubsidiaries2);
    }
}