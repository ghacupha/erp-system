package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
