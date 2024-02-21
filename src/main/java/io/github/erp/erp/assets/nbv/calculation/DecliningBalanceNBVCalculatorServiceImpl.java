package io.github.erp.erp.assets.nbv.calculation;

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
            .netBookValueAmount(nbv)
            .previousNetBookValueAmount(previousNBV)
            .elapsedMonths(elapsedMonths)
            .priorMonths(priorPeriods)
            .usefulLifeYears(calculateUsefulLifeMonths(convertBasisPointsToDecimalDepreciationRate(assetRegistration.getAssetCategory().getDepreciationRateYearly())))
            .activePeriodStartDate(depreciationPeriod.getStartDate())
            .activePeriodEndDate(depreciationPeriod.getEndDate())
            .capitalizationDate(assetRegistration.getCapitalizationDate())
            .build();
    }
}
