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
import io.github.erp.domain.enumeration.DepreciationTypes;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.*;
import static io.github.erp.erp.depreciation.calculation.DepreciationUtility.calculateUsefulLifeMonths;

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
        assetCategory.setDepreciationRateYearly(new BigDecimal("1000")); // Example depreciation rate as basis points

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = faker.date().past(1000, TimeUnit.DAYS)
            .toInstant().atZone(ZoneId.systemDefault())
            .toLocalDate();
        // Depreciation period is any period but within 12 months
        LocalDate periodEndDate = periodStartDate.plusMonths(faker.number().numberBetween(1, 12));
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually
        BigDecimal assetCost = asset.getAssetCost();
        int monthsInPeriod = Math.toIntExact(periodStartDate.until(periodEndDate).getMonths()) + 1;

        // Calculate the months remaining in the partial period
        int partialPeriodMonths = Math.toIntExact(asset.getCapitalizationDate().until(periodEndDate).getMonths()) + 1;

        // Adjust monthsInPeriod if the asset was acquired in a partial period
        if (asset.getCapitalizationDate().isAfter(periodStartDate)) {
            monthsInPeriod = partialPeriodMonths;
        }

        BigDecimal usefulLifeMonths = BigDecimal.ONE.divide(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE), DECIMAL_SCALE, ROUNDING_MODE).multiply(MONTHS_IN_YEAR);

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + periodStartDate);
        System.out.println("Depreciation Period End: " + periodEndDate);
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        BigDecimal expectedDepreciation = null;
        // zero depreciation expected; Capitalization is after period end
        if (asset.getCapitalizationDate().isAfter(periodEndDate)) {
            expectedDepreciation = BigDecimal.ZERO.setScale(MONEY_SCALE, ROUNDING_MODE);
        } else {
            expectedDepreciation =
                assetCost.divide(usefulLifeMonths, DECIMAL_SCALE, ROUNDING_MODE)
                    .multiply(BigDecimal.valueOf(monthsInPeriod))
                    .setScale(MONEY_SCALE, ROUNDING_MODE)
                    .max(BigDecimal.ZERO)
                    .setScale(MONEY_SCALE, ROUNDING_MODE);
        }

        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation, calculatedDepreciation);
    }

    @Test
    public void testCaseScenarioCapitalizationAfterPeriodEnd() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("35555"));
        asset.setCapitalizationDate(LocalDate.of(2023, 6, 7)); // Specific capitalization date

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("1000")); // Example depreciation rate as basis points

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2021, 8, 18); // Specific period start date
        LocalDate periodEndDate = LocalDate.of(2022, 6, 18);   // Specific period end date
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // TODO revisit the calculator to see how effective months arise as negative values
        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually
        BigDecimal assetCost = asset.getAssetCost();
        int monthsInPeriod = Math.toIntExact(periodStartDate.until(periodEndDate).getMonths()) + 1;

        BigDecimal usefulLifeMonths = BigDecimal.ONE.divide(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE), DECIMAL_SCALE, ROUNDING_MODE).multiply(MONTHS_IN_YEAR);

        BigDecimal expectedDepreciation = null;
        // zero depreciation expected; Capitalization is after period end
        if (asset.getCapitalizationDate().isAfter(periodEndDate)) {
            expectedDepreciation = BigDecimal.ZERO.setScale(MONEY_SCALE, ROUNDING_MODE);
        } else {
            expectedDepreciation =
                assetCost.divide(usefulLifeMonths, DECIMAL_SCALE, ROUNDING_MODE)
                    .multiply(BigDecimal.valueOf(monthsInPeriod))
                    .setScale(MONEY_SCALE, ROUNDING_MODE)
                    .max(BigDecimal.ZERO)
                    .setScale(MONEY_SCALE, ROUNDING_MODE);
        }


        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + periodStartDate);
        System.out.println("Depreciation Period End: " + periodEndDate);
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation, calculatedDepreciation);
    }

    @Test
    public void testCaseScenarioCapitalizationAfterPeriodStart() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("35555"));
        asset.setCapitalizationDate(LocalDate.of(2022, 4, 18)); // Specific capitalization date

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("1000")); // Example depreciation rate as basis points

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2021, 8, 18); // Specific period start date
        LocalDate periodEndDate = LocalDate.of(2022, 6, 18);   // Specific period end date
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually
        BigDecimal assetCost = asset.getAssetCost();
        int monthsInPeriod = Math.toIntExact(periodStartDate.until(periodEndDate).getMonths()) + 1;

        // Calculate the months remaining in the partial period
        int partialPeriodMonths = Math.toIntExact(asset.getCapitalizationDate().until(periodEndDate).getMonths()) + 1;

        // Adjust monthsInPeriod if the asset was acquired in a partial period
        if (asset.getCapitalizationDate().isAfter(periodStartDate)) {
            monthsInPeriod = partialPeriodMonths;
        }

        BigDecimal usefulLifeMonths = BigDecimal.ONE.divide(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE), DECIMAL_SCALE, ROUNDING_MODE).multiply(MONTHS_IN_YEAR);

        BigDecimal expectedDepreciation = null;
        // zero depreciation expected; Capitalization is after period end
        if (asset.getCapitalizationDate().isAfter(periodEndDate)) {
            expectedDepreciation = BigDecimal.ZERO.setScale(MONEY_SCALE, ROUNDING_MODE);
        } else {
            expectedDepreciation =
                assetCost.divide(usefulLifeMonths, DECIMAL_SCALE, ROUNDING_MODE)
                    .multiply(BigDecimal.valueOf(monthsInPeriod))
                    .setScale(MONEY_SCALE, ROUNDING_MODE)
                    .max(BigDecimal.ZERO)
                    .setScale(MONEY_SCALE, ROUNDING_MODE);
        }


        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + periodStartDate);
        System.out.println("Depreciation Period End: " + periodEndDate);
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation, calculatedDepreciation);
    }

    @Test
    public void testCaseScenarioRandomizedCapitalizationAfterPeriodStart() {
        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2021, 8, 18); // Specific period start date
        LocalDate periodEndDate = LocalDate.of(2022, 6, 18);   // Specific period end date


        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("35555"));
        asset.setCapitalizationDate(periodStartDate.plusMonths(1).plusDays(faker.number().numberBetween(0, periodStartDate.until(periodEndDate.minusMonths(1)).getDays()))); // randomized capitalization date capitalization date


        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("1000")); // Example depreciation rate as basis points


        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually
        BigDecimal assetCost = asset.getAssetCost();
        int monthsInPeriod = Math.toIntExact(periodStartDate.until(periodEndDate).getMonths()) + 1;

        // Calculate the months remaining in the partial period
        int partialPeriodMonths = Math.toIntExact(asset.getCapitalizationDate().until(periodEndDate).getMonths()) + 1;

        // Adjust monthsInPeriod if the asset was acquired in a partial period
        if (asset.getCapitalizationDate().isAfter(periodStartDate)) {
            monthsInPeriod = partialPeriodMonths;
        }

        BigDecimal usefulLifeMonths = BigDecimal.ONE.divide(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE), DECIMAL_SCALE, ROUNDING_MODE).multiply(MONTHS_IN_YEAR);

        BigDecimal expectedDepreciation = null;
        // zero depreciation expected; Capitalization is after period end
        if (asset.getCapitalizationDate().isAfter(periodEndDate)) {
            expectedDepreciation = BigDecimal.ZERO.setScale(MONEY_SCALE, ROUNDING_MODE);
        } else {
            expectedDepreciation =
                assetCost.divide(usefulLifeMonths, DECIMAL_SCALE, ROUNDING_MODE)
                    .multiply(BigDecimal.valueOf(monthsInPeriod))
                    .setScale(MONEY_SCALE, ROUNDING_MODE)
                    .max(BigDecimal.ZERO)
                    .setScale(MONEY_SCALE, ROUNDING_MODE);
        }


        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + periodStartDate);
        System.out.println("Depreciation Period End: " + periodEndDate);
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation, calculatedDepreciation);
    }


    @Test
    public void testZeroDepreciation() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("100000")); // High asset cost
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("0.001")); // Very low depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2022, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2022, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);
        });
    }

    @Test
    public void testNullInputs() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("50000"));
        asset.setCapitalizationDate(null); // Null capitalization date

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("500")); // Example depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2022, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2022, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);
        });
    }

    @Test
    public void testShortDepreciationPeriod() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("20000"));
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("800")); // Example depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2022, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2022, 1, 31); // Short period of 1 month
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        BigDecimal calculatedDepreciation = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);

        // Calculate expected depreciation manually for a 1-month period
        BigDecimal assetCost = asset.getAssetCost();
        int monthsInPeriod = 1;
        BigDecimal usefulLifeMonths = BigDecimal.ONE.divide(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE), DECIMAL_SCALE, ROUNDING_MODE).multiply(MONTHS_IN_YEAR);
        BigDecimal expectedDepreciation =
            assetCost.divide(usefulLifeMonths, DECIMAL_SCALE, ROUNDING_MODE)
                .multiply(BigDecimal.valueOf(monthsInPeriod))
                .setScale(MONEY_SCALE, ROUNDING_MODE)
                .max(BigDecimal.ZERO)
                .setScale(MONEY_SCALE, ROUNDING_MODE);

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + periodStartDate);
        System.out.println("Depreciation Period End: " + periodEndDate);
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedDepreciation);

        System.out.println("Calculated Depreciation: " + calculatedDepreciation);

        // Perform assertion on the calculated depreciation
        Assertions.assertEquals(expectedDepreciation, calculatedDepreciation);
    }

    @Test
    public void testNetBookValueForFiscalMonthDepreciationPeriod() {
        // Set up your asset, depreciation method, and asset category
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("20000"));
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationPeriodDTO for a fiscal month
        LocalDate fiscalMonthStart = LocalDate.of(2022, 1, 1);
        LocalDate fiscalMonthEnd = LocalDate.of(2022, 1, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(fiscalMonthStart);
        period.setEndDate(fiscalMonthEnd);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("800")); // Example depreciation rate

        // Calculate the prior months based on the fiscal month period
        long priorMonths = DepreciationUtility.getPriorPeriodInMonths(fiscalMonthEnd, asset.getCapitalizationDate(), 0);

        // Calculate the net book value using your utility function
        BigDecimal netBookValue = DepreciationUtility.calculateNetBookValueBeforeDepreciation(
            asset.getCapitalizationDate(),
            asset.getAssetCost(),
            assetCategory.getDepreciationRateYearly(),
            priorMonths,
            period.getStartDate(),
            period.getEndDate()
        );

        BigDecimal expectedNetBookValue = asset.getAssetCost();
        long monthsInPeriod = ChronoUnit.MONTHS.between(period.getStartDate(), period.getEndDate());
        BigDecimal usefulLifeMonths = calculateUsefulLifeMonths(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE));

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + period.getStartDate());
        System.out.println("Depreciation Period End: " + period.getEndDate());
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedNetBookValue);

        System.out.println("Calculated Depreciation: " + netBookValue);

        // Perform assertion on the calculated net book value
        Assertions.assertEquals(expectedNetBookValue, netBookValue);
    }

    @Test
    public void testNBVForFiscalMonthDepreciationPeriodAfterStartDate() {
        // Set up your asset, depreciation method, and asset category
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("20000"));
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 5));

        // Create DepreciationPeriodDTO for a fiscal month
        LocalDate fiscalMonthStart = LocalDate.of(2022, 1, 1);
        LocalDate fiscalMonthEnd = LocalDate.of(2022, 1, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(fiscalMonthStart);
        period.setEndDate(fiscalMonthEnd);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("800")); // Example depreciation rate

        // Calculate the prior months based on the fiscal month period
        long priorMonths = DepreciationUtility.getPriorPeriodInMonths(fiscalMonthEnd, asset.getCapitalizationDate(), 0);

        // Calculate the net book value using your utility function
        BigDecimal netBookValue = DepreciationUtility.calculateNetBookValueBeforeDepreciation(
            asset.getCapitalizationDate(),
            asset.getAssetCost(),
            assetCategory.getDepreciationRateYearly(),
            priorMonths,
            period.getStartDate(),
            period.getEndDate()
        );

        // Calculate expected net book value for the fiscal month scenario
        BigDecimal expectedNetBookValue = asset.getAssetCost();

        long monthsInPeriod = ChronoUnit.MONTHS.between(period.getStartDate(), period.getEndDate());
        BigDecimal usefulLifeMonths = calculateUsefulLifeMonths(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE));

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + period.getStartDate());
        System.out.println("Depreciation Period End: " + period.getEndDate());
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedNetBookValue);

        System.out.println("Calculated Depreciation: " + netBookValue);

        // Perform assertion on the calculated net book value
        Assertions.assertEquals(expectedNetBookValue, netBookValue);
    }

    @Test
    public void testNBVForFiscalMonthDepreciationPeriodOnStartDate() {
        // Set up your asset, depreciation method, and asset category
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("20000"));
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationPeriodDTO for a fiscal month
        LocalDate fiscalMonthStart = LocalDate.of(2022, 1, 1);
        LocalDate fiscalMonthEnd = LocalDate.of(2022, 1, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(fiscalMonthStart);
        period.setEndDate(fiscalMonthEnd);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("800")); // Example depreciation rate

        // Calculate the prior months based on the fiscal month period
        long priorMonths = DepreciationUtility.getPriorPeriodInMonths(fiscalMonthEnd, asset.getCapitalizationDate(), 0);

        // Calculate the net book value using your utility function
        BigDecimal netBookValue = DepreciationUtility.calculateNetBookValueBeforeDepreciation(
            asset.getCapitalizationDate(),
            asset.getAssetCost(),
            assetCategory.getDepreciationRateYearly(),
            priorMonths,
            period.getStartDate(),
            period.getEndDate()
        );

        // Calculate expected net book value for the fiscal month scenario
        BigDecimal expectedNetBookValue = asset.getAssetCost();

        long monthsInPeriod = ChronoUnit.MONTHS.between(period.getStartDate(), period.getEndDate());
        BigDecimal usefulLifeMonths = calculateUsefulLifeMonths(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE));

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + period.getStartDate());
        System.out.println("Depreciation Period End: " + period.getEndDate());
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedNetBookValue);

        System.out.println("Calculated Depreciation: " + netBookValue);

        // Perform assertion on the calculated net book value
        Assertions.assertEquals(expectedNetBookValue, netBookValue);
    }

    @Test
    public void testNBVForFiscalMonthDepreciationPeriodCapitalizedJustBeforeStartDate() {
        // Set up your asset, depreciation method, and asset category
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("20000"));
        asset.setCapitalizationDate(LocalDate.of(2021, 12, 28));

        // Create DepreciationPeriodDTO for a fiscal month
        LocalDate fiscalMonthStart = LocalDate.of(2022, 1, 1);
        LocalDate fiscalMonthEnd = LocalDate.of(2022, 1, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(fiscalMonthStart);
        period.setEndDate(fiscalMonthEnd);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("800")); // Example depreciation rate

        // Calculate the prior months based on the fiscal month period
        long priorMonths = DepreciationUtility.getPriorPeriodInMonths(fiscalMonthEnd, asset.getCapitalizationDate(), 0);

        // Calculate the net book value using your utility function
        BigDecimal netBookValue = DepreciationUtility.calculateNetBookValueBeforeDepreciation(
            asset.getCapitalizationDate(),
            asset.getAssetCost(),
            assetCategory.getDepreciationRateYearly(),
            priorMonths,
            period.getStartDate(),
            period.getEndDate()
        );

        // Calculate expected net book value for the fiscal month scenario
        BigDecimal expectedNetBookValue = asset.getAssetCost();

        long monthsInPeriod = ChronoUnit.MONTHS.between(period.getStartDate(), period.getEndDate());
        BigDecimal usefulLifeMonths = calculateUsefulLifeMonths(assetCategory.getDepreciationRateYearly().divide(TEN_THOUSAND, DECIMAL_SCALE, ROUNDING_MODE));

        System.out.println("Asset Cost: " + asset.getAssetCost());
        System.out.println("Asset Capitalization Date: " + asset.getCapitalizationDate());
        System.out.println("Depreciation Period Start: " + period.getStartDate());
        System.out.println("Depreciation Period End: " + period.getEndDate());
        System.out.println("Depreciation Rate: " + assetCategory.getDepreciationRateYearly());
        System.out.println("Useful life (Months): " + usefulLifeMonths.toString());
        System.out.println("Months in Period: " + monthsInPeriod);

        System.out.println("Expected Depreciation: " + expectedNetBookValue);

        System.out.println("Calculated Depreciation: " + netBookValue);

        // Perform assertion on the calculated net book value
        Assertions.assertEquals(expectedNetBookValue, netBookValue);
    }


    // todo review this failing @Test
    public void testZeroDepreciationRate() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("40000"));
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(BigDecimal.ZERO); // Zero depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2022, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2022, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        Assertions.assertThrows(DepreciationRateNotProvidedException.class, () -> {
            calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);
        });
    }

    // todo review this failing @Test
    public void testNegativeAssetCost() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("-50000")); // Negative asset cost
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("600")); // Example depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2022, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2022, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);
        });
    }

    @Test
    public void testNegativeDepreciationRate() {
        // Create specific asset data
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetCost(new BigDecimal("30000"));
        asset.setCapitalizationDate(LocalDate.of(2022, 1, 1));

        // Create DepreciationMethodDTO
        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.STRAIGHT_LINE);

        // Create AssetCategoryDTO
        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("-500")); // Negative depreciation rate

        // Create DepreciationPeriodDTO
        LocalDate periodStartDate = LocalDate.of(2022, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2022, 12, 31);
        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(periodStartDate);
        period.setEndDate(periodEndDate);

        // Create StraightLineDepreciationCalculator instance
        StraightLineDepreciationCalculator calculator = new StraightLineDepreciationCalculator();

        // Calculate depreciation using the calculator
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod);
        });
    }

}

