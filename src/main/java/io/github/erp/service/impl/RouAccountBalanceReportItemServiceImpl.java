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

import io.github.erp.domain.RouAccountBalanceReportItem;
import io.github.erp.repository.RouAccountBalanceReportItemRepository;
import io.github.erp.repository.search.RouAccountBalanceReportItemSearchRepository;
import io.github.erp.service.RouAccountBalanceReportItemService;
import io.github.erp.service.dto.RouAccountBalanceReportItemDTO;
import io.github.erp.service.mapper.RouAccountBalanceReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouAccountBalanceReportItem}.
 */
@Service
@Transactional
public class RouAccountBalanceReportItemServiceImpl implements RouAccountBalanceReportItemService {

    private final Logger log = LoggerFactory.getLogger(RouAccountBalanceReportItemServiceImpl.class);

    private final RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository;

    private final RouAccountBalanceReportItemMapper rouAccountBalanceReportItemMapper;

    private final RouAccountBalanceReportItemSearchRepository rouAccountBalanceReportItemSearchRepository;

    public RouAccountBalanceReportItemServiceImpl(
        RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository,
        RouAccountBalanceReportItemMapper rouAccountBalanceReportItemMapper,
        RouAccountBalanceReportItemSearchRepository rouAccountBalanceReportItemSearchRepository
    ) {
        this.rouAccountBalanceReportItemRepository = rouAccountBalanceReportItemRepository;
        this.rouAccountBalanceReportItemMapper = rouAccountBalanceReportItemMapper;
        this.rouAccountBalanceReportItemSearchRepository = rouAccountBalanceReportItemSearchRepository;
    }

    @Override
    public RouAccountBalanceReportItemDTO save(RouAccountBalanceReportItemDTO rouAccountBalanceReportItemDTO) {
        log.debug("Request to save RouAccountBalanceReportItem : {}", rouAccountBalanceReportItemDTO);
        RouAccountBalanceReportItem rouAccountBalanceReportItem = rouAccountBalanceReportItemMapper.toEntity(
            rouAccountBalanceReportItemDTO
        );
        rouAccountBalanceReportItem = rouAccountBalanceReportItemRepository.save(rouAccountBalanceReportItem);
        RouAccountBalanceReportItemDTO result = rouAccountBalanceReportItemMapper.toDto(rouAccountBalanceReportItem);
        rouAccountBalanceReportItemSearchRepository.save(rouAccountBalanceReportItem);
        return result;
    }

    @Override
    public Optional<RouAccountBalanceReportItemDTO> partialUpdate(RouAccountBalanceReportItemDTO rouAccountBalanceReportItemDTO) {
        log.debug("Request to partially update RouAccountBalanceReportItem : {}", rouAccountBalanceReportItemDTO);

        return rouAccountBalanceReportItemRepository
            .findById(rouAccountBalanceReportItemDTO.getId())
            .map(existingRouAccountBalanceReportItem -> {
                rouAccountBalanceReportItemMapper.partialUpdate(existingRouAccountBalanceReportItem, rouAccountBalanceReportItemDTO);

                return existingRouAccountBalanceReportItem;
            })
            .map(rouAccountBalanceReportItemRepository::save)
            .map(savedRouAccountBalanceReportItem -> {
                rouAccountBalanceReportItemSearchRepository.save(savedRouAccountBalanceReportItem);

                return savedRouAccountBalanceReportItem;
            })
            .map(rouAccountBalanceReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAccountBalanceReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAccountBalanceReportItems");
        return rouAccountBalanceReportItemRepository.findAll(pageable).map(rouAccountBalanceReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAccountBalanceReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouAccountBalanceReportItem : {}", id);
        return rouAccountBalanceReportItemRepository.findById(id).map(rouAccountBalanceReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAccountBalanceReportItem : {}", id);
        rouAccountBalanceReportItemRepository.deleteById(id);
        rouAccountBalanceReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAccountBalanceReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAccountBalanceReportItems for query {}", query);
        return rouAccountBalanceReportItemSearchRepository.search(query, pageable).map(rouAccountBalanceReportItemMapper::toDto);
    }
}
