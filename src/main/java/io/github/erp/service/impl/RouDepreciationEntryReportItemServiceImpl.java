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

import io.github.erp.domain.RouDepreciationEntryReportItem;
import io.github.erp.repository.RouDepreciationEntryReportItemRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportItemSearchRepository;
import io.github.erp.service.RouDepreciationEntryReportItemService;
import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationEntryReportItem}.
 */
@Service
@Transactional
public class RouDepreciationEntryReportItemServiceImpl implements RouDepreciationEntryReportItemService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportItemServiceImpl.class);

    private final RouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository;

    private final RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper;

    private final RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository;

    public RouDepreciationEntryReportItemServiceImpl(
        RouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository,
        RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper,
        RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository
    ) {
        this.rouDepreciationEntryReportItemRepository = rouDepreciationEntryReportItemRepository;
        this.rouDepreciationEntryReportItemMapper = rouDepreciationEntryReportItemMapper;
        this.rouDepreciationEntryReportItemSearchRepository = rouDepreciationEntryReportItemSearchRepository;
    }

    @Override
    public RouDepreciationEntryReportItemDTO save(RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO) {
        log.debug("Request to save RouDepreciationEntryReportItem : {}", rouDepreciationEntryReportItemDTO);
        RouDepreciationEntryReportItem rouDepreciationEntryReportItem = rouDepreciationEntryReportItemMapper.toEntity(
            rouDepreciationEntryReportItemDTO
        );
        rouDepreciationEntryReportItem = rouDepreciationEntryReportItemRepository.save(rouDepreciationEntryReportItem);
        RouDepreciationEntryReportItemDTO result = rouDepreciationEntryReportItemMapper.toDto(rouDepreciationEntryReportItem);
        rouDepreciationEntryReportItemSearchRepository.save(rouDepreciationEntryReportItem);
        return result;
    }

    @Override
    public Optional<RouDepreciationEntryReportItemDTO> partialUpdate(RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO) {
        log.debug("Request to partially update RouDepreciationEntryReportItem : {}", rouDepreciationEntryReportItemDTO);

        return rouDepreciationEntryReportItemRepository
            .findById(rouDepreciationEntryReportItemDTO.getId())
            .map(existingRouDepreciationEntryReportItem -> {
                rouDepreciationEntryReportItemMapper.partialUpdate(
                    existingRouDepreciationEntryReportItem,
                    rouDepreciationEntryReportItemDTO
                );

                return existingRouDepreciationEntryReportItem;
            })
            .map(rouDepreciationEntryReportItemRepository::save)
            .map(savedRouDepreciationEntryReportItem -> {
                rouDepreciationEntryReportItemSearchRepository.save(savedRouDepreciationEntryReportItem);

                return savedRouDepreciationEntryReportItem;
            })
            .map(rouDepreciationEntryReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationEntryReportItems");
        return rouDepreciationEntryReportItemRepository.findAll(pageable).map(rouDepreciationEntryReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationEntryReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationEntryReportItem : {}", id);
        return rouDepreciationEntryReportItemRepository.findById(id).map(rouDepreciationEntryReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationEntryReportItem : {}", id);
        rouDepreciationEntryReportItemRepository.deleteById(id);
        rouDepreciationEntryReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationEntryReportItems for query {}", query);
        return rouDepreciationEntryReportItemSearchRepository.search(query, pageable).map(rouDepreciationEntryReportItemMapper::toDto);
    }
}
