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

import io.github.erp.domain.AssetGeneralAdjustment;
import io.github.erp.internal.repository.InternalAssetGeneralAdjustmentRepository;
import io.github.erp.repository.AssetGeneralAdjustmentRepository;
import io.github.erp.repository.search.AssetGeneralAdjustmentSearchRepository;
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
import io.github.erp.service.mapper.AssetGeneralAdjustmentMapper;
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
 * Service Implementation for managing AssetGeneralAdjustment
 */
@Service
@Transactional
public class InternalAssetGeneralAdjustmentServiceImpl implements InternalAssetGeneralAdjustmentService {

    private final Logger log = LoggerFactory.getLogger(InternalAssetGeneralAdjustmentServiceImpl.class);

    private final InternalAssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository;

    private final AssetGeneralAdjustmentMapper assetGeneralAdjustmentMapper;

    private final AssetGeneralAdjustmentSearchRepository assetGeneralAdjustmentSearchRepository;

    public InternalAssetGeneralAdjustmentServiceImpl(
        InternalAssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository,
        AssetGeneralAdjustmentMapper assetGeneralAdjustmentMapper,
        AssetGeneralAdjustmentSearchRepository assetGeneralAdjustmentSearchRepository
    ) {
        this.assetGeneralAdjustmentRepository = assetGeneralAdjustmentRepository;
        this.assetGeneralAdjustmentMapper = assetGeneralAdjustmentMapper;
        this.assetGeneralAdjustmentSearchRepository = assetGeneralAdjustmentSearchRepository;
    }

    @Override
    public AssetGeneralAdjustmentDTO save(AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO) {
        log.debug("Request to save AssetGeneralAdjustment : {}", assetGeneralAdjustmentDTO);
        AssetGeneralAdjustment assetGeneralAdjustment = assetGeneralAdjustmentMapper.toEntity(assetGeneralAdjustmentDTO);
        assetGeneralAdjustment = assetGeneralAdjustmentRepository.save(assetGeneralAdjustment);
        AssetGeneralAdjustmentDTO result = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);
        assetGeneralAdjustmentSearchRepository.save(assetGeneralAdjustment);
        return result;
    }

    @Override
    public Optional<AssetGeneralAdjustmentDTO> partialUpdate(AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO) {
        log.debug("Request to partially update AssetGeneralAdjustment : {}", assetGeneralAdjustmentDTO);

        return assetGeneralAdjustmentRepository
            .findById(assetGeneralAdjustmentDTO.getId())
            .map(existingAssetGeneralAdjustment -> {
                assetGeneralAdjustmentMapper.partialUpdate(existingAssetGeneralAdjustment, assetGeneralAdjustmentDTO);

                return existingAssetGeneralAdjustment;
            })
            .map(assetGeneralAdjustmentRepository::save)
            .map(savedAssetGeneralAdjustment -> {
                assetGeneralAdjustmentSearchRepository.save(savedAssetGeneralAdjustment);

                return savedAssetGeneralAdjustment;
            })
            .map(assetGeneralAdjustmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetGeneralAdjustmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetGeneralAdjustments");
        return assetGeneralAdjustmentRepository.findAll(pageable).map(assetGeneralAdjustmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetGeneralAdjustmentDTO> findOne(Long id) {
        log.debug("Request to get AssetGeneralAdjustment : {}", id);
        return assetGeneralAdjustmentRepository.findById(id).map(assetGeneralAdjustmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetGeneralAdjustment : {}", id);
        assetGeneralAdjustmentRepository.deleteById(id);
        assetGeneralAdjustmentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetGeneralAdjustmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetGeneralAdjustments for query {}", query);
        return assetGeneralAdjustmentSearchRepository.search(query, pageable).map(assetGeneralAdjustmentMapper::toDto);
    }

    /**
     * Get the list of assetRevaluations.
     *
     * @param adjustedAssetId             the id of the asset probably disposed.
     * @param depreciationPeriodStartDate
     * @return the entities
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<AssetGeneralAdjustmentDTO>> findAdjustmentItems(Long adjustedAssetId, LocalDate depreciationPeriodStartDate) {
        return assetGeneralAdjustmentRepository.findAssetGeneralAdjustment(adjustedAssetId, depreciationPeriodStartDate)
            .map(assetGeneralAdjustmentMapper::toDto);
    }
}
