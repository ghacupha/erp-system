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

import io.github.erp.domain.RouAssetListReportItem;
import io.github.erp.repository.RouAssetListReportItemRepository;
import io.github.erp.repository.search.RouAssetListReportItemSearchRepository;
import io.github.erp.service.RouAssetListReportItemService;
import io.github.erp.service.dto.RouAssetListReportItemDTO;
import io.github.erp.service.mapper.RouAssetListReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouAssetListReportItem}.
 */
@Service
@Transactional
public class RouAssetListReportItemServiceImpl implements RouAssetListReportItemService {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportItemServiceImpl.class);

    private final RouAssetListReportItemRepository rouAssetListReportItemRepository;

    private final RouAssetListReportItemMapper rouAssetListReportItemMapper;

    private final RouAssetListReportItemSearchRepository rouAssetListReportItemSearchRepository;

    public RouAssetListReportItemServiceImpl(
        RouAssetListReportItemRepository rouAssetListReportItemRepository,
        RouAssetListReportItemMapper rouAssetListReportItemMapper,
        RouAssetListReportItemSearchRepository rouAssetListReportItemSearchRepository
    ) {
        this.rouAssetListReportItemRepository = rouAssetListReportItemRepository;
        this.rouAssetListReportItemMapper = rouAssetListReportItemMapper;
        this.rouAssetListReportItemSearchRepository = rouAssetListReportItemSearchRepository;
    }

    @Override
    public RouAssetListReportItemDTO save(RouAssetListReportItemDTO rouAssetListReportItemDTO) {
        log.debug("Request to save RouAssetListReportItem : {}", rouAssetListReportItemDTO);
        RouAssetListReportItem rouAssetListReportItem = rouAssetListReportItemMapper.toEntity(rouAssetListReportItemDTO);
        rouAssetListReportItem = rouAssetListReportItemRepository.save(rouAssetListReportItem);
        RouAssetListReportItemDTO result = rouAssetListReportItemMapper.toDto(rouAssetListReportItem);
        rouAssetListReportItemSearchRepository.save(rouAssetListReportItem);
        return result;
    }

    @Override
    public Optional<RouAssetListReportItemDTO> partialUpdate(RouAssetListReportItemDTO rouAssetListReportItemDTO) {
        log.debug("Request to partially update RouAssetListReportItem : {}", rouAssetListReportItemDTO);

        return rouAssetListReportItemRepository
            .findById(rouAssetListReportItemDTO.getId())
            .map(existingRouAssetListReportItem -> {
                rouAssetListReportItemMapper.partialUpdate(existingRouAssetListReportItem, rouAssetListReportItemDTO);

                return existingRouAssetListReportItem;
            })
            .map(rouAssetListReportItemRepository::save)
            .map(savedRouAssetListReportItem -> {
                rouAssetListReportItemSearchRepository.save(savedRouAssetListReportItem);

                return savedRouAssetListReportItem;
            })
            .map(rouAssetListReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetListReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAssetListReportItems");
        return rouAssetListReportItemRepository.findAll(pageable).map(rouAssetListReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAssetListReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouAssetListReportItem : {}", id);
        return rouAssetListReportItemRepository.findById(id).map(rouAssetListReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAssetListReportItem : {}", id);
        rouAssetListReportItemRepository.deleteById(id);
        rouAssetListReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetListReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAssetListReportItems for query {}", query);
        return rouAssetListReportItemSearchRepository.search(query, pageable).map(rouAssetListReportItemMapper::toDto);
    }
}
