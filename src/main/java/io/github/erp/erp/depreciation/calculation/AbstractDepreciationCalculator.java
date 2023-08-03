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
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

/**
 * Common methods in the depreciation sequence
 */
public abstract class AbstractDepreciationCalculator {

    protected int calculateElapsedMonths(DepreciationPeriodDTO period) {
        // Calculate the number of elapsed months between the start of the period and the current date.
        // You can use appropriate date/time libraries to perform this calculation.
        // Here's a simplified example assuming each period is a month:
        LocalDate startDate = period.getStartDate();
        LocalDate currentDate = LocalDate.now();
        return Math.toIntExact(Period.between(startDate, currentDate).toTotalMonths());
    }

    public abstract BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod);
}
