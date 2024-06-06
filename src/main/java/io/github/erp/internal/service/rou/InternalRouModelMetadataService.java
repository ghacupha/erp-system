package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link io.github.erp.domain.RouModelMetadata}.
 */
public interface InternalRouModelMetadataService {
    /**
     * Save a rouModelMetadata.
     *
     * @param rouModelMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    RouModelMetadataDTO save(RouModelMetadataDTO rouModelMetadataDTO);

    /**
     * Save a rouModelMetadata.
     *
     * @param rouModelMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    List<RouModelMetadataDTO> saveAll(List<RouModelMetadataDTO> rouModelMetadataDTO);

    /**
     * Partially updates a rouModelMetadata.
     *
     * @param rouModelMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RouModelMetadataDTO> partialUpdate(RouModelMetadataDTO rouModelMetadataDTO);

    /**
     * Get all the rouModelMetadata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouModelMetadataDTO> findAll(Pageable pageable);

    /**
     * Get all the rouModelMetadata with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouModelMetadataDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" rouModelMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RouModelMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" rouModelMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the rouModelMetadata corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouModelMetadataDTO> search(String query, Pageable pageable);

    /**
     * Get all items that are due for depreciation depending on status and
     * conditions of the depreciation process, and update the same with the
     * batchJobIdentifier from the process
     * @param rouDepreciationRequestId Id of the requisition entity
     * @return the list of entities for depreciation
     */
    Optional<List<RouModelMetadataDTO>> getDepreciationAdjacentMetadataItems(long rouDepreciationRequestId /*TODO, UUID batchJobIdentifier*/);

    /**
     * Get all items that are due for depreciation depending on status and
     * conditions of the depreciation process, and update the same with the
     * batchJobIdentifier from the process, and update the items with the batchJobIdentifier
     *
     * @param rouDepreciationRequestId Id of the requisition entity
     * @param rouDepreciationRequestId Id of the requisition entity
     * @return the list of entities for depreciation
     */
    Optional<List<RouModelMetadataDTO>> getDepreciationAdjacentMetadataItems(long rouDepreciationRequestId, String batchJobIdentifier);

    /**
     * This method will fetch the items that have been processed in a particular job
     *
     * @param batchJobIdentifier Id of the current job
     * @return the list of items processed by a job with the above identifier
     */
    Optional<List<RouModelMetadataDTO>> getProcessedItems(UUID batchJobIdentifier);

    /**
     * Typically applied in a batch to save entities within the writer component
     *
     * @param items Items for persistence
     */
    void saveAllWithSearch(List<RouModelMetadataDTO> items);
}
