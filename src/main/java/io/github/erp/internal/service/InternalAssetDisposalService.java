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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.AssetDisposal}.
 */
public interface InternalAssetDisposalService {
    /**
     * Save a assetDisposal.
     *
     * @param assetDisposalDTO the entity to save.
     * @return the persisted entity.
     */
    AssetDisposalDTO save(AssetDisposalDTO assetDisposalDTO);

    /**
     * Partially updates a assetDisposal.
     *
     * @param assetDisposalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetDisposalDTO> partialUpdate(AssetDisposalDTO assetDisposalDTO);

    /**
     * Get all the assetDisposals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetDisposalDTO> findAll(Pageable pageable);

    /**
     * Get all the assetDisposals with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetDisposalDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assetDisposal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetDisposalDTO> findOne(Long id);

    /**
     * Get the "id" assetDisposal.
     *
     * @param disposedAssetId the id of the asset probably disposed.
     * @return the entities
     */
    Optional<List<AssetDisposalDTO>> findDisposedItems(Long disposedAssetId, LocalDate depreciationPeriodStartDate);

    /**
     * Delete the "id" assetDisposal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the assetDisposal corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetDisposalDTO> search(String query, Pageable pageable);
}
