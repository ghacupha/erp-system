package io.github.erp.internal.service.leases;

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
import io.github.erp.domain.LeaseLiabilityByAccountReportItem;
import io.github.erp.internal.repository.InternalLeaseLiabilityByAccountReportItemRepository;
import io.github.erp.repository.LeaseLiabilityByAccountReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportItemSearchRepository;
import io.github.erp.service.LeaseLiabilityByAccountReportItemService;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityByAccountReportItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseLiabilityByAccountReportItem}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityByAccountReportItemServiceImpl implements InternalLeaseLiabilityByAccountReportItemService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityByAccountReportItemServiceImpl.class);

    private final InternalLeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository;

    private final LeaseLiabilityByAccountReportItemMapper leaseLiabilityByAccountReportItemMapper;

    private final LeaseLiabilityByAccountReportItemSearchRepository leaseLiabilityByAccountReportItemSearchRepository;

    public InternalLeaseLiabilityByAccountReportItemServiceImpl(
        InternalLeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository,
        LeaseLiabilityByAccountReportItemMapper leaseLiabilityByAccountReportItemMapper,
        LeaseLiabilityByAccountReportItemSearchRepository leaseLiabilityByAccountReportItemSearchRepository
    ) {
        this.leaseLiabilityByAccountReportItemRepository = leaseLiabilityByAccountReportItemRepository;
        this.leaseLiabilityByAccountReportItemMapper = leaseLiabilityByAccountReportItemMapper;
        this.leaseLiabilityByAccountReportItemSearchRepository = leaseLiabilityByAccountReportItemSearchRepository;
    }

    @Override
    public LeaseLiabilityByAccountReportItemDTO save(LeaseLiabilityByAccountReportItemDTO leaseLiabilityByAccountReportItemDTO) {
        log.debug("Request to save LeaseLiabilityByAccountReportItem : {}", leaseLiabilityByAccountReportItemDTO);
        LeaseLiabilityByAccountReportItem leaseLiabilityByAccountReportItem = leaseLiabilityByAccountReportItemMapper.toEntity(
            leaseLiabilityByAccountReportItemDTO
        );
        leaseLiabilityByAccountReportItem = leaseLiabilityByAccountReportItemRepository.save(leaseLiabilityByAccountReportItem);
        LeaseLiabilityByAccountReportItemDTO result = leaseLiabilityByAccountReportItemMapper.toDto(leaseLiabilityByAccountReportItem);
        leaseLiabilityByAccountReportItemSearchRepository.save(leaseLiabilityByAccountReportItem);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityByAccountReportItemDTO> partialUpdate(
        LeaseLiabilityByAccountReportItemDTO leaseLiabilityByAccountReportItemDTO
    ) {
        log.debug("Request to partially update LeaseLiabilityByAccountReportItem : {}", leaseLiabilityByAccountReportItemDTO);

        return leaseLiabilityByAccountReportItemRepository
            .findById(leaseLiabilityByAccountReportItemDTO.getId())
            .map(existingLeaseLiabilityByAccountReportItem -> {
                leaseLiabilityByAccountReportItemMapper.partialUpdate(
                    existingLeaseLiabilityByAccountReportItem,
                    leaseLiabilityByAccountReportItemDTO
                );

                return existingLeaseLiabilityByAccountReportItem;
            })
            .map(leaseLiabilityByAccountReportItemRepository::save)
            .map(savedLeaseLiabilityByAccountReportItem -> {
                leaseLiabilityByAccountReportItemSearchRepository.save(savedLeaseLiabilityByAccountReportItem);

                return savedLeaseLiabilityByAccountReportItem;
            })
            .map(leaseLiabilityByAccountReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityByAccountReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityByAccountReportItems");
        return leaseLiabilityByAccountReportItemRepository.findAll(pageable).map(leaseLiabilityByAccountReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityByAccountReportItemDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityByAccountReportItem : {}", id);
        return leaseLiabilityByAccountReportItemRepository.findById(id).map(leaseLiabilityByAccountReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityByAccountReportItem : {}", id);
        leaseLiabilityByAccountReportItemRepository.deleteById(id);
        leaseLiabilityByAccountReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityByAccountReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityByAccountReportItems for query {}", query);
        return leaseLiabilityByAccountReportItemSearchRepository
            .search(query, pageable)
            .map(leaseLiabilityByAccountReportItemMapper::toDto);
    }
}
