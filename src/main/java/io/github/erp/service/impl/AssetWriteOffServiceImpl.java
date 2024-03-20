package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AssetWriteOff;
import io.github.erp.repository.AssetWriteOffRepository;
import io.github.erp.repository.search.AssetWriteOffSearchRepository;
import io.github.erp.service.AssetWriteOffService;
import io.github.erp.service.dto.AssetWriteOffDTO;
import io.github.erp.service.mapper.AssetWriteOffMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetWriteOff}.
 */
@Service
@Transactional
public class AssetWriteOffServiceImpl implements AssetWriteOffService {

    private final Logger log = LoggerFactory.getLogger(AssetWriteOffServiceImpl.class);

    private final AssetWriteOffRepository assetWriteOffRepository;

    private final AssetWriteOffMapper assetWriteOffMapper;

    private final AssetWriteOffSearchRepository assetWriteOffSearchRepository;

    public AssetWriteOffServiceImpl(
        AssetWriteOffRepository assetWriteOffRepository,
        AssetWriteOffMapper assetWriteOffMapper,
        AssetWriteOffSearchRepository assetWriteOffSearchRepository
    ) {
        this.assetWriteOffRepository = assetWriteOffRepository;
        this.assetWriteOffMapper = assetWriteOffMapper;
        this.assetWriteOffSearchRepository = assetWriteOffSearchRepository;
    }

    @Override
    public AssetWriteOffDTO save(AssetWriteOffDTO assetWriteOffDTO) {
        log.debug("Request to save AssetWriteOff : {}", assetWriteOffDTO);
        AssetWriteOff assetWriteOff = assetWriteOffMapper.toEntity(assetWriteOffDTO);
        assetWriteOff = assetWriteOffRepository.save(assetWriteOff);
        AssetWriteOffDTO result = assetWriteOffMapper.toDto(assetWriteOff);
        assetWriteOffSearchRepository.save(assetWriteOff);
        return result;
    }

    @Override
    public Optional<AssetWriteOffDTO> partialUpdate(AssetWriteOffDTO assetWriteOffDTO) {
        log.debug("Request to partially update AssetWriteOff : {}", assetWriteOffDTO);

        return assetWriteOffRepository
            .findById(assetWriteOffDTO.getId())
            .map(existingAssetWriteOff -> {
                assetWriteOffMapper.partialUpdate(existingAssetWriteOff, assetWriteOffDTO);

                return existingAssetWriteOff;
            })
            .map(assetWriteOffRepository::save)
            .map(savedAssetWriteOff -> {
                assetWriteOffSearchRepository.save(savedAssetWriteOff);

                return savedAssetWriteOff;
            })
            .map(assetWriteOffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetWriteOffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetWriteOffs");
        return assetWriteOffRepository.findAll(pageable).map(assetWriteOffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetWriteOffDTO> findOne(Long id) {
        log.debug("Request to get AssetWriteOff : {}", id);
        return assetWriteOffRepository.findById(id).map(assetWriteOffMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetWriteOff : {}", id);
        assetWriteOffRepository.deleteById(id);
        assetWriteOffSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetWriteOffDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetWriteOffs for query {}", query);
        return assetWriteOffSearchRepository.search(query, pageable).map(assetWriteOffMapper::toDto);
    }
}
