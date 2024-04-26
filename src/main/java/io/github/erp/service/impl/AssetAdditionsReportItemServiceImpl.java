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

import io.github.erp.domain.AssetAdditionsReportItem;
import io.github.erp.repository.AssetAdditionsReportItemRepository;
import io.github.erp.repository.search.AssetAdditionsReportItemSearchRepository;
import io.github.erp.service.AssetAdditionsReportItemService;
import io.github.erp.service.dto.AssetAdditionsReportItemDTO;
import io.github.erp.service.mapper.AssetAdditionsReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetAdditionsReportItem}.
 */
@Service
@Transactional
public class AssetAdditionsReportItemServiceImpl implements AssetAdditionsReportItemService {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportItemServiceImpl.class);

    private final AssetAdditionsReportItemRepository assetAdditionsReportItemRepository;

    private final AssetAdditionsReportItemMapper assetAdditionsReportItemMapper;

    private final AssetAdditionsReportItemSearchRepository assetAdditionsReportItemSearchRepository;

    public AssetAdditionsReportItemServiceImpl(
        AssetAdditionsReportItemRepository assetAdditionsReportItemRepository,
        AssetAdditionsReportItemMapper assetAdditionsReportItemMapper,
        AssetAdditionsReportItemSearchRepository assetAdditionsReportItemSearchRepository
    ) {
        this.assetAdditionsReportItemRepository = assetAdditionsReportItemRepository;
        this.assetAdditionsReportItemMapper = assetAdditionsReportItemMapper;
        this.assetAdditionsReportItemSearchRepository = assetAdditionsReportItemSearchRepository;
    }

    @Override
    public AssetAdditionsReportItemDTO save(AssetAdditionsReportItemDTO assetAdditionsReportItemDTO) {
        log.debug("Request to save AssetAdditionsReportItem : {}", assetAdditionsReportItemDTO);
        AssetAdditionsReportItem assetAdditionsReportItem = assetAdditionsReportItemMapper.toEntity(assetAdditionsReportItemDTO);
        assetAdditionsReportItem = assetAdditionsReportItemRepository.save(assetAdditionsReportItem);
        AssetAdditionsReportItemDTO result = assetAdditionsReportItemMapper.toDto(assetAdditionsReportItem);
        assetAdditionsReportItemSearchRepository.save(assetAdditionsReportItem);
        return result;
    }

    @Override
    public Optional<AssetAdditionsReportItemDTO> partialUpdate(AssetAdditionsReportItemDTO assetAdditionsReportItemDTO) {
        log.debug("Request to partially update AssetAdditionsReportItem : {}", assetAdditionsReportItemDTO);

        return assetAdditionsReportItemRepository
            .findById(assetAdditionsReportItemDTO.getId())
            .map(existingAssetAdditionsReportItem -> {
                assetAdditionsReportItemMapper.partialUpdate(existingAssetAdditionsReportItem, assetAdditionsReportItemDTO);

                return existingAssetAdditionsReportItem;
            })
            .map(assetAdditionsReportItemRepository::save)
            .map(savedAssetAdditionsReportItem -> {
                assetAdditionsReportItemSearchRepository.save(savedAssetAdditionsReportItem);

                return savedAssetAdditionsReportItem;
            })
            .map(assetAdditionsReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAdditionsReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetAdditionsReportItems");
        return assetAdditionsReportItemRepository.findAll(pageable).map(assetAdditionsReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetAdditionsReportItemDTO> findOne(Long id) {
        log.debug("Request to get AssetAdditionsReportItem : {}", id);
        return assetAdditionsReportItemRepository.findById(id).map(assetAdditionsReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetAdditionsReportItem : {}", id);
        assetAdditionsReportItemRepository.deleteById(id);
        assetAdditionsReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAdditionsReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetAdditionsReportItems for query {}", query);
        return assetAdditionsReportItemSearchRepository.search(query, pageable).map(assetAdditionsReportItemMapper::toDto);
    }
}
