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
import io.github.erp.domain.AssetRevaluation;
import io.github.erp.internal.repository.InternalAssetRevaluationRepository;
import io.github.erp.repository.search.AssetRevaluationSearchRepository;
import io.github.erp.service.dto.AssetRevaluationDTO;
import io.github.erp.service.mapper.AssetRevaluationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AssetRevaluation}.
 */
@Service
@Transactional
public class InternalAssetRevaluationServiceImpl implements InternalAssetRevaluationService {

    private final Logger log = LoggerFactory.getLogger(InternalAssetRevaluationServiceImpl.class);

    private final InternalAssetRevaluationRepository assetRevaluationRepository;

    private final AssetRevaluationMapper assetRevaluationMapper;

    private final AssetRevaluationSearchRepository assetRevaluationSearchRepository;

    public InternalAssetRevaluationServiceImpl(
        InternalAssetRevaluationRepository assetRevaluationRepository,
        AssetRevaluationMapper assetRevaluationMapper,
        AssetRevaluationSearchRepository assetRevaluationSearchRepository
    ) {
        this.assetRevaluationRepository = assetRevaluationRepository;
        this.assetRevaluationMapper = assetRevaluationMapper;
        this.assetRevaluationSearchRepository = assetRevaluationSearchRepository;
    }

    @Override
    public AssetRevaluationDTO save(AssetRevaluationDTO assetRevaluationDTO) {
        log.debug("Request to save AssetRevaluation : {}", assetRevaluationDTO);
        AssetRevaluation assetRevaluation = assetRevaluationMapper.toEntity(assetRevaluationDTO);
        assetRevaluation = assetRevaluationRepository.save(assetRevaluation);
        AssetRevaluationDTO result = assetRevaluationMapper.toDto(assetRevaluation);
        assetRevaluationSearchRepository.save(assetRevaluation);
        return result;
    }

    @Override
    public Optional<AssetRevaluationDTO> partialUpdate(AssetRevaluationDTO assetRevaluationDTO) {
        log.debug("Request to partially update AssetRevaluation : {}", assetRevaluationDTO);

        return assetRevaluationRepository
            .findById(assetRevaluationDTO.getId())
            .map(existingAssetRevaluation -> {
                assetRevaluationMapper.partialUpdate(existingAssetRevaluation, assetRevaluationDTO);

                return existingAssetRevaluation;
            })
            .map(assetRevaluationRepository::save)
            .map(savedAssetRevaluation -> {
                assetRevaluationSearchRepository.save(savedAssetRevaluation);

                return savedAssetRevaluation;
            })
            .map(assetRevaluationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRevaluationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetRevaluations");
        return assetRevaluationRepository.findAll(pageable).map(assetRevaluationMapper::toDto);
    }

    public Page<AssetRevaluationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetRevaluationRepository.findAllWithEagerRelationships(pageable).map(assetRevaluationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetRevaluationDTO> findOne(Long id) {
        log.debug("Request to get AssetRevaluation : {}", id);
        return assetRevaluationRepository.findOneWithEagerRelationships(id).map(assetRevaluationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetRevaluation : {}", id);
        assetRevaluationRepository.deleteById(id);
        assetRevaluationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRevaluationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRevaluations for query {}", query);
        return assetRevaluationSearchRepository.search(query, pageable).map(assetRevaluationMapper::toDto);
    }

    /**
     * Get the list of revalued items given the id of the asset
     *
     * @param revaluedAssetId             the id of the asset probably disposed.
     * @param depreciationPeriodStartDate start date of the depreciation-period of the depreciation job
     * @return the entities
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<AssetRevaluationDTO>> findRevaluedItems(Long revaluedAssetId, LocalDate depreciationPeriodStartDate) {

        return assetRevaluationRepository.findAssetRevaluation(revaluedAssetId, depreciationPeriodStartDate)
            .map(assetRevaluationMapper::toDto);
    }
}
