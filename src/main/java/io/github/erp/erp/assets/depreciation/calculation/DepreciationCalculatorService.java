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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Executes calculation for each individual asset
 */
@Service("depreciationCalculatorService")
public class DepreciationCalculatorService implements CalculatesDepreciation {

    private final ReducingBalanceDepreciationCalculator reducingBalanceDepreciationCalculator;
    private final StraightLineDepreciationCalculator straightLineDepreciationCalculator;

    public DepreciationCalculatorService(ReducingBalanceDepreciationCalculator reducingBalanceDepreciationCalculator, StraightLineDepreciationCalculator straightLineDepreciationCalculator) {
        this.reducingBalanceDepreciationCalculator = reducingBalanceDepreciationCalculator;
        this.straightLineDepreciationCalculator = straightLineDepreciationCalculator;
    }

    @Override
    public DepreciationArtefact calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod, BigDecimal costAdjustment) {

        if (depreciationMethod.getDepreciationType() == DepreciationTypes.STRAIGHT_LINE) {
            return straightLineDepreciationCalculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, costAdjustment);
        }
        if (depreciationMethod.getDepreciationType() == DepreciationTypes.DECLINING_BALANCE) {
            return reducingBalanceDepreciationCalculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, costAdjustment);
        }
        return DepreciationArtefact.builder()
            .depreciationAmount(BigDecimal.ZERO)
            .elapsedMonths(0l)
            .priorMonths(0l)
            .usefulLifeYears(BigDecimal.ZERO)
            .nbvBeforeDepreciation(BigDecimal.ZERO)
            .nbv(BigDecimal.ZERO)
            .depreciationPeriodStartDate(period.getStartDate())
            .depreciationPeriodEndDate(period.getEndDate())
            .capitalizationDate(asset.getCapitalizationDate())
            .build();
    }
}
