package io.github.erp.erp.assets.nbv;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.NetBookValueEntryDTO;

import java.util.List;

public interface NetBookValueUpdateService {

    /**
     * Generate transient entries with other details other than calculated values. These entries
     * are not to be saved to the database
     *
     * @param assetRegistration
     * @param nbvBatchMessage
     * @param nbvCompilationJob
     * @param nbvArtefact
     * @return
     */
    NetBookValueEntryDTO netBookValueUpdate(AssetRegistration assetRegistration, NBVBatchMessage nbvBatchMessage, NbvCompilationJob nbvCompilationJob, NBVArtefact nbvArtefact);

    /**
     * Save transient entries to the database
     *
     * @param netBookValueEntries
     */
    void saveCalculatedEntries(List<NetBookValueEntryDTO> netBookValueEntries);
}
