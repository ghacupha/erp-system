package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
