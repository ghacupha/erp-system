package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.*;
import static java.lang.Long.max;
import static java.lang.Long.min;

/**
 * Here we institute common utilities so we can establish harmony between actual code and test code approaches
 */
public class DepreciationUtility {

    /**
     * The prior period is the period between capitalization date and the start of the
     * depreciation period. This method is designed to be stable even when the capitalization
     * date starts after the depreciation period, by only return positive values or zero
     *
     * @param endDate            The depreciation-period end date
     * @param capitalizationDate The capitalization date of the asset
     * @param elapsedMonths      The period elapsed between depreciation-period start and end dates regardless of capitalization date
     * @return prior period before depreciation start date as months in long
     */
    public static long getPriorPeriodInMonths(LocalDate endDate, LocalDate capitalizationDate, long elapsedMonths) {

        return max(ChronoUnit.MONTHS.between(capitalizationDate, endDate) - elapsedMonths, 0);
    }

    /**
     * The input values for depreciation rate at the client front-end are done as basis points, as a matter of policy. This method
     * converts that value into actual depreciation rate.
     * The point in not doing the same at the front end is that the jhipster generated client which is either
     * angular or reactjs limits the values you can input into a big decimal to two decimal places. This issue
     * became apparent late in the UAT phase of the depreciation module, so it was decided that it would be quicker
     * to simply change policies and computation code without changing the values actually persisted in the database
     * and without making changes to the operations of the sensitive client code.
     * The added advantage is that the use of basis points increases the accuracy of the depreciation rate up to
     * 6 decimal places(assuming you apply the two decimal places). This was confirmed by tests in the UAT against similar parameters on an excel sheet and straight
     * line depreciation was itself quite susceptible to the initial 2-decimal place limitation.
     * But we do need to convert that into a decimals, so here we are...
     *
     * @param depreciationRateYearlyInBasisPoints depreciation rate stated as decimal places
     * @return depreciation rate stated in 6-place decimal value
     */
    public static BigDecimal convertBasisPointsToDecimalDepreciationRate(BigDecimal depreciationRateYearlyInBasisPoints) {
        if (depreciationRateYearlyInBasisPoints == null || depreciationRateYearlyInBasisPoints.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Depreciation rate must be non-zero");
        }
        return depreciationRateYearlyInBasisPoints.divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal convertBasisPointsToDecimalMonthlyDepreciationRate(BigDecimal depreciationRateYearlyInBasisPoints) {
        if (depreciationRateYearlyInBasisPoints == null || depreciationRateYearlyInBasisPoints.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Depreciation rate must be non-zero");
        }
        return convertBasisPointsToDecimalDepreciationRate(depreciationRateYearlyInBasisPoints).divide(MONTHS_IN_YEAR, DECIMAL_SCALE, ROUNDING_MODE);
    }

    /**
     * Given the useful life of an asset as decimal, this method converts the value into useful life
     * in months
     *
     * @param depreciationRateYearlyInDecimal depreciation rate of the asset stated as a 6-place decimal
     * @return useful life of the asset given as months
     */
    public static BigDecimal calculateUsefulLifeMonths(BigDecimal depreciationRateYearlyInDecimal) {

        if (depreciationRateYearlyInDecimal.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Depreciation rate must be non-zero");
        }
        BigDecimal usefulLifeYears = BigDecimal.ONE.divide(depreciationRateYearlyInDecimal, DECIMAL_SCALE, ROUNDING_MODE);

        return usefulLifeYears.multiply(MONTHS_IN_YEAR).setScale(DECIMAL_SCALE, ROUNDING_MODE);
    }

    /**
     * This utility calculates the period for which the calculation actually imputes depreciation,
     * by comparison of the start date of the depreciation-period request and the capitalization date of the asset
     *
     * @param startDate          start date of the depreciation-period request
     * @param endDate            end date of the depreciation-period request
     * @param capitalizationDate capitalization date of the asset
     * @return number of months for which we calculate depreciation
     */
    public static long getEffectiveDepreciationPeriod(LocalDate startDate, LocalDate endDate, LocalDate capitalizationDate) {

        // Calculate the total number of months in the period
        long elapsedMonths = ChronoUnit.MONTHS.between(startDate, endDate) + 1;
        long elapsedMonthsAfterCapitalization = ChronoUnit.MONTHS.between(capitalizationDate, endDate) + 1;

        return min(elapsedMonths, elapsedMonthsAfterCapitalization);
    }

    /**
     * Given the asset's cost and the depreciation rate per year as decimal
     * this method returns the straight-line depreciation per month
     *
     * @param assetCost                       the amount capitalized for the asset in question
     * @param depreciationRateYearlyInDecimal depreciation rate of the asset per annum as decimal
     * @return repeated straight line monthly depreciation for the asset
     */
    public static BigDecimal calculateStraightLineMonthlyDepreciation(BigDecimal assetCost, BigDecimal depreciationRateYearlyInDecimal) {
        if (depreciationRateYearlyInDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Depreciation rate can not be zero or negative. We don't do that here");
        }
        return assetCost.divide(calculateUsefulLifeMonths(depreciationRateYearlyInDecimal), DECIMAL_SCALE, ROUNDING_MODE);
    }

    /**
     * Total depreciation imputed in the depreciation period in the request.
     * When the effective months for depreciation are calculated as negative, the depreciation will be returned as
     * zero.
     *
     * @param monthlyStraightLineDepreciation the depreciation amount repeated for every month in straight line depreciation
     * @param effectiveDepreciationMonths     the months in the depreciation period
     * @return Total aggregate depreciation amount in the depreciation-period from the request
     */
    public static BigDecimal calculateTotalStraightLineDepreciation(BigDecimal monthlyStraightLineDepreciation, long effectiveDepreciationMonths) {
        if (effectiveDepreciationMonths <= 0) {
            // throw new IllegalArgumentException("The effective months determined for depreciation can not be negative");
            return ZERO;
        }
        return monthlyStraightLineDepreciation.multiply(BigDecimal.valueOf(effectiveDepreciationMonths)).setScale(DECIMAL_SCALE, ROUNDING_MODE);
    }

    public static boolean isCapitalizedAfterAssumedPreviousDepreciation(LocalDate capitalizationDate, LocalDate assumedPreviousDepreciationEndDate) {
        return capitalizationDate.isAfter(assumedPreviousDepreciationEndDate);
    }

    public static BigDecimal calculateNetBookValueBeforeDepreciation(LocalDate capitalizationDate, BigDecimal assetCost, BigDecimal depreciationRateYearly, long priorMonths, LocalDate periodStartDate, LocalDate periodEndDate) {

        long monthsInDepreciationPeriod = ChronoUnit.MONTHS.between(periodStartDate, periodEndDate);
        long fiscalMonthDuration = 1; // Assuming a fiscal month duration

        // Opt out if:
        // - capitalization date is after the period end date,
        // - on/after the period start date (within the fiscal month), or
        // - the duration of the depreciation period is equal to or less than the duration of the fiscal month
        if (capitalizationDate.isAfter(periodEndDate) ||
            !capitalizationDate.isBefore(periodStartDate) ||
            monthsInDepreciationPeriod <= fiscalMonthDuration) {
            return assetCost;
        }

        return calculateTotalStraightLineDepreciation(calculateStraightLineMonthlyDepreciation(assetCost, depreciationRateYearly), priorMonths);
    }

    public static long calculateElapsedMonths(LocalDate periodStartDate, LocalDate periodEndDate) {

        return ChronoUnit.MONTHS.between(periodEndDate, periodStartDate);
    }
}
