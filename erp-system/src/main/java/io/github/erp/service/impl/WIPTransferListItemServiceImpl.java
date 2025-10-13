package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.WIPTransferListItem;
import io.github.erp.repository.WIPTransferListItemRepository;
import io.github.erp.repository.search.WIPTransferListItemSearchRepository;
import io.github.erp.service.WIPTransferListItemService;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import io.github.erp.service.mapper.WIPTransferListItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WIPTransferListItem}.
 */
@Service
@Transactional
public class WIPTransferListItemServiceImpl implements WIPTransferListItemService {

    private final Logger log = LoggerFactory.getLogger(WIPTransferListItemServiceImpl.class);

    private final WIPTransferListItemRepository wIPTransferListItemRepository;

    private final WIPTransferListItemMapper wIPTransferListItemMapper;

    private final WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository;

    public WIPTransferListItemServiceImpl(
        WIPTransferListItemRepository wIPTransferListItemRepository,
        WIPTransferListItemMapper wIPTransferListItemMapper,
        WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository
    ) {
        this.wIPTransferListItemRepository = wIPTransferListItemRepository;
        this.wIPTransferListItemMapper = wIPTransferListItemMapper;
        this.wIPTransferListItemSearchRepository = wIPTransferListItemSearchRepository;
    }

    @Override
    public WIPTransferListItemDTO save(WIPTransferListItemDTO wIPTransferListItemDTO) {
        log.debug("Request to save WIPTransferListItem : {}", wIPTransferListItemDTO);
        WIPTransferListItem wIPTransferListItem = wIPTransferListItemMapper.toEntity(wIPTransferListItemDTO);
        wIPTransferListItem = wIPTransferListItemRepository.save(wIPTransferListItem);
        WIPTransferListItemDTO result = wIPTransferListItemMapper.toDto(wIPTransferListItem);
        wIPTransferListItemSearchRepository.save(wIPTransferListItem);
        return result;
    }

    @Override
    public Optional<WIPTransferListItemDTO> partialUpdate(WIPTransferListItemDTO wIPTransferListItemDTO) {
        log.debug("Request to partially update WIPTransferListItem : {}", wIPTransferListItemDTO);

        return wIPTransferListItemRepository
            .findById(wIPTransferListItemDTO.getId())
            .map(existingWIPTransferListItem -> {
                wIPTransferListItemMapper.partialUpdate(existingWIPTransferListItem, wIPTransferListItemDTO);

                return existingWIPTransferListItem;
            })
            .map(wIPTransferListItemRepository::save)
            .map(savedWIPTransferListItem -> {
                wIPTransferListItemSearchRepository.save(savedWIPTransferListItem);

                return savedWIPTransferListItem;
            })
            .map(wIPTransferListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPTransferListItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPTransferListItems");
        return wIPTransferListItemRepository.findAll(pageable).map(wIPTransferListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPTransferListItemDTO> findOne(Long id) {
        log.debug("Request to get WIPTransferListItem : {}", id);
        return wIPTransferListItemRepository.findById(id).map(wIPTransferListItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPTransferListItem : {}", id);
        wIPTransferListItemRepository.deleteById(id);
        wIPTransferListItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPTransferListItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPTransferListItems for query {}", query);
        return wIPTransferListItemSearchRepository.search(query, pageable).map(wIPTransferListItemMapper::toDto);
    }
}
