package io.github.erp.internal.service.rou;

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
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;

import java.util.List;

/**
 * This object compiles the depreciation entries for each ROU model metadata
 * provided in the following way:
 * 1. We calculate the initial lease-period using the commencementDate of the
 * model-metadata provided
 * 2. Fetch a list of x lease-periods starting with the initial period computed
 * above and going through sequential increments until the leaseTerm specified
 * in the model metadata is attained
 * 3. For each period compute the depreciation amount using the straight line basis
 * and save a corresponding rou-depreciation-entry
 * <p>
 * After these steps the compilation for a single rouModelMetadata is complete
 */
public interface ROUDepreciationEntryCompilationService {

    /**
     *
     * @param model RouModelMetadataDTO whose depreciation-entry instances we are calculating
     * @param batchJobIdentifier Identifier for the currently running job
     * @return list of calculated depreciation-entry instances
     */
    List<RouDepreciationEntryDTO> compileDepreciationEntries(RouModelMetadataDTO model, String batchJobIdentifier);
}
