package io.github.erp.erp.assets.nbv;

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
