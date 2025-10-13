package io.github.erp.erp.assets.nbv.calculation;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
