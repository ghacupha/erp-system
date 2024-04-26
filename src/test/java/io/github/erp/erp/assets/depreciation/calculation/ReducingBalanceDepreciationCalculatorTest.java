package io.github.erp.erp.assets.depreciation.calculation;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.*;

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
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO).getDepreciationAmount();

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
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO).getDepreciationAmount();

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
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO).getDepreciationAmount();

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(new BigDecimal("374.72").setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation);
    }

    // Update 2024.01.23 @Test case seen to have caused over depreciation so now we are checking
    // if the prior period is more than the useful life first
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
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO).getDepreciationAmount();

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(new BigDecimal("0.00").setScale(MONEY_SCALE, ROUNDING_MODE), calculatedDepreciation);
    }
}

