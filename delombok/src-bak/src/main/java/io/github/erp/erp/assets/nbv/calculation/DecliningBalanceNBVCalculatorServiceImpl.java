package io.github.erp.erp.assets.nbv.calculation;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
