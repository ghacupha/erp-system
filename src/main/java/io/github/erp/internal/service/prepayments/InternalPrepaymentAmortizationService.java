package io.github.erp.internal.service.prepayments;

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

import io.github.erp.service.criteria.PrepaymentAmortizationCriteria;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentAmortization}.
 */
public interface InternalPrepaymentAmortizationService {
    /**
     * Save a prepaymentAmortization.
     *
     * @param prepaymentAmortizationDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentAmortizationDTO save(PrepaymentAmortizationDTO prepaymentAmortizationDTO);

    /**
     * Partially updates a prepaymentAmortization.
     *
     * @param prepaymentAmortizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentAmortizationDTO> partialUpdate(PrepaymentAmortizationDTO prepaymentAmortizationDTO);

    /**
     * Get all the prepaymentAmortizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAmortizationDTO> findAll(Pageable pageable);

    /**
     * Get all the prepaymentAmortizations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAmortizationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prepaymentAmortization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentAmortizationDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentAmortization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentAmortization corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAmortizationDTO> search(String query, Pageable pageable);

    /**
     * Return a {@link Page} of {@link PrepaymentAmortizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param pageable The page, which should be returned.
     * @return the matching entities.
     */
    Page<PrepaymentAmortizationDTO> findByCriteria(PrepaymentAmortizationCriteria criteria, Pageable pageable);
}