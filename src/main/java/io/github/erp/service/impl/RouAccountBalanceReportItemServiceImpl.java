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
