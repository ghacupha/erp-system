package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark IV No 3 (Ehud Series) Server ver 1.3.3
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
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates reducing balance depreciation for the period requested on a month-by-month basis
 */
@Service("reducingBalanceDepreciationCalculator")
public class ReducingBalanceDepreciationCalculator extends AbstractDepreciationCalculator implements CalculatesDepreciation {

    public BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) {

        // opt out
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.DECLINING_BALANCE ) {

            return BigDecimal.ZERO;
        }

        BigDecimal netBookValue = asset.getAssetCost();

        // ADAPT TO MONTHLY UNITS
        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly().setScale(6, RoundingMode.HALF_EVEN).divide(BigDecimal.valueOf(12), RoundingMode.HALF_EVEN).setScale(6, RoundingMode.HALF_EVEN);
        int elapsedMonths = calculateElapsedMonths(period);

        BigDecimal depreciationAmount = BigDecimal.ZERO;
        for (int month = 1; month <= elapsedMonths; month++) {
            BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate).setScale(6, RoundingMode.HALF_EVEN);
            depreciationAmount = depreciationAmount.add(monthlyDepreciation);
            netBookValue = netBookValue.subtract(monthlyDepreciation);
            if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                netBookValue = BigDecimal.ZERO;
            }
        }

        return depreciationAmount;
    }

}

