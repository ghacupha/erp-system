package io.github.erp.internal.service.assets;

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

import io.github.erp.service.dto.WorkInProgressOutstandingReportRequisitionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WorkInProgressOutstandingReportRequisition}.
 */
public interface InternalWorkInProgressOutstandingReportRequisitionService {
    /**
     * Save a workInProgressOutstandingReportRequisition.
     *
     * @param workInProgressOutstandingReportRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressOutstandingReportRequisitionDTO save(
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    );

    /**
     * Partially updates a workInProgressOutstandingReportRequisition.
     *
     * @param workInProgressOutstandingReportRequisitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkInProgressOutstandingReportRequisitionDTO> partialUpdate(
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    );

    /**
     * Get all the workInProgressOutstandingReportRequisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressOutstandingReportRequisitionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workInProgressOutstandingReportRequisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkInProgressOutstandingReportRequisitionDTO> findOne(Long id);

    /**
     * Delete the "id" workInProgressOutstandingReportRequisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workInProgressOutstandingReportRequisition corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressOutstandingReportRequisitionDTO> search(String query, Pageable pageable);
}
