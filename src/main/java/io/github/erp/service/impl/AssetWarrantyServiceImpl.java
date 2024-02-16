package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.AssetWarranty;
import io.github.erp.repository.AssetWarrantyRepository;
import io.github.erp.repository.search.AssetWarrantySearchRepository;
import io.github.erp.service.AssetWarrantyService;
import io.github.erp.service.dto.AssetWarrantyDTO;
import io.github.erp.service.mapper.AssetWarrantyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetWarranty}.
 */
@Service
@Transactional
public class AssetWarrantyServiceImpl implements AssetWarrantyService {

    private final Logger log = LoggerFactory.getLogger(AssetWarrantyServiceImpl.class);

    private final AssetWarrantyRepository assetWarrantyRepository;

    private final AssetWarrantyMapper assetWarrantyMapper;

    private final AssetWarrantySearchRepository assetWarrantySearchRepository;

    public AssetWarrantyServiceImpl(
        AssetWarrantyRepository assetWarrantyRepository,
        AssetWarrantyMapper assetWarrantyMapper,
        AssetWarrantySearchRepository assetWarrantySearchRepository
    ) {
        this.assetWarrantyRepository = assetWarrantyRepository;
        this.assetWarrantyMapper = assetWarrantyMapper;
        this.assetWarrantySearchRepository = assetWarrantySearchRepository;
    }

    @Override
    public AssetWarrantyDTO save(AssetWarrantyDTO assetWarrantyDTO) {
        log.debug("Request to save AssetWarranty : {}", assetWarrantyDTO);
        AssetWarranty assetWarranty = assetWarrantyMapper.toEntity(assetWarrantyDTO);
        assetWarranty = assetWarrantyRepository.save(assetWarranty);
        AssetWarrantyDTO result = assetWarrantyMapper.toDto(assetWarranty);
        assetWarrantySearchRepository.save(assetWarranty);
        return result;
    }

    @Override
    public Optional<AssetWarrantyDTO> partialUpdate(AssetWarrantyDTO assetWarrantyDTO) {
        log.debug("Request to partially update AssetWarranty : {}", assetWarrantyDTO);

        return assetWarrantyRepository
            .findById(assetWarrantyDTO.getId())
            .map(existingAssetWarranty -> {
                assetWarrantyMapper.partialUpdate(existingAssetWarranty, assetWarrantyDTO);

                return existingAssetWarranty;
            })
            .map(assetWarrantyRepository::save)
            .map(savedAssetWarranty -> {
                assetWarrantySearchRepository.save(savedAssetWarranty);

                return savedAssetWarranty;
            })
            .map(assetWarrantyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetWarrantyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetWarranties");
        return assetWarrantyRepository.findAll(pageable).map(assetWarrantyMapper::toDto);
    }

    public Page<AssetWarrantyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetWarrantyRepository.findAllWithEagerRelationships(pageable).map(assetWarrantyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetWarrantyDTO> findOne(Long id) {
        log.debug("Request to get AssetWarranty : {}", id);
        return assetWarrantyRepository.findOneWithEagerRelationships(id).map(assetWarrantyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetWarranty : {}", id);
        assetWarrantyRepository.deleteById(id);
        assetWarrantySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetWarrantyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetWarranties for query {}", query);
        return assetWarrantySearchRepository.search(query, pageable).map(assetWarrantyMapper::toDto);
    }
}
