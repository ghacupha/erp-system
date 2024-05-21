package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link io.github.erp.domain.RouDepreciationEntry}.
 */
public interface InternalRouDepreciationEntryService {
    /**
     * Save a rouDepreciationEntry.
     *
     * @param rouDepreciationEntryDTO the entity to save.
     * @return the persisted entity.
     */
    RouDepreciationEntryDTO save(RouDepreciationEntryDTO rouDepreciationEntryDTO);

    /**
     * Save a collection of rouDepreciationEntry
     *
     * @param rouDepreciationEntryDTO the entity to save.
     * @return the persisted entity.
     */
    List<RouDepreciationEntryDTO> save(List<RouDepreciationEntryDTO> rouDepreciationEntryDTO);

    /**
     * Partially updates a rouDepreciationEntry.
     *
     * @param rouDepreciationEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RouDepreciationEntryDTO> partialUpdate(RouDepreciationEntryDTO rouDepreciationEntryDTO);

    /**
     * Get all the rouDepreciationEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouDepreciationEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rouDepreciationEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RouDepreciationEntryDTO> findOne(Long id);

    /**
     * Delete the "id" rouDepreciationEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the rouDepreciationEntry corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouDepreciationEntryDTO> search(String query, Pageable pageable);

    /**
     * Calculate the outstanding amount for the current depreciation entry
     * @param entry Depreciation entry whose oustanding amount we compute
     * @return Calculated outstanding amount corresponding to the depreciation entry
     */
    BigDecimal calculateOutstandingAmount(RouDepreciationEntryDTO entry);

    /**
     * This query looks for processed items whose outstanding amount has not yet been
     * updated. It's used in the batch sequence to further update the outstanding amounts
     * @return list of entities for processing
     */
    Optional<List<RouDepreciationEntryDTO>> getOutstandingAmountItems();

    /**
     * Fetch items that contain the provided batch-job-processor. These are items that have
     * been processed in a particular way through a batch process whose identifier is the same
     * as the parameter
     *
     * @param batchJobIdentifier Identifier of the batch process
     * @return List of items from a particular batch process instance
     */
    Optional<List<RouDepreciationEntryDTO>> getProcessedItems(UUID batchJobIdentifier);

    /**
     * Typically applied in a batch process to save a collection
     * @param items Collection for persistence
     */
    void saveAll(List<? extends RouDepreciationEntryDTO> items);
}
