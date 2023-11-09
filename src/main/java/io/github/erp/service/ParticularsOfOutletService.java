package io.github.erp.service;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.service.dto.ParticularsOfOutletDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ParticularsOfOutlet}.
 */
public interface ParticularsOfOutletService {
    /**
     * Save a particularsOfOutlet.
     *
     * @param particularsOfOutletDTO the entity to save.
     * @return the persisted entity.
     */
    ParticularsOfOutletDTO save(ParticularsOfOutletDTO particularsOfOutletDTO);

    /**
     * Partially updates a particularsOfOutlet.
     *
     * @param particularsOfOutletDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ParticularsOfOutletDTO> partialUpdate(ParticularsOfOutletDTO particularsOfOutletDTO);

    /**
     * Get all the particularsOfOutlets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticularsOfOutletDTO> findAll(Pageable pageable);

    /**
     * Get the "id" particularsOfOutlet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticularsOfOutletDTO> findOne(Long id);

    /**
     * Delete the "id" particularsOfOutlet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the particularsOfOutlet corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticularsOfOutletDTO> search(String query, Pageable pageable);
}
