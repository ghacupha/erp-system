package io.github.erp.erp.assets.nbv;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.DepreciationPeriodDTO;

public interface NBVCalculatorService {

    NBVArtefact calculateNetBookValue(AssetRegistration assetRegistration, NBVBatchMessage nbvBatchMessage, DepreciationPeriodDTO depreciationPeriod);
}
