package io.github.erp.internal.service.leases;

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

import io.github.erp.service.dto.LeaseLiabilityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseLiability}.
 */
public interface InternalLeaseLiabilityService {
    /**
     * Save a leaseLiability.
     *
     * @param leaseLiabilityDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseLiabilityDTO save(LeaseLiabilityDTO leaseLiabilityDTO);

    /**
     * Partially updates a leaseLiability.
     *
     * @param leaseLiabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaseLiabilityDTO> partialUpdate(LeaseLiabilityDTO leaseLiabilityDTO);

    /**
     * Get all the leaseLiabilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leaseLiability.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseLiabilityDTO> findOne(Long id);

    /**
     * Delete the "id" leaseLiability.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseLiability corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityDTO> search(String query, Pageable pageable);

    /**
     * Lease liability items due for lease liability amortization
     *
     * @param leaseLiabilityCompilationRequestId id of the requisition entity instance
     * @param batchJobIdentifier id of the current compilation spring batch job
     * @return List of leases for processing
     */
    Optional<List<LeaseLiabilityDTO>> getCompilationAdjacentMetadataItems(long leaseLiabilityCompilationRequestId, String batchJobIdentifier);
}
