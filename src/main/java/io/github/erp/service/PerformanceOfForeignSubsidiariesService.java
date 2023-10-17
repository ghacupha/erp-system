package io.github.erp.service;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

import io.github.erp.service.dto.PerformanceOfForeignSubsidiariesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PerformanceOfForeignSubsidiaries}.
 */
public interface PerformanceOfForeignSubsidiariesService {
    /**
     * Save a performanceOfForeignSubsidiaries.
     *
     * @param performanceOfForeignSubsidiariesDTO the entity to save.
     * @return the persisted entity.
     */
    PerformanceOfForeignSubsidiariesDTO save(PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO);

    /**
     * Partially updates a performanceOfForeignSubsidiaries.
     *
     * @param performanceOfForeignSubsidiariesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PerformanceOfForeignSubsidiariesDTO> partialUpdate(PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO);

    /**
     * Get all the performanceOfForeignSubsidiaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PerformanceOfForeignSubsidiariesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" performanceOfForeignSubsidiaries.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PerformanceOfForeignSubsidiariesDTO> findOne(Long id);

    /**
     * Delete the "id" performanceOfForeignSubsidiaries.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the performanceOfForeignSubsidiaries corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PerformanceOfForeignSubsidiariesDTO> search(String query, Pageable pageable);
}
