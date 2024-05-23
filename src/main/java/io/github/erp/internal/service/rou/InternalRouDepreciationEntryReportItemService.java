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

import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.RouDepreciationEntryReportItem}.
 */
public interface InternalRouDepreciationEntryReportItemService {
    /**
     * Save a rouDepreciationEntryReportItem.
     *
     * @param rouDepreciationEntryReportItemDTO the entity to save.
     * @return the persisted entity.
     */
    RouDepreciationEntryReportItemDTO save(RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO);

    /**
     * Partially updates a rouDepreciationEntryReportItem.
     *
     * @param rouDepreciationEntryReportItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RouDepreciationEntryReportItemDTO> partialUpdate(RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO);

    /**
     * Get all the rouDepreciationEntryReportItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouDepreciationEntryReportItemDTO> findAll(Pageable pageable);

    /**
     * This is the list of entries for an individual metadata selection, given the id
     * Ideally this would be used when the client selects a metadata item on the frontend
     * and the list which will have the same parameters will shift to show only items for an
     * individual model-metadata
     *
     * @param pageable the pagination information
     * @param metadataId id of the model
     * @return list of depreciation entries for the model
     */
    Page<RouDepreciationEntryReportItemDTO> findAllByMetadataId(Pageable pageable, Long metadataId);

    /**
     * This is the list of items posted for a given lease period
     *
     * @param pageable the pagination information
     * @param leasePeriodId id of the lease period
     * @return list of depreciation entries for the model
     */
    Page<RouDepreciationEntryReportItemDTO> findAllByLeasePeriodId(Pageable pageable, Long leasePeriodId);

    /**
     * Get the "id" rouDepreciationEntryReportItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RouDepreciationEntryReportItemDTO> findOne(Long id);

    /**
     * Delete the "id" rouDepreciationEntryReportItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the rouDepreciationEntryReportItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouDepreciationEntryReportItemDTO> search(String query, Pageable pageable);
}
