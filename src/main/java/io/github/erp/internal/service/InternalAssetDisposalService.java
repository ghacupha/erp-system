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

import io.github.erp.service.dto.AssetDisposalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @return the entity.
     */
    Optional<AssetDisposalDTO> findOneByDisposedAsset(Long disposedAssetId);

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
