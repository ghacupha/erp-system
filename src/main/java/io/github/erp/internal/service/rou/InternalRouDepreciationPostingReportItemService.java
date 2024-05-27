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

import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.RouDepreciationPostingReportItem}.
 */
public interface InternalRouDepreciationPostingReportItemService {
    /**
     * Save a rouDepreciationPostingReportItem.
     *
     * @param rouDepreciationPostingReportItemDTO the entity to save.
     * @return the persisted entity.
     */
    RouDepreciationPostingReportItemDTO save(RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO);

    /**
     * Partially updates a rouDepreciationPostingReportItem.
     *
     * @param rouDepreciationPostingReportItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RouDepreciationPostingReportItemDTO> partialUpdate(RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO);

    /**
     * Get all the rouDepreciationPostingReportItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouDepreciationPostingReportItemDTO> findAll(Pageable pageable);

    /**
     * Get all the rouDepreciationPostingReportItems for a specified lease period.
     *
     * @param pageable the pagination information.
     * @param leasePeriodId the lease period iid
     * @return the list of entities.
     */
    Page<RouDepreciationPostingReportItemDTO> findAllByLeasePeriodId(Pageable pageable, long leasePeriodId);

    /**
     * Get the "id" rouDepreciationPostingReportItem.
     *
     * @param id the id of the entity.
     * @param leasePeriodId the id of the period.
     * @return the entity.
     */
    Optional<RouDepreciationPostingReportItemDTO> findOne(Long id, Long leasePeriodId);

    /**
     * Delete the "id" rouDepreciationPostingReportItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the rouDepreciationPostingReportItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RouDepreciationPostingReportItemDTO> search(String query, Pageable pageable);
}
