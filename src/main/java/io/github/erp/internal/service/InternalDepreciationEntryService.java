package io.github.erp.internal.service;

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
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.service.dto.DepreciationEntryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InternalDepreciationEntryService {
    /**
     * Save a depreciationEntry.
     *
     * @param depreciationEntryDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationEntryDTO save(DepreciationEntryDTO depreciationEntryDTO);

    /**
     * Partially updates a depreciationEntry.
     *
     * @param depreciationEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationEntryDTO> partialUpdate(DepreciationEntryDTO depreciationEntryDTO);

    /**
     * Get all the depreciationEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationEntryDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationEntry corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationEntryDTO> search(String query, Pageable pageable);

    void saveAll(List<DepreciationEntry> depreciationEntries);
}
