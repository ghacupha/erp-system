package io.github.erp.erp.assets.depreciation.calculation;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.*;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.calculateNetBookValueBeforeDepreciation;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.calculateStraightLineMonthlyDepreciation;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.calculateTotalStraightLineDepreciation;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.calculateUsefulLifeMonths;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.convertBasisPointsToDecimalDepreciationRate;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.getEffectiveDepreciationPeriod;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.getPriorPeriodInMonths;

@Service("straightLineDepreciationCalculator")
public class StraightLineDepreciationCalculator implements CalculatesDepreciation {

    private static final Logger log = LoggerFactory.getLogger(StraightLineDepreciationCalculator.class);

    @Override
    public DepreciationArtefact calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod, BigDecimal costAdjustment) throws DepreciationRateNotProvidedException {

        // OPT OUT
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.STRAIGHT_LINE) {
            // return BigDecimal.ZERO;
            return zeroAmountDepreciation(asset, period, assetCategory, getEffectiveDepreciationPeriod(period.getStartDate(), period.getEndDate(), asset.getCapitalizationDate()), getPriorPeriodInMonths(period.getEndDate(), asset.getCapitalizationDate(), getEffectiveDepreciationPeriod(period.getStartDate(), period.getEndDate(), asset.getCapitalizationDate())));
        }

        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();
        BigDecimal assetCost = asset.getAssetCost().subtract(costAdjustment);
        LocalDate capitalizationDate = asset.getCapitalizationDate();

        long elapsedMonths = getEffectiveDepreciationPeriod(startDate, endDate, capitalizationDate);

        long priorMonths = getPriorPeriodInMonths(endDate, capitalizationDate, elapsedMonths);

        // Convert from basis points to depreciation rate
        BigDecimal depreciationRateYearly = convertBasisPointsToDecimalDepreciationRate(assetCategory.getDepreciationRateYearly());

        BigDecimal usefulLifeYears = calculateUsefulLifeMonths(depreciationRateYearly); // Calculate useful life from depreciation rate


        // TODO OPT OUT if prior months >= useful life
        if (priorMonths >= usefulLifeYears.longValue()) {
            return zeroAmountDepreciation(asset, period, assetCategory, elapsedMonths, priorMonths);
        }


        BigDecimal netBookValueBeforeDepreciation = calculateNetBookValueBeforeDepreciation(capitalizationDate, assetCost, depreciationRateYearly, priorMonths,startDate, endDate);

        BigDecimal monthlyDepreciation = calculateStraightLineMonthlyDepreciation(assetCost, depreciationRateYearly); // Calculate monthly depreciation using useful life

        BigDecimal depreciationAmount = calculateTotalStraightLineDepreciation(monthlyDepreciation, elapsedMonths);

        log.debug("Capitalization date: {}", capitalizationDate);
        log.debug("Depreciation period start: {}", startDate);
        log.debug("Depreciation period end: {}", endDate);
        log.debug("Useful life (years): {}", usefulLifeYears.toPlainString());
        log.debug("NBV before depreciation: {}", netBookValueBeforeDepreciation.toPlainString());
        log.debug("Monthly depreciation: {}", monthlyDepreciation.toPlainString());
        log.debug("Depreciation amount: {}", depreciationAmount.toPlainString());

        return DepreciationArtefact.builder()
            .depreciationPeriodStartDate(startDate)
            .depreciationPeriodEndDate(endDate)
            .depreciationAmount(depreciationAmount.min(netBookValueBeforeDepreciation).max(BigDecimal.ZERO).setScale(MONEY_SCALE, ROUNDING_MODE))
            .elapsedMonths(elapsedMonths)
            .priorMonths(priorMonths)
            .usefulLifeYears(usefulLifeYears)
            .nbvBeforeDepreciation(netBookValueBeforeDepreciation)
            .nbv(netBookValueBeforeDepreciation.subtract(depreciationAmount.min(netBookValueBeforeDepreciation).max(BigDecimal.ZERO).setScale(MONEY_SCALE, ROUNDING_MODE)))
            .capitalizationDate(asset.getCapitalizationDate())
            .build();
    }

    private DepreciationArtefact zeroAmountDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, long elapsedMonths, long priorMonths) {
        return DepreciationArtefact.builder()
            .depreciationPeriodStartDate(period.getStartDate())
            .depreciationPeriodEndDate(period.getEndDate())
            .depreciationAmount(BigDecimal.ZERO)
            .elapsedMonths(elapsedMonths)
            .priorMonths(priorMonths)
            .usefulLifeYears(calculateUsefulLifeMonths(assetCategory.getDepreciationRateYearly()))
            .nbvBeforeDepreciation(BigDecimal.ZERO)
            .capitalizationDate(asset.getCapitalizationDate())
            .nbv(ZERO)
            .build();
    }
}
