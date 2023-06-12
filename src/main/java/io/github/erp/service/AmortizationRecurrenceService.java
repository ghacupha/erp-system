package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.service.dto.AmortizationRecurrenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.AmortizationRecurrence}.
 */
public interface AmortizationRecurrenceService {
    /**
     * Save a amortizationRecurrence.
     *
     * @param amortizationRecurrenceDTO the entity to save.
     * @return the persisted entity.
     */
    AmortizationRecurrenceDTO save(AmortizationRecurrenceDTO amortizationRecurrenceDTO);

    /**
     * Partially updates a amortizationRecurrence.
     *
     * @param amortizationRecurrenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AmortizationRecurrenceDTO> partialUpdate(AmortizationRecurrenceDTO amortizationRecurrenceDTO);

    /**
     * Get all the amortizationRecurrences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmortizationRecurrenceDTO> findAll(Pageable pageable);

    /**
     * Get all the amortizationRecurrences with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmortizationRecurrenceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" amortizationRecurrence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AmortizationRecurrenceDTO> findOne(Long id);

    /**
     * Delete the "id" amortizationRecurrence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the amortizationRecurrence corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmortizationRecurrenceDTO> search(String query, Pageable pageable);
}
