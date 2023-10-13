package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.DECIMAL_SCALE;
import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.MONEY_SCALE;
import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.MONTHS_IN_YEAR;
import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.ROUNDING_MODE;
import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.TEN_THOUSAND;
import static io.github.erp.erp.depreciation.calculation.DepreciationUtility.convertBasisPointsToDecimalMonthlyDepreciationRate;

/**
 * Calculates reducing balance depreciation for the period requested on a month-by-month basis
 */
@Service("reducingBalanceDepreciationCalculator")
public class ReducingBalanceDepreciationCalculator implements CalculatesDepreciation {

    public BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) throws DepreciationRateNotProvidedException {

        // opt out, no pain no pain
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.DECLINING_BALANCE ) {

            return BigDecimal.ZERO;
        }

        // Check database for accrued depreciation and net book value before periodStartDate
        BigDecimal accruedDepreciationBeforeStart = fetchAccruedDepreciationFromDatabase(asset.getId(), period.getStartDate());
        BigDecimal netBookValueBeforeStart = fetchNetBookValueFromDatabase(asset.getId(), period.getStartDate());

        // If values exist, use them
        if ((BigDecimal.ZERO.compareTo(accruedDepreciationBeforeStart) != 0) && (BigDecimal.ZERO.compareTo(netBookValueBeforeStart) != 0)) {
            return accruedDepreciationBeforeStart;
        }

        // Calculate and return the depreciation for the specified period as before
        BigDecimal calculatedDepreciation = calculatedDepreciation(asset, period, assetCategory);

        // TODO calculate accrued depreciation with net period start details
        // TODO net period start is end-date + 1 day
        BigDecimal accruedDepreciation = accruedDepreciationBeforeStart.add(calculatedDepreciation);
        // After calculating depreciation, enqueue messages for updating net book value and accrued depreciation
        // TODO enqueueNetBookValueUpdate(asset.getId(), period.getStartDate(), netBookValueBeforeStart);
        // TODO enqueueAccruedDepreciationUpdate(asset.getId(), period.getStartDate(), accruedDepreciation);

        return calculatedDepreciation.setScale(MONEY_SCALE, ROUNDING_MODE);

    }

    private void enqueueAccruedDepreciationUpdate(Long id, LocalDate startDate, BigDecimal calculatedDepreciation) {
        // TODO See about that message queue
    }

    private void enqueueNetBookValueUpdate(Long id, LocalDate startDate, BigDecimal netBookValueBeforeStart) {
        // TODO See about that message queue
    }

    @NotNull
    private BigDecimal calculatedDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory) throws DepreciationRateNotProvidedException {

        BigDecimal netBookValue = asset.getAssetCost();
        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly();
        if (depreciationRate == null) {
            throw new DepreciationRateNotProvidedException("Depreciation rate is not provided", assetCategory);
        }
        depreciationRate = convertBasisPointsToDecimalMonthlyDepreciationRate(assetCategory.getDepreciationRateYearly());
        LocalDate capitalizationDate = asset.getCapitalizationDate();
        LocalDate periodStartDate = period.getStartDate();
        LocalDate periodEndDate = period.getEndDate();

        if (capitalizationDate.isAfter(periodEndDate)) {
            return BigDecimal.ZERO; // No depreciation before capitalization
        }

        BigDecimal depreciationBeforeStartDate = BigDecimal.ZERO.setScale(DECIMAL_SCALE, ROUNDING_MODE);
        if (capitalizationDate.isBefore(periodStartDate)) {
            int elapsedMonthsBeforeStart = Math.toIntExact(ChronoUnit.MONTHS.between(capitalizationDate, periodStartDate)) + 1;
            for (int month = 1; month <= elapsedMonthsBeforeStart; month++) {
                BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate).setScale(DECIMAL_SCALE, ROUNDING_MODE);
                depreciationBeforeStartDate = depreciationBeforeStartDate.add(monthlyDepreciation);
                netBookValue = netBookValue.subtract(monthlyDepreciation);
                if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                    netBookValue = BigDecimal.ZERO;
                }
            }
        }


        int elapsedMonths = Math.toIntExact(ChronoUnit.MONTHS.between(periodStartDate, periodEndDate)) + 1;

        BigDecimal depreciationAmount = BigDecimal.ZERO;
        for (int month = 1; month <= elapsedMonths; month++) {
            BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate).setScale(DECIMAL_SCALE, ROUNDING_MODE);
            depreciationAmount = depreciationAmount.add(monthlyDepreciation);
            netBookValue = netBookValue.subtract(monthlyDepreciation);
            if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                netBookValue = BigDecimal.ZERO;
            }
        }

        return depreciationAmount;
    }

    private BigDecimal fetchAccruedDepreciationFromDatabase(Long assetId, LocalDate startDate) {
        // Query database to fetch accrued depreciation before startDate for the given assetId
        // Return null if not found, or the value if found

        return BigDecimal.ZERO;
    }

    private BigDecimal fetchNetBookValueFromDatabase(Long assetId, LocalDate startDate) {
        // Query database to fetch net book value before startDate for the given assetId
        // Return null if not found, or the value if found

        return BigDecimal.ZERO;
    }
}

