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
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
import io.github.erp.service.dto.AssetRevaluationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.AssetGeneralAdjustment}.
 */
public interface InternalAssetGeneralAdjustmentService {
    /**
     * Save a assetGeneralAdjustment.
     *
     * @param assetGeneralAdjustmentDTO the entity to save.
     * @return the persisted entity.
     */
    AssetGeneralAdjustmentDTO save(AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO);

    /**
     * Partially updates a assetGeneralAdjustment.
     *
     * @param assetGeneralAdjustmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetGeneralAdjustmentDTO> partialUpdate(AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO);

    /**
     * Get all the assetGeneralAdjustments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetGeneralAdjustmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetGeneralAdjustment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetGeneralAdjustmentDTO> findOne(Long id);

    /**
     * Delete the "id" assetGeneralAdjustment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the assetGeneralAdjustment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetGeneralAdjustmentDTO> search(String query, Pageable pageable);

    /**
     * Get the list of assetRevaluations.
     *
     * @param adjustedAssetId the id of the asset probably disposed.
     * @return the entities
     */
    Optional<List<AssetGeneralAdjustmentDTO>> findAdjustmentItems(Long adjustedAssetId, LocalDate depreciationPeriodStartDate);
}
