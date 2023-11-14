package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.MONEY_SCALE;
import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.ROUNDING_MODE;

public class ReducingBalanceDepreciationCalculatorTest extends TestAssetDataGenerator {

    @Test
    public void testReducingBalanceDepreciationCalculation() {
        // Generate random asset data
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setAssetCost(new BigDecimal("58000").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2023,6,6));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("3000"));

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2023, 6, 21);
        LocalDate periodEndDate = LocalDate.of(2023, 7, 20);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create ReducingBalanceDepreciationCalculator instance
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(new BigDecimal("1413.75").setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation);
    }

    @Test
    public void testReducingBalanceDepreciationCalculationManyPeriodsPrior() {
        // Generate random asset data
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setAssetCost(new BigDecimal("747446").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2022,12,29));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("3000"));

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2023, 6, 21);
        LocalDate periodEndDate = LocalDate.of(2023, 7, 20);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create ReducingBalanceDepreciationCalculator instance
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(new BigDecimal("16052.68").setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation);
    }

    @Test
    public void testReducingBalanceDepreciationCalculationManyMorePeriodsPrior() {
        // Generate random asset data
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setAssetCost(new BigDecimal("89458.63").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2016,3,24));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("1250"));

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2023, 6, 21);
        LocalDate periodEndDate = LocalDate.of(2023, 7, 20);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create ReducingBalanceDepreciationCalculator instance
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(new BigDecimal("374.72").setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation);
    }

    @Test
    public void testReducingBalanceDepreciationAfterDepreciationPeriod() {
        // Generate random asset data
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setAssetCost(new BigDecimal("89458.63").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2024,3,24));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("1250"));

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2023, 6, 21);
        LocalDate periodEndDate = LocalDate.of(2023, 7, 20);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create ReducingBalanceDepreciationCalculator instance
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(new BigDecimal("0.00").setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation);
    }
}

