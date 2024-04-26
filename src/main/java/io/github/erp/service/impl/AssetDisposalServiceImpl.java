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

import io.github.erp.domain.AssetDisposal;
import io.github.erp.repository.AssetDisposalRepository;
import io.github.erp.repository.search.AssetDisposalSearchRepository;
import io.github.erp.service.AssetDisposalService;
import io.github.erp.service.dto.AssetDisposalDTO;
import io.github.erp.service.mapper.AssetDisposalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetDisposal}.
 */
@Service
@Transactional
public class AssetDisposalServiceImpl implements AssetDisposalService {

    private final Logger log = LoggerFactory.getLogger(AssetDisposalServiceImpl.class);

    private final AssetDisposalRepository assetDisposalRepository;

    private final AssetDisposalMapper assetDisposalMapper;

    private final AssetDisposalSearchRepository assetDisposalSearchRepository;

    public AssetDisposalServiceImpl(
        AssetDisposalRepository assetDisposalRepository,
        AssetDisposalMapper assetDisposalMapper,
        AssetDisposalSearchRepository assetDisposalSearchRepository
    ) {
        this.assetDisposalRepository = assetDisposalRepository;
        this.assetDisposalMapper = assetDisposalMapper;
        this.assetDisposalSearchRepository = assetDisposalSearchRepository;
    }

    @Override
    public AssetDisposalDTO save(AssetDisposalDTO assetDisposalDTO) {
        log.debug("Request to save AssetDisposal : {}", assetDisposalDTO);
        AssetDisposal assetDisposal = assetDisposalMapper.toEntity(assetDisposalDTO);
        assetDisposal = assetDisposalRepository.save(assetDisposal);
        AssetDisposalDTO result = assetDisposalMapper.toDto(assetDisposal);
        assetDisposalSearchRepository.save(assetDisposal);
        return result;
    }

    @Override
    public Optional<AssetDisposalDTO> partialUpdate(AssetDisposalDTO assetDisposalDTO) {
        log.debug("Request to partially update AssetDisposal : {}", assetDisposalDTO);

        return assetDisposalRepository
            .findById(assetDisposalDTO.getId())
            .map(existingAssetDisposal -> {
                assetDisposalMapper.partialUpdate(existingAssetDisposal, assetDisposalDTO);

                return existingAssetDisposal;
            })
            .map(assetDisposalRepository::save)
            .map(savedAssetDisposal -> {
                assetDisposalSearchRepository.save(savedAssetDisposal);

                return savedAssetDisposal;
            })
            .map(assetDisposalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDisposals");
        return assetDisposalRepository.findAll(pageable).map(assetDisposalMapper::toDto);
    }

    public Page<AssetDisposalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetDisposalRepository.findAllWithEagerRelationships(pageable).map(assetDisposalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetDisposalDTO> findOne(Long id) {
        log.debug("Request to get AssetDisposal : {}", id);
        return assetDisposalRepository.findOneWithEagerRelationships(id).map(assetDisposalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetDisposal : {}", id);
        assetDisposalRepository.deleteById(id);
        assetDisposalSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetDisposals for query {}", query);
        return assetDisposalSearchRepository.search(query, pageable).map(assetDisposalMapper::toDto);
    }
}
