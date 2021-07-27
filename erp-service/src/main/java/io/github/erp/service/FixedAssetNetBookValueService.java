package io.github.erp.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.service.dto.FixedAssetNetBookValueDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.FixedAssetNetBookValue}.
 */
public interface FixedAssetNetBookValueService {

    /**
     * Save a fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the entity to save.
     * @return the persisted entity.
     */
    FixedAssetNetBookValueDTO save(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO);

    /**
     * Get all the fixedAssetNetBookValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetNetBookValueDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FixedAssetNetBookValueDTO> findOne(Long id);

    /**
     * Delete the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fixedAssetNetBookValue corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetNetBookValueDTO> search(String query, Pageable pageable);
}
