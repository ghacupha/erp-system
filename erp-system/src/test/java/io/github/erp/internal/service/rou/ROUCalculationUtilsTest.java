package io.github.erp.internal.service.rou;

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
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.*;

class ROUCalculationUtilsTest {


    @Test
    public void testCalculateYearFrac() {
        LocalDate startDate = LocalDate.of(2023, 12, 31);
        LocalDate endDate = LocalDate.of(2029, 3, 31);
        BigDecimal expected = new BigDecimal("5.2500000000");
        BigDecimal actual = ROUCalculationUtils.calculateYearFrac(startDate, endDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateLeaseTermPeriods() {
        LocalDate startDate = LocalDate.of(2023, 12, 31);
        LocalDate endDate = LocalDate.of(2029, 3, 31);
        int expected = 63;
        int actual = ROUCalculationUtils.calculate12MonthPeriodicityLeaseTermPeriods(startDate, endDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateDepreciationAmount() {
        BigDecimal leaseAmount = new BigDecimal("24707759.12");
        LocalDate startDate = LocalDate.of(2023, 12, 31);
        LocalDate endDate = LocalDate.of(2029, 3, 31);
        BigDecimal expected = new BigDecimal("392186.65");  // Expected depreciation amount per period
        BigDecimal actual = ROUCalculationUtils.calculate12MonthlyPeriodicityDepreciationAmount(leaseAmount, startDate, endDate).setScale(2, HALF_UP);
        assertEquals(expected, actual);
    }

}
