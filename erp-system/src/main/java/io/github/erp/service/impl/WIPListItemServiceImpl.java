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

import io.github.erp.domain.WIPListItem;
import io.github.erp.repository.WIPListItemRepository;
import io.github.erp.repository.search.WIPListItemSearchRepository;
import io.github.erp.service.WIPListItemService;
import io.github.erp.service.dto.WIPListItemDTO;
import io.github.erp.service.mapper.WIPListItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WIPListItem}.
 */
@Service
@Transactional
public class WIPListItemServiceImpl implements WIPListItemService {

    private final Logger log = LoggerFactory.getLogger(WIPListItemServiceImpl.class);

    private final WIPListItemRepository wIPListItemRepository;

    private final WIPListItemMapper wIPListItemMapper;

    private final WIPListItemSearchRepository wIPListItemSearchRepository;

    public WIPListItemServiceImpl(
        WIPListItemRepository wIPListItemRepository,
        WIPListItemMapper wIPListItemMapper,
        WIPListItemSearchRepository wIPListItemSearchRepository
    ) {
        this.wIPListItemRepository = wIPListItemRepository;
        this.wIPListItemMapper = wIPListItemMapper;
        this.wIPListItemSearchRepository = wIPListItemSearchRepository;
    }

    @Override
    public WIPListItemDTO save(WIPListItemDTO wIPListItemDTO) {
        log.debug("Request to save WIPListItem : {}", wIPListItemDTO);
        WIPListItem wIPListItem = wIPListItemMapper.toEntity(wIPListItemDTO);
        wIPListItem = wIPListItemRepository.save(wIPListItem);
        WIPListItemDTO result = wIPListItemMapper.toDto(wIPListItem);
        wIPListItemSearchRepository.save(wIPListItem);
        return result;
    }

    @Override
    public Optional<WIPListItemDTO> partialUpdate(WIPListItemDTO wIPListItemDTO) {
        log.debug("Request to partially update WIPListItem : {}", wIPListItemDTO);

        return wIPListItemRepository
            .findById(wIPListItemDTO.getId())
            .map(existingWIPListItem -> {
                wIPListItemMapper.partialUpdate(existingWIPListItem, wIPListItemDTO);

                return existingWIPListItem;
            })
            .map(wIPListItemRepository::save)
            .map(savedWIPListItem -> {
                wIPListItemSearchRepository.save(savedWIPListItem);

                return savedWIPListItem;
            })
            .map(wIPListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPListItems");
        return wIPListItemRepository.findAll(pageable).map(wIPListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPListItemDTO> findOne(Long id) {
        log.debug("Request to get WIPListItem : {}", id);
        return wIPListItemRepository.findById(id).map(wIPListItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPListItem : {}", id);
        wIPListItemRepository.deleteById(id);
        wIPListItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPListItems for query {}", query);
        return wIPListItemSearchRepository.search(query, pageable).map(wIPListItemMapper::toDto);
    }
}
