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
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WorkInProgressRegistration}.
 */
public interface InternalWorkInProgressRegistrationService {
    /**
     * Save a workInProgressRegistration.
     *
     * @param workInProgressRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressRegistrationDTO save(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);

    /**
     * Partially updates a workInProgressRegistration.
     *
     * @param workInProgressRegistrationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkInProgressRegistrationDTO> partialUpdate(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);

    /**
     * Get all the workInProgressRegistrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressRegistrationDTO> findAll(Pageable pageable);

    /**
     * Get all the workInProgressRegistrations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressRegistrationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workInProgressRegistration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkInProgressRegistrationDTO> findOne(Long id);

    /**
     * Delete the "id" workInProgressRegistration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workInProgressRegistration corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressRegistrationDTO> search(String query, Pageable pageable);

    /**
     * Calculate the next number. Typically used for cataloguing the instances of the entity
     *
     * @return the next catalogue number
     */
    Long calculateNextNumber();

    /**
     * Save a workInProgressRegistration bypassing the limitation in the es engine of 1000
     *
     * @param workInProgressRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressRegistrationDTO update(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);
}
