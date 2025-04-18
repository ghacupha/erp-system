package io.github.erp.internal.service.wip;

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

import io.github.erp.domain.WIPListItem;
import io.github.erp.service.criteria.WIPListItemCriteria;
import io.github.erp.service.dto.WIPListItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WIPListItem}.
 */
public interface InternalWIPListItemService {
    /**
     * Save a wIPListItem.
     *
     * @param wIPListItemDTO the entity to save.
     * @return the persisted entity.
     */
    WIPListItemDTO save(WIPListItemDTO wIPListItemDTO);

    /**
     * Partially updates a wIPListItem.
     *
     * @param wIPListItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WIPListItemDTO> partialUpdate(WIPListItemDTO wIPListItemDTO);

    /**
     * Get all the wIPListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPListItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wIPListItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WIPListItemDTO> findOne(Long id);

    /**
     * Delete the "id" wIPListItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the wIPListItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPListItemDTO> search(String query, Pageable pageable);

    Page<WIPListItemDTO> findAllReportItemsByReportDate(Pageable pageable);

    /**
     * Return a {@link Page} of {@link WIPListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    Page<WIPListItemDTO> findAll(WIPListItemCriteria criteria, Pageable page);

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    long countByCriteria(WIPListItemCriteria criteria);

    /**
     * Return a {@link Page} of {@link WIPListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    Page<WIPListItemDTO> findByCriteria(WIPListItemCriteria criteria, Pageable page);
}
