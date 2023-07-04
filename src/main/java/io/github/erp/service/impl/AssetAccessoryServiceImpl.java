package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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

import io.github.erp.domain.AssetAccessory;
import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.repository.search.AssetAccessorySearchRepository;
import io.github.erp.service.AssetAccessoryService;
import io.github.erp.service.dto.AssetAccessoryDTO;
import io.github.erp.service.mapper.AssetAccessoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetAccessory}.
 */
@Service
@Transactional
public class AssetAccessoryServiceImpl implements AssetAccessoryService {

    private final Logger log = LoggerFactory.getLogger(AssetAccessoryServiceImpl.class);

    private final AssetAccessoryRepository assetAccessoryRepository;

    private final AssetAccessoryMapper assetAccessoryMapper;

    private final AssetAccessorySearchRepository assetAccessorySearchRepository;

    public AssetAccessoryServiceImpl(
        AssetAccessoryRepository assetAccessoryRepository,
        AssetAccessoryMapper assetAccessoryMapper,
        AssetAccessorySearchRepository assetAccessorySearchRepository
    ) {
        this.assetAccessoryRepository = assetAccessoryRepository;
        this.assetAccessoryMapper = assetAccessoryMapper;
        this.assetAccessorySearchRepository = assetAccessorySearchRepository;
    }

    @Override
    public AssetAccessoryDTO save(AssetAccessoryDTO assetAccessoryDTO) {
        log.debug("Request to save AssetAccessory : {}", assetAccessoryDTO);
        AssetAccessory assetAccessory = assetAccessoryMapper.toEntity(assetAccessoryDTO);
        assetAccessory = assetAccessoryRepository.save(assetAccessory);
        AssetAccessoryDTO result = assetAccessoryMapper.toDto(assetAccessory);
        assetAccessorySearchRepository.save(assetAccessory);
        return result;
    }

    @Override
    public Optional<AssetAccessoryDTO> partialUpdate(AssetAccessoryDTO assetAccessoryDTO) {
        log.debug("Request to partially update AssetAccessory : {}", assetAccessoryDTO);

        return assetAccessoryRepository
            .findById(assetAccessoryDTO.getId())
            .map(existingAssetAccessory -> {
                assetAccessoryMapper.partialUpdate(existingAssetAccessory, assetAccessoryDTO);

                return existingAssetAccessory;
            })
            .map(assetAccessoryRepository::save)
            .map(savedAssetAccessory -> {
                assetAccessorySearchRepository.save(savedAssetAccessory);

                return savedAssetAccessory;
            })
            .map(assetAccessoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAccessoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetAccessories");
        return assetAccessoryRepository.findAll(pageable).map(assetAccessoryMapper::toDto);
    }

    public Page<AssetAccessoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetAccessoryRepository.findAllWithEagerRelationships(pageable).map(assetAccessoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetAccessoryDTO> findOne(Long id) {
        log.debug("Request to get AssetAccessory : {}", id);
        return assetAccessoryRepository.findOneWithEagerRelationships(id).map(assetAccessoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetAccessory : {}", id);
        assetAccessoryRepository.deleteById(id);
        assetAccessorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAccessoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetAccessories for query {}", query);
        return assetAccessorySearchRepository.search(query, pageable).map(assetAccessoryMapper::toDto);
    }
}
