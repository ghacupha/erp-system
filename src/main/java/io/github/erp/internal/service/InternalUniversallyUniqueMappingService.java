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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * This service extends the functionality of the common service to simply select values that
 * are mapped to a particular key and are called in various parts of the programming for instance
 * to search out common values or preferred inputs. This has helped us avoid hard-coding business
 * logic into the app
 */
public interface InternalUniversallyUniqueMappingService {

    /**
     * Search for the universallyUniqueMapping corresponding to the query.
     *
     * @param universalKey the query of the search.
     *
     * @return the value mapped to that key
     */
    Optional<UniversallyUniqueMapping> getMapping(String universalKey);

    /**
     * Save a universallyUniqueMapping.
     *
     * @param universallyUniqueMappingDTO the entity to save.
     * @return the persisted entity.
     */
    UniversallyUniqueMappingDTO save(UniversallyUniqueMappingDTO universallyUniqueMappingDTO);

    /**
     * Partially updates a universallyUniqueMapping.
     *
     * @param universallyUniqueMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UniversallyUniqueMappingDTO> partialUpdate(UniversallyUniqueMappingDTO universallyUniqueMappingDTO);

    /**
     * Get all the universallyUniqueMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversallyUniqueMappingDTO> findAll(Pageable pageable);

    /**
     * Get all the universallyUniqueMappings with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversallyUniqueMappingDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" universallyUniqueMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniversallyUniqueMappingDTO> findOne(Long id);

    /**
     * Delete the "id" universallyUniqueMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the universallyUniqueMapping corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversallyUniqueMappingDTO> search(String query, Pageable pageable);
}
