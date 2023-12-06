package io.github.erp.erp.depreciation.calculation;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DepreciationUtilityTest {

    @Test
    public void testGetPriorPeriodInMonths() {
        LocalDate endDate = LocalDate.of(2023, 8, 31);
        LocalDate capitalizationDate = LocalDate.of(2023, 7, 15);
        long elapsedMonths = 2; // meaning LocalDate startDate = LocalDate.of(2023, 6, 30);

        long priorPeriod = DepreciationUtility.getPriorPeriodInMonths(endDate, capitalizationDate, elapsedMonths);

        Assertions.assertEquals(0, priorPeriod);
    }

    @Test
    public void testConvertBasisPointsToDecimalDepreciationRate() {
        BigDecimal basisPointsRate = BigDecimal.valueOf(250); // 2.50%

        BigDecimal decimalRate = DepreciationUtility.convertBasisPointsToDecimalDepreciationRate(basisPointsRate);

        Assertions.assertEquals(new BigDecimal("0.025000"), decimalRate);
    }

    @Test
    public void testCalculateUsefulLifeMonths() {
        BigDecimal decimalRate = new BigDecimal("0.0250"); // 2.50% per annum

        BigDecimal usefulLifeMonths = DepreciationUtility.calculateUsefulLifeMonths(decimalRate);

        Assertions.assertEquals(new BigDecimal("480.000000"), usefulLifeMonths);
    }

    @Test
    public void testGetEffectiveDepreciationPeriod() {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 31);
        LocalDate capitalizationDate = LocalDate.of(2023, 7, 15);

        long effectiveDepreciationPeriod = DepreciationUtility.getEffectiveDepreciationPeriod(startDate, endDate, capitalizationDate);

        Assertions.assertEquals(1, effectiveDepreciationPeriod);
    }

    @Test
    public void testCalculateStraightLineMonthlyDepreciation() {
        BigDecimal assetCost = new BigDecimal("10000");
        BigDecimal decimalRate = new BigDecimal("0.0250"); // 2.50% per annum

        BigDecimal monthlyDepreciation = DepreciationUtility.calculateStraightLineMonthlyDepreciation(assetCost, decimalRate);

        Assertions.assertEquals(new BigDecimal("20.833333"), monthlyDepreciation);
    }

    @Test
    public void testCalculateTotalStraightLineDepreciation() {
        BigDecimal monthlyDepreciation = new BigDecimal("20.833333");
        long effectiveDepreciationMonths = 17;

        BigDecimal totalDepreciation = DepreciationUtility.calculateTotalStraightLineDepreciation(monthlyDepreciation, effectiveDepreciationMonths);

        Assertions.assertEquals(new BigDecimal("354.166661"), totalDepreciation);
    }

    @Test
    public void testGetPriorPeriodInMonths_CapitalizationDateBeforeStartDate() {
        LocalDate endDate = LocalDate.of(2023, 8, 31);
        LocalDate capitalizationDate = LocalDate.of(2022, 7, 15);
        long elapsedMonths = 2;

        long priorPeriod = DepreciationUtility.getPriorPeriodInMonths(endDate, capitalizationDate, elapsedMonths);

        Assertions.assertEquals(11, priorPeriod);
    }

    @Test
    public void testGetPriorPeriodInMonths_CapitalizationDateAfterEndDate() {
        LocalDate endDate = LocalDate.of(2023, 8, 15);
        LocalDate capitalizationDate = LocalDate.of(2023, 8, 31);
        long elapsedMonths = 2;

        long priorPeriod = DepreciationUtility.getPriorPeriodInMonths(endDate, capitalizationDate, elapsedMonths);

        Assertions.assertEquals(0, priorPeriod);
    }

    @Test
    public void testGetPriorPeriodInMonths_CapitalizationDateAfterStartDateBeforeEndDate() {
        LocalDate endDate = LocalDate.of(2023, 8, 31);
        LocalDate capitalizationDate = LocalDate.of(2023, 8, 15);
        long elapsedMonths = 2;

        long priorPeriod = DepreciationUtility.getPriorPeriodInMonths(endDate, capitalizationDate, elapsedMonths);

        Assertions.assertEquals(0, priorPeriod);
    }

    @Test
    public void testConvertBasisPointsToDecimalDepreciationRate_ZeroRate() {
        BigDecimal basisPointsRate = BigDecimal.ZERO;

        Assertions.assertThrows(IllegalStateException.class, () -> {
            DepreciationUtility.convertBasisPointsToDecimalDepreciationRate(basisPointsRate);
        });
    }

    @Test
    public void testCalculateUsefulLifeMonths_ZeroRate() {
        BigDecimal decimalRate = BigDecimal.ZERO;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepreciationUtility.calculateUsefulLifeMonths(decimalRate);
        });
    }

    @Test
    public void testGetEffectiveDepreciationPeriod_CapitalizationDateBeforeStartDate() {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 31);
        LocalDate capitalizationDate = LocalDate.of(2023, 7, 15);

        long effectiveDepreciationPeriod = DepreciationUtility.getEffectiveDepreciationPeriod(startDate, endDate, capitalizationDate);

        Assertions.assertEquals(1, effectiveDepreciationPeriod);
    }

    @Test
    public void testCalculateStraightLineMonthlyDepreciation_NegativeRate() {
        BigDecimal assetCost = new BigDecimal("10000");
        BigDecimal decimalRate = new BigDecimal("-0.0250"); // Negative 2.50% per annum

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepreciationUtility.calculateStraightLineMonthlyDepreciation(assetCost, decimalRate);
        });
    }

    @Test
    public void testCalculateTotalStraightLineDepreciation_NegativeEffectiveMonths() {
        BigDecimal monthlyDepreciation = new BigDecimal("20.833333");
        long effectiveDepreciationMonths = -5;

        Assertions.assertEquals(new BigDecimal("0.00"),
            DepreciationUtility.calculateTotalStraightLineDepreciation(monthlyDepreciation, effectiveDepreciationMonths)
        );
    }
}


