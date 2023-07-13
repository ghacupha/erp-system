package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.enumeration.DepreciationTypes;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Calculate the depreciation for the monthly periods in the request
 */
@Service("straightLineDepreciationCalculator")
public class StraightLineDepreciationCalculator extends AbstractDepreciationCalculator implements CalculatesDepreciation {

    @Override
    public BigDecimal calculateDepreciation(AssetRegistration asset, DepreciationPeriod period, AssetCategory assetCategory, DepreciationMethod depreciationMethod) {

        // OPT OUT
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.STRAIGHT_LINE) {

            return BigDecimal.ZERO;
        }

        BigDecimal netBookValue = asset.getAssetCost();
        BigDecimal assetCost = asset.getAssetCost();
        // ADAPT TO MONTHLY UNITS
        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly().divide(BigDecimal.valueOf(12));
        int elapsedMonths = calculateElapsedMonths(period);

        BigDecimal depreciationAmount = BigDecimal.ZERO;
        for (int month = 1; month <= elapsedMonths; month++) {
            BigDecimal monthlyDepreciation = assetCost.multiply(depreciationRate);
            depreciationAmount = depreciationAmount.add(monthlyDepreciation);
            netBookValue = netBookValue.subtract(monthlyDepreciation);
            if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                netBookValue = BigDecimal.ZERO;
            }
        }

        return depreciationAmount;
    }

}

