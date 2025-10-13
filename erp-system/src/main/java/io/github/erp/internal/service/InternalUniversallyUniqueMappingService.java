package io.github.erp.internal.service;

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
