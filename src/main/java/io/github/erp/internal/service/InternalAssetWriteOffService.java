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
import io.github.erp.domain.AssetWriteOffInternal;
import io.github.erp.service.dto.AssetWriteOffDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.AssetWriteOff}.
 */
public interface InternalAssetWriteOffService {
    /**
     * Save a assetWriteOff.
     *
     * @param assetWriteOffDTO the entity to save.
     * @return the persisted entity.
     */
    AssetWriteOffDTO save(AssetWriteOffDTO assetWriteOffDTO);

    /**
     * Partially updates a assetWriteOff.
     *
     * @param assetWriteOffDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetWriteOffDTO> partialUpdate(AssetWriteOffDTO assetWriteOffDTO);

    /**
     * Get all the assetWriteOffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetWriteOffDTO> findAll(Pageable pageable);

    /**
     * Get all the assetWriteOffs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetWriteOffDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assetWriteOff.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetWriteOffDTO> findOne(Long id);

    /**
     * Get the "id" assetWriteOff.
     *
     * The method implement buffered update persistence of the accessor. This one enables
     * bulk updates when running depreciation and is therefore decidedly not suitable for individual
     * access of items
     *
     * @param assetWrittenOffId the id of the entity written off.
     * @return the entity.
     */
    Optional<List<AssetWriteOffInternal>> findAssetWriteOffByIdAndPeriod(DepreciationPeriodDTO depreciationPeriod, Long assetWrittenOffId);

    /**
     * Delete the "id" assetWriteOff.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the assetWriteOff corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetWriteOffDTO> search(String query, Pageable pageable);
}
