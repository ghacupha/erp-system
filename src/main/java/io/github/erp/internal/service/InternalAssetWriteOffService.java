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
