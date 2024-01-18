package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.service.dto.CardChargesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CardCharges}.
 */
public interface CardChargesService {
    /**
     * Save a cardCharges.
     *
     * @param cardChargesDTO the entity to save.
     * @return the persisted entity.
     */
    CardChargesDTO save(CardChargesDTO cardChargesDTO);

    /**
     * Partially updates a cardCharges.
     *
     * @param cardChargesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CardChargesDTO> partialUpdate(CardChargesDTO cardChargesDTO);

    /**
     * Get all the cardCharges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CardChargesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cardCharges.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CardChargesDTO> findOne(Long id);

    /**
     * Delete the "id" cardCharges.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cardCharges corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CardChargesDTO> search(String query, Pageable pageable);
}
