package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/**
 * Thrown when the asset category is not configured with the yearly depreciation rate
 */
public class DepreciationRateNotProvidedException extends IllegalStateException {
    public DepreciationRateNotProvidedException(String errorMessage, AssetCategoryDTO assetCategory) {
        super(errorMessage + " please ensure the yearly depreciation rate for category id : " + assetCategory.getAssetCategoryName() + " is provided in decimals");
    }
}
