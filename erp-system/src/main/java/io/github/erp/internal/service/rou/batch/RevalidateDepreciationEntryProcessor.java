package io.github.erp.internal.service.rou.batch;

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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;

import java.util.UUID;

public class RevalidateDepreciationEntryProcessor implements ItemProcessor<RouDepreciationEntryDTO, RouDepreciationEntryDTO> {


    private final String batchJobIdentifier;

    public RevalidateDepreciationEntryProcessor(String batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is {@code null}, it is assumed that processing of the item
     * should not continue.
     * <p>
     * A {@code null} item will never reach this method because the only possible sources are:
     * <ul>
     *     <li>an {@link ItemReader } (which indicates no more items)</li>
     *     <li>a previous {@link ItemProcessor} in a composite processor (which indicates a filtered item)</li>
     * </ul>
     *
     * @param item to be processed, never {@code null}.
     * @return potentially modified or new item for continued processing, {@code null} if processing of the
     * provided item should not continue.
     * @throws Exception thrown if exception occurs during processing.
     */
    @Override
    public RouDepreciationEntryDTO process(RouDepreciationEntryDTO item) throws Exception {
        // Reverse invalidated and batch-job-identifier
        item.setInvalidated(false);
        item.setActivated(true);
        item.setBatchJobIdentifier(UUID.fromString(batchJobIdentifier));
        return item;
    }
}
