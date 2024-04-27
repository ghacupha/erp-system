package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.domain.RouMonthlyDepreciationReportItem;
import io.github.erp.repository.RouMonthlyDepreciationReportItemRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportItemSearchRepository;
import io.github.erp.service.RouMonthlyDepreciationReportItemService;
import io.github.erp.service.dto.RouMonthlyDepreciationReportItemDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouMonthlyDepreciationReportItem}.
 */
@Service
@Transactional
public class RouMonthlyDepreciationReportItemServiceImpl implements RouMonthlyDepreciationReportItemService {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportItemServiceImpl.class);

    private final RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository;

    private final RouMonthlyDepreciationReportItemMapper rouMonthlyDepreciationReportItemMapper;

    private final RouMonthlyDepreciationReportItemSearchRepository rouMonthlyDepreciationReportItemSearchRepository;

    public RouMonthlyDepreciationReportItemServiceImpl(
        RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository,
        RouMonthlyDepreciationReportItemMapper rouMonthlyDepreciationReportItemMapper,
        RouMonthlyDepreciationReportItemSearchRepository rouMonthlyDepreciationReportItemSearchRepository
    ) {
        this.rouMonthlyDepreciationReportItemRepository = rouMonthlyDepreciationReportItemRepository;
        this.rouMonthlyDepreciationReportItemMapper = rouMonthlyDepreciationReportItemMapper;
        this.rouMonthlyDepreciationReportItemSearchRepository = rouMonthlyDepreciationReportItemSearchRepository;
    }

    @Override
    public RouMonthlyDepreciationReportItemDTO save(RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO) {
        log.debug("Request to save RouMonthlyDepreciationReportItem : {}", rouMonthlyDepreciationReportItemDTO);
        RouMonthlyDepreciationReportItem rouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemMapper.toEntity(
            rouMonthlyDepreciationReportItemDTO
        );
        rouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItemRepository.save(rouMonthlyDepreciationReportItem);
        RouMonthlyDepreciationReportItemDTO result = rouMonthlyDepreciationReportItemMapper.toDto(rouMonthlyDepreciationReportItem);
        rouMonthlyDepreciationReportItemSearchRepository.save(rouMonthlyDepreciationReportItem);
        return result;
    }

    @Override
    public Optional<RouMonthlyDepreciationReportItemDTO> partialUpdate(
        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO
    ) {
        log.debug("Request to partially update RouMonthlyDepreciationReportItem : {}", rouMonthlyDepreciationReportItemDTO);

        return rouMonthlyDepreciationReportItemRepository
            .findById(rouMonthlyDepreciationReportItemDTO.getId())
            .map(existingRouMonthlyDepreciationReportItem -> {
                rouMonthlyDepreciationReportItemMapper.partialUpdate(
                    existingRouMonthlyDepreciationReportItem,
                    rouMonthlyDepreciationReportItemDTO
                );

                return existingRouMonthlyDepreciationReportItem;
            })
            .map(rouMonthlyDepreciationReportItemRepository::save)
            .map(savedRouMonthlyDepreciationReportItem -> {
                rouMonthlyDepreciationReportItemSearchRepository.save(savedRouMonthlyDepreciationReportItem);

                return savedRouMonthlyDepreciationReportItem;
            })
            .map(rouMonthlyDepreciationReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouMonthlyDepreciationReportItems");
        return rouMonthlyDepreciationReportItemRepository.findAll(pageable).map(rouMonthlyDepreciationReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouMonthlyDepreciationReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouMonthlyDepreciationReportItem : {}", id);
        return rouMonthlyDepreciationReportItemRepository.findById(id).map(rouMonthlyDepreciationReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouMonthlyDepreciationReportItem : {}", id);
        rouMonthlyDepreciationReportItemRepository.deleteById(id);
        rouMonthlyDepreciationReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouMonthlyDepreciationReportItems for query {}", query);
        return rouMonthlyDepreciationReportItemSearchRepository.search(query, pageable).map(rouMonthlyDepreciationReportItemMapper::toDto);
    }
}
