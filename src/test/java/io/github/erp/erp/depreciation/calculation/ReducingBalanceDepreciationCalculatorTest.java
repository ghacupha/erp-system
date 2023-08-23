package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import io.github.erp.domain.enumeration.DepreciationTypes;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.*;

public class ReducingBalanceDepreciationCalculatorTest extends TestAssetDataGenerator {

    // todo fix failing @Test
    public void testReducingBalanceDepreciationCalculation() {
        // Generate random asset data
        AssetRegistrationDTO asset = generateRandomAssetRegistration();

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("0.2").setScale(DECIMAL_SCALE, ROUNDING_MODE)); // Example depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2023, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2023, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create ReducingBalanceDepreciationCalculator instance
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually
        BigDecimal assetCost = asset.getAssetCost();
        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly().divide(BigDecimal.valueOf(12), RoundingMode.HALF_EVEN);

        BigDecimal netBookValue = assetCost;
        BigDecimal expectedDepreciation = BigDecimal.ZERO;

        int elapsedMonths = Math.toIntExact(ChronoUnit.MONTHS.between(periodStartDate, periodEndDate))+1;

        for (int month = 1; month <= elapsedMonths; month++) {
            BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate).setScale(6, RoundingMode.HALF_EVEN);
            expectedDepreciation = expectedDepreciation.add(monthlyDepreciation);
            netBookValue = netBookValue.subtract(monthlyDepreciation);
            if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                netBookValue = BigDecimal.ZERO;
            }
        }

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Months in Period: " + elapsedMonths);

        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation.setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation.setScale(MONEY_SCALE, ROUNDING_MODE));
    }
}

