package io.github.erp.erp.assets.nbv.calculation;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.assets.nbv.NBVCalculatorService;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.erp.domain.enumeration.DepreciationTypes.DECLINING_BALANCE;
import static io.github.erp.domain.enumeration.DepreciationTypes.STRAIGHT_LINE;

/**
 * Runs calculations returning transient NBVArtefact
 */
@Transactional
@Service("nbvCalculatorServiceMain")
public class NBVCalculatorServiceMain implements NBVCalculatorService {

    private final StraightLineNBVCalculatorService straightlineNBVCalculatorService;
    private final DecliningBalanceNBVCalculatorService decliningBalanceNBVCalculatorService;

    public NBVCalculatorServiceMain(StraightLineNBVCalculatorService straightlineNBVCalculatorService, DecliningBalanceNBVCalculatorService decliningBalanceNBVCalculatorService) {
        this.straightlineNBVCalculatorService = straightlineNBVCalculatorService;
        this.decliningBalanceNBVCalculatorService = decliningBalanceNBVCalculatorService;
    }

    @Override
    public NBVArtefact calculateNetBookValue(AssetRegistration assetRegistration, NBVBatchMessage nbvBatchMessage, DepreciationPeriodDTO depreciationPeriod) {

        if (assetRegistration.getAssetCategory().getDepreciationMethod().getDepreciationType() == STRAIGHT_LINE) {

            return straightlineNBVCalculatorService.calculateNetBookValue(assetRegistration, nbvBatchMessage, depreciationPeriod);
        }

        if (assetRegistration.getAssetCategory().getDepreciationMethod().getDepreciationType() == DECLINING_BALANCE) {

            return decliningBalanceNBVCalculatorService.calculateNetBookValue(assetRegistration, nbvBatchMessage, depreciationPeriod);
        }

        return null;
    }
}
