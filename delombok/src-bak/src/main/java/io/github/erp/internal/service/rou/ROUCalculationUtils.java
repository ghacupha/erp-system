package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Key in this utility implementation is the calculation of lease terms which depends on
 * calculation of the fractional year period using the 30/360 day count basis. Further the
 * periodicity of the application is set to monthly basis as configured with the lease periods
 * so the yearFrac is multiplied by 12
 */
public class ROUCalculationUtils {

    public static BigDecimal calculate12MonthlyPeriodicityDepreciationAmount(BigDecimal leaseAmount, LocalDate startDate, LocalDate endDate) {
        return calculateDepreciationAmount(leaseAmount, startDate, endDate,12);
    }

    public static BigDecimal calculateDepreciationAmount(BigDecimal leaseAmount, LocalDate startDate, LocalDate endDate, int periodicity) {
        return leaseAmount.divide(BigDecimal.valueOf(calculateLeaseTermPeriods(startDate, endDate, periodicity)), 10, RoundingMode.HALF_UP);
    }

    /**
     * For monthly periodicity use the value 12 which stands for the 12 month period in a year
     * @param startDate This is the start of the period covered
     * @param endDate End of the period covered
     * @return Number of lease periods
     */
    public static int calculate12MonthPeriodicityLeaseTermPeriods(LocalDate startDate, LocalDate endDate) {

        return calculateLeaseTermPeriods(startDate, endDate, 12);

    }

    /**
     * For monthly periodicity use the value 12 which stands for the 12 month period in a year
     * @param startDate This is the start of the period covered
     * @param endDate End of the period covered
     * @param periodicity Number of periods per year
     * @return Number of lease periods
     */
    public static int calculateLeaseTermPeriods(LocalDate startDate, LocalDate endDate, int periodicity) {

        return calculateYearFrac(startDate, endDate).multiply(BigDecimal.valueOf(periodicity)).setScale(0, RoundingMode.DOWN).intValue();

    }

    // Method to calculate the fractional year difference using US(NASD) 30/360 day count basis
    public static BigDecimal calculateYearFrac(LocalDate startDate, LocalDate endDate) {
        int startDay = startDate.getDayOfMonth();
        int startMonth = startDate.getMonthValue();
        int startYear = startDate.getYear();

        int endDay = endDate.getDayOfMonth();
        int endMonth = endDate.getMonthValue();
        int endYear = endDate.getYear();

        if (startDay == 31 || (startMonth == 2 && isLastDayOfMonth(startDate))) {
            startDay = 30;
        }

        if (endDay == 31 && startDay == 30) {
            endDay = 30;
        }

        int days = endDay - startDay;
        int months = endMonth - startMonth;
        int years = endYear - startYear;

        BigDecimal totalDays = BigDecimal.valueOf(360 * years + 30 * months + days);
        BigDecimal yearFrac = totalDays.divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_UP);
        return yearFrac;
    }

    // Helper method to check if a date is the last day of the month
    private static boolean isLastDayOfMonth(LocalDate date) {
        return date.getDayOfMonth() == date.lengthOfMonth();
    }
}
