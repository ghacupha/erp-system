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

import io.github.erp.domain.RouAssetNBVReportItem;
import io.github.erp.repository.RouAssetNBVReportItemRepository;
import io.github.erp.repository.search.RouAssetNBVReportItemSearchRepository;
import io.github.erp.service.RouAssetNBVReportItemService;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
import io.github.erp.service.mapper.RouAssetNBVReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouAssetNBVReportItem}.
 */
@Service
@Transactional
public class RouAssetNBVReportItemServiceImpl implements RouAssetNBVReportItemService {

    private final Logger log = LoggerFactory.getLogger(RouAssetNBVReportItemServiceImpl.class);

    private final RouAssetNBVReportItemRepository rouAssetNBVReportItemRepository;

    private final RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper;

    private final RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository;

    public RouAssetNBVReportItemServiceImpl(
        RouAssetNBVReportItemRepository rouAssetNBVReportItemRepository,
        RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper,
        RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository
    ) {
        this.rouAssetNBVReportItemRepository = rouAssetNBVReportItemRepository;
        this.rouAssetNBVReportItemMapper = rouAssetNBVReportItemMapper;
        this.rouAssetNBVReportItemSearchRepository = rouAssetNBVReportItemSearchRepository;
    }

    @Override
    public RouAssetNBVReportItemDTO save(RouAssetNBVReportItemDTO rouAssetNBVReportItemDTO) {
        log.debug("Request to save RouAssetNBVReportItem : {}", rouAssetNBVReportItemDTO);
        RouAssetNBVReportItem rouAssetNBVReportItem = rouAssetNBVReportItemMapper.toEntity(rouAssetNBVReportItemDTO);
        rouAssetNBVReportItem = rouAssetNBVReportItemRepository.save(rouAssetNBVReportItem);
        RouAssetNBVReportItemDTO result = rouAssetNBVReportItemMapper.toDto(rouAssetNBVReportItem);
        rouAssetNBVReportItemSearchRepository.save(rouAssetNBVReportItem);
        return result;
    }

    @Override
    public Optional<RouAssetNBVReportItemDTO> partialUpdate(RouAssetNBVReportItemDTO rouAssetNBVReportItemDTO) {
        log.debug("Request to partially update RouAssetNBVReportItem : {}", rouAssetNBVReportItemDTO);

        return rouAssetNBVReportItemRepository
            .findById(rouAssetNBVReportItemDTO.getId())
            .map(existingRouAssetNBVReportItem -> {
                rouAssetNBVReportItemMapper.partialUpdate(existingRouAssetNBVReportItem, rouAssetNBVReportItemDTO);

                return existingRouAssetNBVReportItem;
            })
            .map(rouAssetNBVReportItemRepository::save)
            .map(savedRouAssetNBVReportItem -> {
                rouAssetNBVReportItemSearchRepository.save(savedRouAssetNBVReportItem);

                return savedRouAssetNBVReportItem;
            })
            .map(rouAssetNBVReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAssetNBVReportItems");
        return rouAssetNBVReportItemRepository.findAll(pageable).map(rouAssetNBVReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAssetNBVReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouAssetNBVReportItem : {}", id);
        return rouAssetNBVReportItemRepository.findById(id).map(rouAssetNBVReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAssetNBVReportItem : {}", id);
        rouAssetNBVReportItemRepository.deleteById(id);
        rouAssetNBVReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAssetNBVReportItems for query {}", query);
        return rouAssetNBVReportItemSearchRepository.search(query, pageable).map(rouAssetNBVReportItemMapper::toDto);
    }
}
