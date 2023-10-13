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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.*;
import static io.github.erp.erp.depreciation.calculation.DepreciationUtility.*;

@Service("straightLineDepreciationCalculator")
public class StraightLineDepreciationCalculator implements CalculatesDepreciation {

    private static final Logger log = LoggerFactory.getLogger(StraightLineDepreciationCalculator.class);

    @Override
    public BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) throws DepreciationRateNotProvidedException {

        // OPT OUT
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.STRAIGHT_LINE) {
            return BigDecimal.ZERO;
        }

        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();
        BigDecimal assetCost = asset.getAssetCost();
        LocalDate capitalizationDate = asset.getCapitalizationDate();

        long elapsedMonths = getEffectiveDepreciationPeriod(startDate, endDate, capitalizationDate);

        long priorMonths = getPriorPeriodInMonths(endDate, capitalizationDate, elapsedMonths);

        // Convert from basis points to depreciation rate
        BigDecimal depreciationRateYearly = convertBasisPointsToDecimalDepreciationRate(assetCategory.getDepreciationRateYearly());

        // OPT out for low or zero depreciation rates
//        if(BigDecimal.ZERO.setScale(DECIMAL_SCALE,ROUNDING_MODE).compareTo(depreciationRateYearly) <= 0) {
//            return BigDecimal.ZERO.setScale(MONEY_SCALE,ROUNDING_MODE);
//        }

        BigDecimal usefulLifeYears = calculateUsefulLifeMonths(depreciationRateYearly); // Calculate useful life from depreciation rate

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

        return depreciationAmount.min(netBookValueBeforeDepreciation).max(BigDecimal.ZERO).setScale(MONEY_SCALE, ROUNDING_MODE);
    }
}
