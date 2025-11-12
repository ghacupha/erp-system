package io.github.erp.internal.service.wip;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.service.criteria.WIPTransferListItemCriteria;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import io.github.erp.service.dto.WIPTransferListReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WIPTransferListItem}.
 */
public interface InternalWIPTransferListItemService {
    /**
     * Save a wIPTransferListItem.
     *
     * @param wIPTransferListItemDTO the entity to save.
     * @return the persisted entity.
     */
    WIPTransferListItemDTO save(WIPTransferListItemDTO wIPTransferListItemDTO);

    /**
     * Partially updates a wIPTransferListItem.
     *
     * @param wIPTransferListItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WIPTransferListItemDTO> partialUpdate(WIPTransferListItemDTO wIPTransferListItemDTO);

    /**
     * Get all the wIPTransferListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPTransferListItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wIPTransferListItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WIPTransferListItemDTO> findOne(Long id);

    /**
     * Delete the "id" wIPTransferListItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the wIPTransferListItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPTransferListItemDTO> search(String query, Pageable pageable);

    /**
     * Get all the wIPTransferListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPTransferListItemDTO> findAllReportItems(Pageable pageable);

    /**
     * Return a {@link Page} of {@link WIPTransferListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    Page<WIPTransferListItemDTO> findAllSpecifiedReportItems(WIPTransferListItemCriteria criteria, Pageable page);

    long countByCriteria(WIPTransferListItemCriteria criteria);

    Page<WIPTransferListItemDTO> findByCriteria(WIPTransferListItemCriteria criteria, Pageable pageable);
}
