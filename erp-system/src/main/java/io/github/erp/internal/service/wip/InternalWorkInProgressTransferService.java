package io.github.erp.internal.service.wip;

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
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WorkInProgressTransfer}.
 */
public interface InternalWorkInProgressTransferService {
    /**
     * Save a workInProgressTransfer.
     * This method retrieves the original-transfer from the wip-registration repository so the
     * original settlement is not necessary
     *
     * @param workInProgressTransferDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressTransferDTO save(WorkInProgressTransferDTO workInProgressTransferDTO);

    /**
     * Partially updates a workInProgressTransfer.
     *
     * @param workInProgressTransferDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkInProgressTransferDTO> partialUpdate(WorkInProgressTransferDTO workInProgressTransferDTO);

    /**
     * Get all the workInProgressTransfers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressTransferDTO> findAll(Pageable pageable);

    /**
     * Get all the workInProgressTransfers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressTransferDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workInProgressTransfer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkInProgressTransferDTO> findOne(Long id);

    /**
     * Delete the "id" workInProgressTransfer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workInProgressTransfer corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressTransferDTO> search(String query, Pageable pageable);

    /**
     * Save a workInProgressTransfer without the ES updates.
     *
     * This method retrieves the original-transfer from the wip-registration repository so the
     * original settlement is not necessary
     *
     * @param workInProgressTransferDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressTransferDTO update(WorkInProgressTransferDTO workInProgressTransferDTO);
}
