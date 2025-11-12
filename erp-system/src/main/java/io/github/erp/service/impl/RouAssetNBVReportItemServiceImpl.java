package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
