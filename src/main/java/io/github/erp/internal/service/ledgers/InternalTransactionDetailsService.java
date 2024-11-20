package io.github.erp.internal.service.ledgers;

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

import io.github.erp.service.criteria.TransactionDetailsCriteria;
import io.github.erp.service.dto.TransactionDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.TransactionDetails}.
 */
public interface InternalTransactionDetailsService {
    /**
     * Save a transactionDetails.
     *
     * @param transactionDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionDetailsDTO save(TransactionDetailsDTO transactionDetailsDTO);

    /**
     * Partially updates a transactionDetails.
     *
     * @param transactionDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionDetailsDTO> partialUpdate(TransactionDetailsDTO transactionDetailsDTO);

    /**
     * Get all the transactionDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionDetailsDTO> findAll(Pageable pageable);

    /**
     * Get all the transactionDetails with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionDetailsDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" transactionDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" transactionDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the transactionDetails corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionDetailsDTO> search(String query, Pageable pageable);

    /**
     * Return a {@link List} of {@link TransactionDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    Page<TransactionDetailsDTO> findByCriteria(TransactionDetailsCriteria criteria, Pageable pageable);
}
