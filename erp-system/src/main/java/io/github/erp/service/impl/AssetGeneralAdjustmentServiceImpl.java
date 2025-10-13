package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AssetGeneralAdjustment;
import io.github.erp.repository.AssetGeneralAdjustmentRepository;
import io.github.erp.repository.search.AssetGeneralAdjustmentSearchRepository;
import io.github.erp.service.AssetGeneralAdjustmentService;
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
import io.github.erp.service.mapper.AssetGeneralAdjustmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetGeneralAdjustment}.
 */
@Service
@Transactional
public class AssetGeneralAdjustmentServiceImpl implements AssetGeneralAdjustmentService {

    private final Logger log = LoggerFactory.getLogger(AssetGeneralAdjustmentServiceImpl.class);

    private final AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository;

    private final AssetGeneralAdjustmentMapper assetGeneralAdjustmentMapper;

    private final AssetGeneralAdjustmentSearchRepository assetGeneralAdjustmentSearchRepository;

    public AssetGeneralAdjustmentServiceImpl(
        AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository,
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
}
