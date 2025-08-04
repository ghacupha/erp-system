package io.github.erp.service;

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

import io.github.erp.service.dto.SettlementGroupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SettlementGroup}.
 */
public interface SettlementGroupService {
    /**
     * Save a settlementGroup.
     *
     * @param settlementGroupDTO the entity to save.
     * @return the persisted entity.
     */
    SettlementGroupDTO save(SettlementGroupDTO settlementGroupDTO);

    /**
     * Updates a settlementGroup.
     *
     * @param settlementGroupDTO the entity to update.
     * @return the persisted entity.
     */
    SettlementGroupDTO update(SettlementGroupDTO settlementGroupDTO);

    /**
     * Partially updates a settlementGroup.
     *
     * @param settlementGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SettlementGroupDTO> partialUpdate(SettlementGroupDTO settlementGroupDTO);

    /**
     * Get all the settlementGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementGroupDTO> findAll(Pageable pageable);

    /**
     * Get all the settlementGroups with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementGroupDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" settlementGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SettlementGroupDTO> findOne(Long id);

    /**
     * Delete the "id" settlementGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the settlementGroup corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementGroupDTO> search(String query, Pageable pageable);
}
