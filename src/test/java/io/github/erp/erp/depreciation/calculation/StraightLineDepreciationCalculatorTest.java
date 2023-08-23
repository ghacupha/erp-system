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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.*;

public class StraightLineDepreciationCalculatorTest extends TestAssetDataGenerator {

    @Test
    public void testStraightLineDepreciationCalculation() {
        // Generate random asset data
        AssetRegistrationDTO asset = generateRandomAssetRegistration();

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("0.1").setScale(DECIMAL_SCALE, ROUNDING_MODE)); // Example depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2023, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2023, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually
        BigDecimal assetCost = asset.getAssetCost();
        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly().divide(BigDecimal.valueOf(12), ROUNDING_MODE);
        // int monthsInPeriod = periodStartDate.getMonth().getValue() - periodEndDate.getMonth().getValue() + 12; // Assuming the period spans one year

        int monthsInPeriod = Math.toIntExact(ChronoUnit.MONTHS.between(periodStartDate, periodEndDate) + 1);

        // BigDecimal expectedDepreciation = assetCost.multiply(depreciationRate).multiply(BigDecimal.valueOf(monthsInPeriod));

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Months in Period: " + monthsInPeriod);

        BigDecimal expectedDepreciation = assetCost.multiply(depreciationRate).multiply(BigDecimal.valueOf(monthsInPeriod)).setScale(MONEY_SCALE, ROUNDING_MODE);
        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);


        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation.setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation.setScale(MONEY_SCALE, ROUNDING_MODE));
    }
}

