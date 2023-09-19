package io.github.erp.service;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.service.dto.OutletTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.OutletType}.
 */
public interface OutletTypeService {
    /**
     * Save a outletType.
     *
     * @param outletTypeDTO the entity to save.
     * @return the persisted entity.
     */
    OutletTypeDTO save(OutletTypeDTO outletTypeDTO);

    /**
     * Partially updates a outletType.
     *
     * @param outletTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OutletTypeDTO> partialUpdate(OutletTypeDTO outletTypeDTO);

    /**
     * Get all the outletTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutletTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the outletTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutletTypeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" outletType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OutletTypeDTO> findOne(Long id);

    /**
     * Delete the "id" outletType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the outletType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutletTypeDTO> search(String query, Pageable pageable);
}
