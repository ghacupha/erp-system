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
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;

import java.math.BigDecimal;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.1
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
public interface CalculatesDepreciation {

    /**
     * Calculates depreciation amount based on the depreciation rate configured on the asset-category
     * in which the asset-registration belongs. The depreciation rate is given in whole number basis points like instead
     * f 12.5% we say 1250, and instead of 30% we say 3000. So the calculation converts the basis points to decimals before
     * calculation
     *
     * @param asset
     * @param period
     * @param assetCategory
     * @param depreciationMethod
     * @param disposalAmount amount of asset-cost disposed
     * @return
     * @throws DepreciationRateNotProvidedException
     */
    DepreciationArtefact calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod, BigDecimal costAdjustment) throws DepreciationRateNotProvidedException;
}
