package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.AssetCategory;
import io.github.erp.repository.AssetCategoryRepository;
import io.github.erp.repository.search.AssetCategorySearchRepository;
import io.github.erp.service.AssetCategoryService;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.mapper.AssetCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetCategory}.
 */
@Service
@Transactional
public class AssetCategoryServiceImpl implements AssetCategoryService {

    private final Logger log = LoggerFactory.getLogger(AssetCategoryServiceImpl.class);

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryMapper assetCategoryMapper;

    private final AssetCategorySearchRepository assetCategorySearchRepository;

    public AssetCategoryServiceImpl(
        AssetCategoryRepository assetCategoryRepository,
        AssetCategoryMapper assetCategoryMapper,
        AssetCategorySearchRepository assetCategorySearchRepository
    ) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryMapper = assetCategoryMapper;
        this.assetCategorySearchRepository = assetCategorySearchRepository;
    }

    @Override
    public AssetCategoryDTO save(AssetCategoryDTO assetCategoryDTO) {
        log.debug("Request to save AssetCategory : {}", assetCategoryDTO);
        AssetCategory assetCategory = assetCategoryMapper.toEntity(assetCategoryDTO);
        assetCategory = assetCategoryRepository.save(assetCategory);
        AssetCategoryDTO result = assetCategoryMapper.toDto(assetCategory);
        assetCategorySearchRepository.save(assetCategory);
        return result;
    }

    @Override
    public Optional<AssetCategoryDTO> partialUpdate(AssetCategoryDTO assetCategoryDTO) {
        log.debug("Request to partially update AssetCategory : {}", assetCategoryDTO);

        return assetCategoryRepository
            .findById(assetCategoryDTO.getId())
            .map(existingAssetCategory -> {
                assetCategoryMapper.partialUpdate(existingAssetCategory, assetCategoryDTO);

                return existingAssetCategory;
            })
            .map(assetCategoryRepository::save)
            .map(savedAssetCategory -> {
                assetCategorySearchRepository.save(savedAssetCategory);

                return savedAssetCategory;
            })
            .map(assetCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetCategories");
        return assetCategoryRepository.findAll(pageable).map(assetCategoryMapper::toDto);
    }

    public Page<AssetCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetCategoryRepository.findAllWithEagerRelationships(pageable).map(assetCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetCategoryDTO> findOne(Long id) {
        log.debug("Request to get AssetCategory : {}", id);
        return assetCategoryRepository.findOneWithEagerRelationships(id).map(assetCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetCategory : {}", id);
        assetCategoryRepository.deleteById(id);
        assetCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetCategories for query {}", query);
        return assetCategorySearchRepository.search(query, pageable).map(assetCategoryMapper::toDto);
    }
}
