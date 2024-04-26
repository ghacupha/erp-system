package io.github.erp.service.impl;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.DerivativeUnderlyingAsset;
import io.github.erp.repository.DerivativeUnderlyingAssetRepository;
import io.github.erp.repository.search.DerivativeUnderlyingAssetSearchRepository;
import io.github.erp.service.DerivativeUnderlyingAssetService;
import io.github.erp.service.dto.DerivativeUnderlyingAssetDTO;
import io.github.erp.service.mapper.DerivativeUnderlyingAssetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DerivativeUnderlyingAsset}.
 */
@Service
@Transactional
public class DerivativeUnderlyingAssetServiceImpl implements DerivativeUnderlyingAssetService {

    private final Logger log = LoggerFactory.getLogger(DerivativeUnderlyingAssetServiceImpl.class);

    private final DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository;

    private final DerivativeUnderlyingAssetMapper derivativeUnderlyingAssetMapper;

    private final DerivativeUnderlyingAssetSearchRepository derivativeUnderlyingAssetSearchRepository;

    public DerivativeUnderlyingAssetServiceImpl(
        DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository,
        DerivativeUnderlyingAssetMapper derivativeUnderlyingAssetMapper,
        DerivativeUnderlyingAssetSearchRepository derivativeUnderlyingAssetSearchRepository
    ) {
        this.derivativeUnderlyingAssetRepository = derivativeUnderlyingAssetRepository;
        this.derivativeUnderlyingAssetMapper = derivativeUnderlyingAssetMapper;
        this.derivativeUnderlyingAssetSearchRepository = derivativeUnderlyingAssetSearchRepository;
    }

    @Override
    public DerivativeUnderlyingAssetDTO save(DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO) {
        log.debug("Request to save DerivativeUnderlyingAsset : {}", derivativeUnderlyingAssetDTO);
        DerivativeUnderlyingAsset derivativeUnderlyingAsset = derivativeUnderlyingAssetMapper.toEntity(derivativeUnderlyingAssetDTO);
        derivativeUnderlyingAsset = derivativeUnderlyingAssetRepository.save(derivativeUnderlyingAsset);
        DerivativeUnderlyingAssetDTO result = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);
        derivativeUnderlyingAssetSearchRepository.save(derivativeUnderlyingAsset);
        return result;
    }

    @Override
    public Optional<DerivativeUnderlyingAssetDTO> partialUpdate(DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO) {
        log.debug("Request to partially update DerivativeUnderlyingAsset : {}", derivativeUnderlyingAssetDTO);

        return derivativeUnderlyingAssetRepository
            .findById(derivativeUnderlyingAssetDTO.getId())
            .map(existingDerivativeUnderlyingAsset -> {
                derivativeUnderlyingAssetMapper.partialUpdate(existingDerivativeUnderlyingAsset, derivativeUnderlyingAssetDTO);

                return existingDerivativeUnderlyingAsset;
            })
            .map(derivativeUnderlyingAssetRepository::save)
            .map(savedDerivativeUnderlyingAsset -> {
                derivativeUnderlyingAssetSearchRepository.save(savedDerivativeUnderlyingAsset);

                return savedDerivativeUnderlyingAsset;
            })
            .map(derivativeUnderlyingAssetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DerivativeUnderlyingAssetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DerivativeUnderlyingAssets");
        return derivativeUnderlyingAssetRepository.findAll(pageable).map(derivativeUnderlyingAssetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DerivativeUnderlyingAssetDTO> findOne(Long id) {
        log.debug("Request to get DerivativeUnderlyingAsset : {}", id);
        return derivativeUnderlyingAssetRepository.findById(id).map(derivativeUnderlyingAssetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DerivativeUnderlyingAsset : {}", id);
        derivativeUnderlyingAssetRepository.deleteById(id);
        derivativeUnderlyingAssetSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DerivativeUnderlyingAssetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DerivativeUnderlyingAssets for query {}", query);
        return derivativeUnderlyingAssetSearchRepository.search(query, pageable).map(derivativeUnderlyingAssetMapper::toDto);
    }
}
