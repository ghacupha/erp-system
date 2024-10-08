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
import io.github.erp.service.dto.LeasePaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeasePayment}.
 */
public interface InternalLeasePaymentService {
    /**
     * Save a leasePayment.
     *
     * @param leasePaymentDTO the entity to save.
     * @return the persisted entity.
     */
    LeasePaymentDTO save(LeasePaymentDTO leasePaymentDTO);

    /**
     * Save a leasePayment in the db and exclude index update step
     *
     * @param leasePaymentDTO the entity to save.
     * @return the persisted entity.
     */
    LeasePaymentDTO update(LeasePaymentDTO leasePaymentDTO);

    /**
     * Partially updates a leasePayment.
     *
     * @param leasePaymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeasePaymentDTO> partialUpdate(LeasePaymentDTO leasePaymentDTO);

    /**
     * Get all the leasePayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeasePaymentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leasePayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeasePaymentDTO> findOne(Long id);

    /**
     * Delete the "id" leasePayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leasePayment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeasePaymentDTO> search(String query, Pageable pageable);

    /**
     *
     * @param leaseContractId id of the adjacent lease-contract
     * @return List of lease-payments related to specified lease-contract
     */
    Optional<List<LeasePaymentDTO>> findPaymentsByContractId(Long leaseContractId);
}
