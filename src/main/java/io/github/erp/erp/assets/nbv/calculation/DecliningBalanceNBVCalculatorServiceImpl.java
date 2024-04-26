package io.github.erp.erp.assets.nbv.calculation;

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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationUtility.*;

@Transactional
@Service
public class DecliningBalanceNBVCalculatorServiceImpl implements DecliningBalanceNBVCalculatorService {

    private final NBVDecliningBalanceCalculationRepository nbvDecliningBalanceCalculationRepository;

    public DecliningBalanceNBVCalculatorServiceImpl(NBVDecliningBalanceCalculationRepository nbvDecliningBalanceCalculationRepository) {
        this.nbvDecliningBalanceCalculationRepository = nbvDecliningBalanceCalculationRepository;
    }

    @Override
    public NBVArtefact calculateNetBookValue(AssetRegistration assetRegistration, NBVBatchMessage nbvBatchMessage, DepreciationPeriodDTO depreciationPeriod) {

        long elapsedMonths = calculateElapsedMonths(depreciationPeriod.getStartDate(), depreciationPeriod.getEndDate());
        long priorPeriods = getPriorPeriodInMonths(depreciationPeriod.getEndDate(), assetRegistration.getCapitalizationDate(), elapsedMonths);

        long periodN = priorPeriods + 1;

        BigDecimal nbv = nbvDecliningBalanceCalculationRepository.netBookValueAtPeriodN(periodN, assetRegistration.getId());

        BigDecimal previousNBV = nbvDecliningBalanceCalculationRepository.netBookValueAtPeriodN(priorPeriods, assetRegistration.getId());

        return NBVArtefact.builder()
            .netBookValueAmount(nbv.max(BigDecimal.ZERO))
            .previousNetBookValueAmount(previousNBV.max(BigDecimal.ZERO))
            .elapsedMonths(elapsedMonths)
            .priorMonths(priorPeriods)
            .usefulLifeYears(calculateUsefulLifeMonths(convertBasisPointsToDecimalDepreciationRate(assetRegistration.getAssetCategory().getDepreciationRateYearly())))
            .activePeriodStartDate(depreciationPeriod.getStartDate())
            .activePeriodEndDate(depreciationPeriod.getEndDate())
            .capitalizationDate(assetRegistration.getCapitalizationDate())
            .build();
    }
}
