package io.github.erp.internal.service.assets;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.service.criteria.AssetGeneralAdjustmentCriteria;
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

    /**
     * Return a {@link Page} of {@link AssetGeneralAdjustmentDTO} which matches the criteria from the database.
     * This implementation includes an extra step in which the system refreshes the AssetRegistration cache for related assets
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param pageable The page, which should be returned.
     * @return the matching entities.
     */
    Page<AssetGeneralAdjustmentDTO> findByCriteria(AssetGeneralAdjustmentCriteria criteria, Pageable pageable);
}
