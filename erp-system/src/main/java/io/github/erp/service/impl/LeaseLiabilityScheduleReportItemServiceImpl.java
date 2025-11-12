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

import io.github.erp.domain.LeaseLiabilityScheduleReportItem;
import io.github.erp.repository.LeaseLiabilityScheduleReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportItemSearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleReportItemService;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseLiabilityScheduleReportItem}.
 */
@Service
@Transactional
public class LeaseLiabilityScheduleReportItemServiceImpl implements LeaseLiabilityScheduleReportItemService {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleReportItemServiceImpl.class);

    private final LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository;

    private final LeaseLiabilityScheduleReportItemMapper leaseLiabilityScheduleReportItemMapper;

    private final LeaseLiabilityScheduleReportItemSearchRepository leaseLiabilityScheduleReportItemSearchRepository;

    public LeaseLiabilityScheduleReportItemServiceImpl(
        LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository,
        LeaseLiabilityScheduleReportItemMapper leaseLiabilityScheduleReportItemMapper,
        LeaseLiabilityScheduleReportItemSearchRepository leaseLiabilityScheduleReportItemSearchRepository
    ) {
        this.leaseLiabilityScheduleReportItemRepository = leaseLiabilityScheduleReportItemRepository;
        this.leaseLiabilityScheduleReportItemMapper = leaseLiabilityScheduleReportItemMapper;
        this.leaseLiabilityScheduleReportItemSearchRepository = leaseLiabilityScheduleReportItemSearchRepository;
    }

    @Override
    public LeaseLiabilityScheduleReportItemDTO save(LeaseLiabilityScheduleReportItemDTO leaseLiabilityScheduleReportItemDTO) {
        log.debug("Request to save LeaseLiabilityScheduleReportItem : {}", leaseLiabilityScheduleReportItemDTO);
        LeaseLiabilityScheduleReportItem leaseLiabilityScheduleReportItem = leaseLiabilityScheduleReportItemMapper.toEntity(
            leaseLiabilityScheduleReportItemDTO
        );
        leaseLiabilityScheduleReportItem = leaseLiabilityScheduleReportItemRepository.save(leaseLiabilityScheduleReportItem);
        LeaseLiabilityScheduleReportItemDTO result = leaseLiabilityScheduleReportItemMapper.toDto(leaseLiabilityScheduleReportItem);
        leaseLiabilityScheduleReportItemSearchRepository.save(leaseLiabilityScheduleReportItem);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityScheduleReportItemDTO> partialUpdate(
        LeaseLiabilityScheduleReportItemDTO leaseLiabilityScheduleReportItemDTO
    ) {
        log.debug("Request to partially update LeaseLiabilityScheduleReportItem : {}", leaseLiabilityScheduleReportItemDTO);

        return leaseLiabilityScheduleReportItemRepository
            .findById(leaseLiabilityScheduleReportItemDTO.getId())
            .map(existingLeaseLiabilityScheduleReportItem -> {
                leaseLiabilityScheduleReportItemMapper.partialUpdate(
                    existingLeaseLiabilityScheduleReportItem,
                    leaseLiabilityScheduleReportItemDTO
                );

                return existingLeaseLiabilityScheduleReportItem;
            })
            .map(leaseLiabilityScheduleReportItemRepository::save)
            .map(savedLeaseLiabilityScheduleReportItem -> {
                leaseLiabilityScheduleReportItemSearchRepository.save(savedLeaseLiabilityScheduleReportItem);

                return savedLeaseLiabilityScheduleReportItem;
            })
            .map(leaseLiabilityScheduleReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityScheduleReportItems");
        return leaseLiabilityScheduleReportItemRepository.findAll(pageable).map(leaseLiabilityScheduleReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityScheduleReportItemDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityScheduleReportItem : {}", id);
        return leaseLiabilityScheduleReportItemRepository.findById(id).map(leaseLiabilityScheduleReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityScheduleReportItem : {}", id);
        leaseLiabilityScheduleReportItemRepository.deleteById(id);
        leaseLiabilityScheduleReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityScheduleReportItems for query {}", query);
        return leaseLiabilityScheduleReportItemSearchRepository.search(query, pageable).map(leaseLiabilityScheduleReportItemMapper::toDto);
    }
}
