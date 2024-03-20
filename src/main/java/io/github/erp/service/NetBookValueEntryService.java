package io.github.erp.service;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.service.dto.NetBookValueEntryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.NetBookValueEntry}.
 */
public interface NetBookValueEntryService {
    /**
     * Save a netBookValueEntry.
     *
     * @param netBookValueEntryDTO the entity to save.
     * @return the persisted entity.
     */
    NetBookValueEntryDTO save(NetBookValueEntryDTO netBookValueEntryDTO);

    /**
     * Partially updates a netBookValueEntry.
     *
     * @param netBookValueEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NetBookValueEntryDTO> partialUpdate(NetBookValueEntryDTO netBookValueEntryDTO);

    /**
     * Get all the netBookValueEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NetBookValueEntryDTO> findAll(Pageable pageable);

    /**
     * Get all the netBookValueEntries with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NetBookValueEntryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" netBookValueEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NetBookValueEntryDTO> findOne(Long id);

    /**
     * Delete the "id" netBookValueEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the netBookValueEntry corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NetBookValueEntryDTO> search(String query, Pageable pageable);
}
