package io.github.erp.internal.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
