package io.github.erp.internal.service.leases.batch;

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
import io.github.erp.internal.service.leases.InternalLeaseLiabilityService;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import org.springframework.batch.item.ItemReader;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LeaseLiabilityCompilationItemReader implements ItemReader<LeaseLiabilityDTO> {

    private final InternalLeaseLiabilityService internalLeaseLiabilityService;

    private List<LeaseLiabilityDTO> metadataList;
    private Iterator<LeaseLiabilityDTO> metadataIterator;

    private final long leaseLiabilityCompilationRequestId;
    private final String batchJobIdentifier;

    public LeaseLiabilityCompilationItemReader(
        InternalLeaseLiabilityService internalLeaseLiabilityService,
        long leaseLiabilityCompilationRequestId,
        String batchJobIdentifier) {
        this.internalLeaseLiabilityService = internalLeaseLiabilityService;
        this.leaseLiabilityCompilationRequestId = leaseLiabilityCompilationRequestId;
        this.batchJobIdentifier = batchJobIdentifier;
    }

    @Override
    public LeaseLiabilityDTO read() {
        if (metadataList == null) {
            metadataList =
                internalLeaseLiabilityService
                    .getCompilationAdjacentMetadataItems(leaseLiabilityCompilationRequestId, batchJobIdentifier)
                    .orElse(Collections.emptyList());

            metadataIterator = metadataList.iterator();
        }
        return metadataIterator.hasNext() ? metadataIterator.next() : null;
    }
}
