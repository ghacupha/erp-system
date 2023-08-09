package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static io.github.erp.erp.depreciation.calculation.DepreciationConstants.*;

@Service("straightLineDepreciationCalculator")
public class StraightLineDepreciationCalculator implements CalculatesDepreciation {

    @Override
    public BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) {

        // OPT OUT
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.STRAIGHT_LINE) {
            return BigDecimal.ZERO;
        }

        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();
        BigDecimal assetCost = asset.getAssetCost();
        // TODO LocalDate capitalizationDate = asset.getCapitalizationDate();
        LocalDate capitalizationDate = LocalDate.of(2023,1,1);

        // Calculate the total number of months in the period
        long elapsedMonths = ChronoUnit.MONTHS.between(startDate, endDate) + 1;

        // Calculate the total number of months before beginning of the period and be sure to avoid overlap
        long priorMonths = ChronoUnit.MONTHS.between(capitalizationDate, endDate) - elapsedMonths;

        BigDecimal depreciationRate = calculateDepreciationRate(assetCategory);

        BigDecimal netBookValueBeforeDepreciation = calculateNetBookValueBeforeDepreciation(assetCost, depreciationRate, priorMonths);

        BigDecimal monthlyDepreciation = calculateMonthlyDepreciation(assetCost, depreciationRate);

        BigDecimal depreciationAmount = calculateTotalDepreciation(monthlyDepreciation, elapsedMonths);

        return depreciationAmount.min(netBookValueBeforeDepreciation);
    }

    private BigDecimal calculateDepreciationRate(AssetCategoryDTO assetCategory) {
        return assetCategory.getDepreciationRateYearly()
            .divide(MONTHS_IN_YEAR, DECIMAL_SCALE, ROUNDING_MODE);
    }

    private BigDecimal calculateNetBookValueBeforeDepreciation(BigDecimal assetCost, BigDecimal depreciationRate, long priorMonths) {
        return assetCost.subtract(assetCost.multiply(depreciationRate).multiply(BigDecimal.valueOf(priorMonths)).setScale(MONEY_SCALE, ROUNDING_MODE));
    }

    private BigDecimal calculateMonthlyDepreciation(BigDecimal assetCost, BigDecimal depreciationRate) {
        return assetCost.multiply(depreciationRate).setScale(DECIMAL_SCALE, ROUNDING_MODE);
    }

    private BigDecimal calculateTotalDepreciation(BigDecimal monthlyDepreciation, long elapsedMonths) {
        return monthlyDepreciation.multiply(BigDecimal.valueOf(elapsedMonths)).setScale(MONEY_SCALE, ROUNDING_MODE);
    }
}


