package io.github.erp.service;

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
import io.github.erp.service.dto.TaxReferenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.TaxReference}.
 */
public interface TaxReferenceService {
    /**
     * Save a taxReference.
     *
     * @param taxReferenceDTO the entity to save.
     * @return the persisted entity.
     */
    TaxReferenceDTO save(TaxReferenceDTO taxReferenceDTO);

    /**
     * Partially updates a taxReference.
     *
     * @param taxReferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaxReferenceDTO> partialUpdate(TaxReferenceDTO taxReferenceDTO);

    /**
     * Get all the taxReferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxReferenceDTO> findAll(Pageable pageable);

    /**
     * Get all the taxReferences with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxReferenceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" taxReference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxReferenceDTO> findOne(Long id);

    /**
     * Delete the "id" taxReference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the taxReference corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxReferenceDTO> search(String query, Pageable pageable);
}
