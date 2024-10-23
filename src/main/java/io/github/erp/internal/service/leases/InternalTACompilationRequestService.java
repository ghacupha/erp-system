package io.github.erp.internal.service.leases;

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

import io.github.erp.service.dto.TACompilationRequestDTO;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.TACompilationRequest}.
 */
public interface InternalTACompilationRequestService {
    /**
     * Save a tACompilationRequest.
     *
     * @param tACompilationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    TACompilationRequestDTO save(TACompilationRequestDTO tACompilationRequestDTO);

    /**
     * Partially updates a tACompilationRequest.
     *
     * @param tACompilationRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TACompilationRequestDTO> partialUpdate(TACompilationRequestDTO tACompilationRequestDTO);

    /**
     * Get all the tACompilationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TACompilationRequestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tACompilationRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TACompilationRequestDTO> findOne(Long id);

    /**
     * Delete the "id" tACompilationRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tACompilationRequest corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TACompilationRequestDTO> search(String query, Pageable pageable);

    /**
     * This method is used to launch actual postings of transactions into the transaction-details
     * table, in their respective debit and credit account positions
     *
     * @param compilationRequest DTO containing the run parameters
     */
    void launchTACompilationBatch(TACompilationRequestDTO compilationRequest) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;
}
