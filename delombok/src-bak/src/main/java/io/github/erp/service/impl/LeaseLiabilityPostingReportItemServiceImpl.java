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

import io.github.erp.domain.LeaseLiabilityPostingReportItem;
import io.github.erp.repository.LeaseLiabilityPostingReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportItemSearchRepository;
import io.github.erp.service.LeaseLiabilityPostingReportItemService;
import io.github.erp.service.dto.LeaseLiabilityPostingReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityPostingReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseLiabilityPostingReportItem}.
 */
@Service
@Transactional
public class LeaseLiabilityPostingReportItemServiceImpl implements LeaseLiabilityPostingReportItemService {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityPostingReportItemServiceImpl.class);

    private final LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository;

    private final LeaseLiabilityPostingReportItemMapper leaseLiabilityPostingReportItemMapper;

    private final LeaseLiabilityPostingReportItemSearchRepository leaseLiabilityPostingReportItemSearchRepository;

    public LeaseLiabilityPostingReportItemServiceImpl(
        LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository,
        LeaseLiabilityPostingReportItemMapper leaseLiabilityPostingReportItemMapper,
        LeaseLiabilityPostingReportItemSearchRepository leaseLiabilityPostingReportItemSearchRepository
    ) {
        this.leaseLiabilityPostingReportItemRepository = leaseLiabilityPostingReportItemRepository;
        this.leaseLiabilityPostingReportItemMapper = leaseLiabilityPostingReportItemMapper;
        this.leaseLiabilityPostingReportItemSearchRepository = leaseLiabilityPostingReportItemSearchRepository;
    }

    @Override
    public LeaseLiabilityPostingReportItemDTO save(LeaseLiabilityPostingReportItemDTO leaseLiabilityPostingReportItemDTO) {
        log.debug("Request to save LeaseLiabilityPostingReportItem : {}", leaseLiabilityPostingReportItemDTO);
        LeaseLiabilityPostingReportItem leaseLiabilityPostingReportItem = leaseLiabilityPostingReportItemMapper.toEntity(
            leaseLiabilityPostingReportItemDTO
        );
        leaseLiabilityPostingReportItem = leaseLiabilityPostingReportItemRepository.save(leaseLiabilityPostingReportItem);
        LeaseLiabilityPostingReportItemDTO result = leaseLiabilityPostingReportItemMapper.toDto(leaseLiabilityPostingReportItem);
        leaseLiabilityPostingReportItemSearchRepository.save(leaseLiabilityPostingReportItem);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityPostingReportItemDTO> partialUpdate(
        LeaseLiabilityPostingReportItemDTO leaseLiabilityPostingReportItemDTO
    ) {
        log.debug("Request to partially update LeaseLiabilityPostingReportItem : {}", leaseLiabilityPostingReportItemDTO);

        return leaseLiabilityPostingReportItemRepository
            .findById(leaseLiabilityPostingReportItemDTO.getId())
            .map(existingLeaseLiabilityPostingReportItem -> {
                leaseLiabilityPostingReportItemMapper.partialUpdate(
                    existingLeaseLiabilityPostingReportItem,
                    leaseLiabilityPostingReportItemDTO
                );

                return existingLeaseLiabilityPostingReportItem;
            })
            .map(leaseLiabilityPostingReportItemRepository::save)
            .map(savedLeaseLiabilityPostingReportItem -> {
                leaseLiabilityPostingReportItemSearchRepository.save(savedLeaseLiabilityPostingReportItem);

                return savedLeaseLiabilityPostingReportItem;
            })
            .map(leaseLiabilityPostingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityPostingReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityPostingReportItems");
        return leaseLiabilityPostingReportItemRepository.findAll(pageable).map(leaseLiabilityPostingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityPostingReportItemDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityPostingReportItem : {}", id);
        return leaseLiabilityPostingReportItemRepository.findById(id).map(leaseLiabilityPostingReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityPostingReportItem : {}", id);
        leaseLiabilityPostingReportItemRepository.deleteById(id);
        leaseLiabilityPostingReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityPostingReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityPostingReportItems for query {}", query);
        return leaseLiabilityPostingReportItemSearchRepository.search(query, pageable).map(leaseLiabilityPostingReportItemMapper::toDto);
    }
}
