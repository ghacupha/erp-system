package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

class RouMonthlyDepreciationReportItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouMonthlyDepreciationReportItem.class);
        RouMonthlyDepreciationReportItem rouMonthlyDepreciationReportItem1 = new RouMonthlyDepreciationReportItem();
        rouMonthlyDepreciationReportItem1.setId(1L);
        RouMonthlyDepreciationReportItem rouMonthlyDepreciationReportItem2 = new RouMonthlyDepreciationReportItem();
        rouMonthlyDepreciationReportItem2.setId(rouMonthlyDepreciationReportItem1.getId());
        assertThat(rouMonthlyDepreciationReportItem1).isEqualTo(rouMonthlyDepreciationReportItem2);
        rouMonthlyDepreciationReportItem2.setId(2L);
        assertThat(rouMonthlyDepreciationReportItem1).isNotEqualTo(rouMonthlyDepreciationReportItem2);
        rouMonthlyDepreciationReportItem1.setId(null);
        assertThat(rouMonthlyDepreciationReportItem1).isNotEqualTo(rouMonthlyDepreciationReportItem2);
    }
}
