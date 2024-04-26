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

import io.github.erp.domain.RouDepreciationPostingReportItem;
import io.github.erp.repository.RouDepreciationPostingReportItemRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportItemSearchRepository;
import io.github.erp.service.RouDepreciationPostingReportItemService;
import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationPostingReportItem}.
 */
@Service
@Transactional
public class RouDepreciationPostingReportItemServiceImpl implements RouDepreciationPostingReportItemService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationPostingReportItemServiceImpl.class);

    private final RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository;

    private final RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper;

    private final RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository;

    public RouDepreciationPostingReportItemServiceImpl(
        RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository,
        RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper,
        RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository
    ) {
        this.rouDepreciationPostingReportItemRepository = rouDepreciationPostingReportItemRepository;
        this.rouDepreciationPostingReportItemMapper = rouDepreciationPostingReportItemMapper;
        this.rouDepreciationPostingReportItemSearchRepository = rouDepreciationPostingReportItemSearchRepository;
    }

    @Override
    public RouDepreciationPostingReportItemDTO save(RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO) {
        log.debug("Request to save RouDepreciationPostingReportItem : {}", rouDepreciationPostingReportItemDTO);
        RouDepreciationPostingReportItem rouDepreciationPostingReportItem = rouDepreciationPostingReportItemMapper.toEntity(
            rouDepreciationPostingReportItemDTO
        );
        rouDepreciationPostingReportItem = rouDepreciationPostingReportItemRepository.save(rouDepreciationPostingReportItem);
        RouDepreciationPostingReportItemDTO result = rouDepreciationPostingReportItemMapper.toDto(rouDepreciationPostingReportItem);
        rouDepreciationPostingReportItemSearchRepository.save(rouDepreciationPostingReportItem);
        return result;
    }

    @Override
    public Optional<RouDepreciationPostingReportItemDTO> partialUpdate(
        RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO
    ) {
        log.debug("Request to partially update RouDepreciationPostingReportItem : {}", rouDepreciationPostingReportItemDTO);

        return rouDepreciationPostingReportItemRepository
            .findById(rouDepreciationPostingReportItemDTO.getId())
            .map(existingRouDepreciationPostingReportItem -> {
                rouDepreciationPostingReportItemMapper.partialUpdate(
                    existingRouDepreciationPostingReportItem,
                    rouDepreciationPostingReportItemDTO
                );

                return existingRouDepreciationPostingReportItem;
            })
            .map(rouDepreciationPostingReportItemRepository::save)
            .map(savedRouDepreciationPostingReportItem -> {
                rouDepreciationPostingReportItemSearchRepository.save(savedRouDepreciationPostingReportItem);

                return savedRouDepreciationPostingReportItem;
            })
            .map(rouDepreciationPostingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationPostingReportItems");
        return rouDepreciationPostingReportItemRepository.findAll(pageable).map(rouDepreciationPostingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationPostingReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationPostingReportItem : {}", id);
        return rouDepreciationPostingReportItemRepository.findById(id).map(rouDepreciationPostingReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationPostingReportItem : {}", id);
        rouDepreciationPostingReportItemRepository.deleteById(id);
        rouDepreciationPostingReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationPostingReportItems for query {}", query);
        return rouDepreciationPostingReportItemSearchRepository.search(query, pageable).map(rouDepreciationPostingReportItemMapper::toDto);
    }
}
