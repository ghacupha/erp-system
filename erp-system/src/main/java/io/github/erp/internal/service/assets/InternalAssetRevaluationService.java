package io.github.erp.internal.service.assets;

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
