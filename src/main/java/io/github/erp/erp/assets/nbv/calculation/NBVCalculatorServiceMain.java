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
