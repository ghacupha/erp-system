package io.github.erp.erp.assets.depreciation.calculation;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.enumeration.DepreciationTypes;
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.*;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.calculateUsefulLifeMonths;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.convertBasisPointsToDecimalDepreciationRate;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.convertBasisPointsToDecimalMonthlyDepreciationRate;

/**
 * Calculates reducing balance depreciation for the period requested on a month-by-month basis
 */
@Service("reducingBalanceDepreciationCalculator")
public class ReducingBalanceDepreciationCalculator implements CalculatesDepreciation {

    private static final MathContext POWER_MATH_CONTEXT = new MathContext(DECIMAL_SCALE + 2, ROUNDING_MODE);

    private final NetBookValueSnapshotProvider netBookValueSnapshotProvider;

    public ReducingBalanceDepreciationCalculator(NetBookValueSnapshotProvider netBookValueSnapshotProvider) {
        this.netBookValueSnapshotProvider = netBookValueSnapshotProvider;
    }

    public DepreciationArtefact calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod, BigDecimal costAdjustment) throws DepreciationRateNotProvidedException {

        // opt out, no pain, no pain
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.DECLINING_BALANCE) {
            return returnZeroAmountDepreciation(asset, ZERO, assetCategory.getDepreciationRateYearly(), period.getStartDate(), period.getEndDate());
        }

        // Calculate and return the depreciation for the specified period as before
        return calculatedDepreciation(asset, period, assetCategory, costAdjustment);
    }

    @NotNull
    private DepreciationArtefact calculatedDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, BigDecimal costAdjustment) throws DepreciationRateNotProvidedException {

        BigDecimal depreciationRateInBasisPoints = assetCategory.getDepreciationRateYearly();
        if (depreciationRateInBasisPoints == null) {
            throw new DepreciationRateNotProvidedException("Depreciation rate is not provided", assetCategory);
        }
        BigDecimal monthlyDepreciationRate = convertBasisPointsToDecimalMonthlyDepreciationRate(depreciationRateInBasisPoints);
        BigDecimal depreciationRateInDecimal = convertBasisPointsToDecimalDepreciationRate(depreciationRateInBasisPoints);
        LocalDate capitalizationDate = asset.getCapitalizationDate();
        LocalDate periodStartDate = period.getStartDate();
        LocalDate periodEndDate = period.getEndDate();

        if (capitalizationDate.isAfter(periodEndDate)) {
            BigDecimal netBookValue = asset.getAssetCost().subtract(costAdjustment == null ? BigDecimal.ZERO : costAdjustment);
            return returnZeroAmountDepreciation(asset, netBookValue, depreciationRateInBasisPoints, periodStartDate, periodEndDate);
        }

        BigDecimal adjustments = costAdjustment == null ? BigDecimal.ZERO : costAdjustment;
        BigDecimal baselineNetBookValue = asset.getAssetCost().subtract(adjustments).setScale(DECIMAL_SCALE, ROUNDING_MODE);
        BigDecimal nbvBeforeDepreciation = netBookValueSnapshotProvider
            .findLatestNetBookValueBeforePeriod(asset.getId(), periodStartDate)
            .map(value -> value.setScale(DECIMAL_SCALE, ROUNDING_MODE))
            .orElse(baselineNetBookValue);
        if (nbvBeforeDepreciation.compareTo(BigDecimal.ZERO) < 0) {
            nbvBeforeDepreciation = BigDecimal.ZERO.setScale(DECIMAL_SCALE, ROUNDING_MODE);
        }

        int elapsedMonths = Math.toIntExact(ChronoUnit.MONTHS.between(periodStartDate, periodEndDate)) + 1;
        long elapsedMonthsBeforeStart = capitalizationDate.isBefore(periodStartDate)
            ? ChronoUnit.MONTHS.between(capitalizationDate, periodStartDate) + 1
            : 0;

        BigDecimal depreciationAmount = BigDecimal.ZERO;
        BigDecimal nbvBeforeRounded = nbvBeforeDepreciation.setScale(MONEY_SCALE, ROUNDING_MODE);
        BigDecimal nbvAfterRounded = nbvBeforeRounded;

        if (elapsedMonths > 0 && nbvBeforeDepreciation.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal retentionRate = BigDecimal.ONE.subtract(monthlyDepreciationRate, POWER_MATH_CONTEXT);
            BigDecimal retentionFactor = retentionRate.pow(elapsedMonths, POWER_MATH_CONTEXT);
            BigDecimal depreciationFactor = BigDecimal.ONE.subtract(retentionFactor, POWER_MATH_CONTEXT);
            depreciationAmount = nbvBeforeDepreciation.multiply(depreciationFactor, POWER_MATH_CONTEXT);
            depreciationAmount = depreciationAmount.setScale(MONEY_SCALE, ROUNDING_MODE);
            if (depreciationAmount.compareTo(nbvBeforeRounded) >= 0) {
                depreciationAmount = nbvBeforeRounded;
                nbvAfterRounded = ZERO;
            } else {
                nbvAfterRounded = nbvBeforeRounded.subtract(depreciationAmount);
            }
        }

        return DepreciationArtefact.builder()
            .depreciationPeriodStartDate(periodStartDate)
            .depreciationPeriodEndDate(periodEndDate)
            .depreciationAmount(depreciationAmount.setScale(MONEY_SCALE, ROUNDING_MODE))
            .elapsedMonths((long) elapsedMonths)
            .priorMonths((long) elapsedMonthsBeforeStart)
            .usefulLifeYears(calculateUsefulLifeMonths(depreciationRateInDecimal))
            .nbvBeforeDepreciation(nbvBeforeRounded)
            .nbv(nbvAfterRounded)
            .capitalizationDate(asset.getCapitalizationDate())
            .build();
    }

    private DepreciationArtefact returnZeroAmountDepreciation(
        AssetRegistrationDTO asset,
        BigDecimal netBookValue,
        BigDecimal depreciationRateInBasisPoints,
        LocalDate periodStartDate,
        LocalDate periodEndDate
    ) {
        return DepreciationArtefact.builder()
            .depreciationPeriodStartDate(periodStartDate)
            .depreciationPeriodEndDate(periodEndDate)
            .depreciationAmount(BigDecimal.ZERO)
            .elapsedMonths((long) 0)
            .priorMonths((long) 0)
            .usefulLifeYears(calculateUsefulLifeMonths(convertBasisPointsToDecimalDepreciationRate(depreciationRateInBasisPoints)))
            .nbvBeforeDepreciation(netBookValue.setScale(MONEY_SCALE, ROUNDING_MODE))
            .capitalizationDate(asset.getCapitalizationDate())
            .nbv(netBookValue.setScale(MONEY_SCALE, ROUNDING_MODE))
            .build();
    }
}

