package io.github.erp.internal.service.leases;

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

import io.github.erp.domain.LeaseLiabilityReportItem;
import io.github.erp.internal.repository.InternalLeaseLiabilityReportItemRepository;
import io.github.erp.repository.LeaseLiabilityReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityReportItemSearchRepository;
import io.github.erp.service.LeaseLiabilityReportItemService;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityReportItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseLiabilityReportItem}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityReportItemServiceImpl implements InternalLeaseLiabilityReportItemService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityReportItemServiceImpl.class);

    private final InternalLeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository;

    private final LeaseLiabilityReportItemMapper leaseLiabilityReportItemMapper;

    private final LeaseLiabilityReportItemSearchRepository leaseLiabilityReportItemSearchRepository;

    public InternalLeaseLiabilityReportItemServiceImpl(
        InternalLeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository,
        LeaseLiabilityReportItemMapper leaseLiabilityReportItemMapper,
        LeaseLiabilityReportItemSearchRepository leaseLiabilityReportItemSearchRepository
    ) {
        this.leaseLiabilityReportItemRepository = leaseLiabilityReportItemRepository;
        this.leaseLiabilityReportItemMapper = leaseLiabilityReportItemMapper;
        this.leaseLiabilityReportItemSearchRepository = leaseLiabilityReportItemSearchRepository;
    }

    @Override
    public LeaseLiabilityReportItemDTO save(LeaseLiabilityReportItemDTO leaseLiabilityReportItemDTO) {
        log.debug("Request to save LeaseLiabilityReportItem : {}", leaseLiabilityReportItemDTO);
        LeaseLiabilityReportItem leaseLiabilityReportItem = leaseLiabilityReportItemMapper.toEntity(leaseLiabilityReportItemDTO);
        leaseLiabilityReportItem = leaseLiabilityReportItemRepository.save(leaseLiabilityReportItem);
        LeaseLiabilityReportItemDTO result = leaseLiabilityReportItemMapper.toDto(leaseLiabilityReportItem);
        leaseLiabilityReportItemSearchRepository.save(leaseLiabilityReportItem);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityReportItemDTO> partialUpdate(LeaseLiabilityReportItemDTO leaseLiabilityReportItemDTO) {
        log.debug("Request to partially update LeaseLiabilityReportItem : {}", leaseLiabilityReportItemDTO);

        return leaseLiabilityReportItemRepository
            .findById(leaseLiabilityReportItemDTO.getId())
            .map(existingLeaseLiabilityReportItem -> {
                leaseLiabilityReportItemMapper.partialUpdate(existingLeaseLiabilityReportItem, leaseLiabilityReportItemDTO);

                return existingLeaseLiabilityReportItem;
            })
            .map(leaseLiabilityReportItemRepository::save)
            .map(savedLeaseLiabilityReportItem -> {
                leaseLiabilityReportItemSearchRepository.save(savedLeaseLiabilityReportItem);

                return savedLeaseLiabilityReportItem;
            })
            .map(leaseLiabilityReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityReportItems");
        return leaseLiabilityReportItemRepository.findAll(pageable).map(leaseLiabilityReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityReportItemDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityReportItem : {}", id);
        return leaseLiabilityReportItemRepository.findById(id).map(leaseLiabilityReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityReportItem : {}", id);
        leaseLiabilityReportItemRepository.deleteById(id);
        leaseLiabilityReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityReportItems for query {}", query);
        return leaseLiabilityReportItemSearchRepository.search(query, pageable).map(leaseLiabilityReportItemMapper::toDto);
    }
}
