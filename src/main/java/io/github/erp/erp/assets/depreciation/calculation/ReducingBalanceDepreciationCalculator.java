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
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.*;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.calculateUsefulLifeMonths;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.convertBasisPointsToDecimalMonthlyDepreciationRate;

/**
 * Calculates reducing balance depreciation for the period requested on a month-by-month basis
 */
@Service("reducingBalanceDepreciationCalculator")
public class ReducingBalanceDepreciationCalculator implements CalculatesDepreciation {

    public DepreciationArtefact calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod, BigDecimal costAdjustment) throws DepreciationRateNotProvidedException {

        // opt out, no pain no pain
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.DECLINING_BALANCE ) {

            // return BigDecimal.ZERO;
            return returnZeroAmountDepreciation(asset, ZERO, assetCategory.getDepreciationRateYearly(), period.getStartDate(), period.getEndDate());
        }

        // Calculate and return the depreciation for the specified period as before
        return calculatedDepreciation(asset, period, assetCategory, costAdjustment);
    }

    @NotNull
    private DepreciationArtefact calculatedDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, BigDecimal costAdjustment) throws DepreciationRateNotProvidedException {

        BigDecimal netBookValue = asset.getAssetCost().subtract(costAdjustment);


        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly();
        if (depreciationRate == null) {
            throw new DepreciationRateNotProvidedException("Depreciation rate is not provided", assetCategory);
        }
        depreciationRate = convertBasisPointsToDecimalMonthlyDepreciationRate(assetCategory.getDepreciationRateYearly());
        LocalDate capitalizationDate = asset.getCapitalizationDate();
        LocalDate periodStartDate = period.getStartDate();
        LocalDate periodEndDate = period.getEndDate();

        if (capitalizationDate.isAfter(periodEndDate)) {
            return returnZeroAmountDepreciation(asset, netBookValue, depreciationRate, periodStartDate, periodEndDate);
        }

        int elapsedMonthsBeforeStart = 0;

        BigDecimal depreciationBeforeStartDate = BigDecimal.ZERO.setScale(DECIMAL_SCALE, ROUNDING_MODE);
        if (capitalizationDate.isBefore(periodStartDate)) {
            elapsedMonthsBeforeStart = Math.toIntExact(ChronoUnit.MONTHS.between(capitalizationDate, periodStartDate)) + 1;
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

        // return depreciationAmount;

        return DepreciationArtefact.builder()
            .depreciationPeriodStartDate(periodStartDate)
            .depreciationPeriodEndDate(periodEndDate)
            .depreciationAmount(depreciationAmount.setScale(MONEY_SCALE, ROUNDING_MODE))
            .elapsedMonths((long) elapsedMonths)
            .priorMonths((long) elapsedMonthsBeforeStart)
            .usefulLifeYears(calculateUsefulLifeMonths(depreciationRate))
            .nbvBeforeDepreciation(netBookValue.add(depreciationAmount).setScale(MONEY_SCALE, ROUNDING_MODE))
            .nbv(netBookValue)
            .capitalizationDate(asset.getCapitalizationDate())
            .build();
    }

    private DepreciationArtefact returnZeroAmountDepreciation(AssetRegistrationDTO asset, BigDecimal netBookValue, BigDecimal depreciationRate, LocalDate periodStartDate, LocalDate periodEndDate) {
        return DepreciationArtefact.builder()
            .depreciationPeriodStartDate(periodStartDate)
            .depreciationPeriodEndDate(periodEndDate)
            .depreciationAmount(BigDecimal.ZERO)
            .elapsedMonths((long) 0)
            .priorMonths((long) 0)
            .usefulLifeYears(calculateUsefulLifeMonths(depreciationRate))
            .nbvBeforeDepreciation(BigDecimal.ZERO)
            .capitalizationDate(asset.getCapitalizationDate())
            .nbv(netBookValue)
            .build();
    }
}

