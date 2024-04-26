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
import io.github.erp.service.dto.AssetDisposalDTO;
import io.github.erp.service.dto.AssetRevaluationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.AssetRevaluation}.
 */
public interface InternalAssetRevaluationService {
    /**
     * Save a assetRevaluation.
     *
     * @param assetRevaluationDTO the entity to save.
     * @return the persisted entity.
     */
    AssetRevaluationDTO save(AssetRevaluationDTO assetRevaluationDTO);

    /**
     * Partially updates a assetRevaluation.
     *
     * @param assetRevaluationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetRevaluationDTO> partialUpdate(AssetRevaluationDTO assetRevaluationDTO);

    /**
     * Get all the assetRevaluations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetRevaluationDTO> findAll(Pageable pageable);

    /**
     * Get all the assetRevaluations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetRevaluationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assetRevaluation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetRevaluationDTO> findOne(Long id);

    /**
     * Delete the "id" assetRevaluation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the assetRevaluation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetRevaluationDTO> search(String query, Pageable pageable);

    /**
     * Get the list of assetRevaluations.
     *
     * @param revaluedAssetId the id of the asset probably disposed.
     * @return the entities
     */
    Optional<List<AssetRevaluationDTO>> findRevaluedItems(Long revaluedAssetId, LocalDate depreciationPeriodStartDate);
}
