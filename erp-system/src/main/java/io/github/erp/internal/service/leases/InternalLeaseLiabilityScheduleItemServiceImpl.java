package io.github.erp.internal.service.leases;

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
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.impl.LeaseLiabilityCompilationServiceImpl;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseLiabilityScheduleItem}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityScheduleItemServiceImpl implements InternalLeaseLiabilityScheduleItemService {

    private static final int SEARCH_INDEX_BATCH_SIZE = 200;

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityScheduleItemServiceImpl.class);

    private final InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    private final LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    private final LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository;
    private final InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService;

    public InternalLeaseLiabilityScheduleItemServiceImpl(
        InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper,
        LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository,
        InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService) {
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityScheduleItemMapper = leaseLiabilityScheduleItemMapper;
        this.leaseLiabilityScheduleItemSearchRepository = leaseLiabilityScheduleItemSearchRepository;
        this.leaseLiabilityCompilationService = leaseLiabilityCompilationService;
    }

    @Override
    public LeaseLiabilityScheduleItemDTO save(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO) {
        log.debug("Request to save LeaseLiabilityScheduleItem : {}", leaseLiabilityScheduleItemDTO);
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem = leaseLiabilityScheduleItemMapper.toEntity(leaseLiabilityScheduleItemDTO);
        leaseLiabilityScheduleItem = leaseLiabilityScheduleItemRepository.save(leaseLiabilityScheduleItem);
        LeaseLiabilityScheduleItemDTO result = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);
        leaseLiabilityScheduleItemSearchRepository.save(leaseLiabilityScheduleItem);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityScheduleItemDTO> partialUpdate(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO) {
        log.debug("Request to partially update LeaseLiabilityScheduleItem : {}", leaseLiabilityScheduleItemDTO);

        return leaseLiabilityScheduleItemRepository
            .findById(leaseLiabilityScheduleItemDTO.getId())
            .map(existingLeaseLiabilityScheduleItem -> {
                leaseLiabilityScheduleItemMapper.partialUpdate(existingLeaseLiabilityScheduleItem, leaseLiabilityScheduleItemDTO);

                return existingLeaseLiabilityScheduleItem;
            })
            .map(leaseLiabilityScheduleItemRepository::save)
            .map(savedLeaseLiabilityScheduleItem -> {
                leaseLiabilityScheduleItemSearchRepository.save(savedLeaseLiabilityScheduleItem);

                return savedLeaseLiabilityScheduleItem;
            })
            .map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityScheduleItems");
        return leaseLiabilityScheduleItemRepository.findAll(pageable).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    public Page<LeaseLiabilityScheduleItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return leaseLiabilityScheduleItemRepository.findAllWithEagerRelationships(pageable).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityScheduleItemDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityScheduleItem : {}", id);
        return leaseLiabilityScheduleItemRepository.findOneWithEagerRelationships(id).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityScheduleItem : {}", id);
        leaseLiabilityScheduleItemRepository.deleteById(id);
        leaseLiabilityScheduleItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityScheduleItems for query {}", query);
        return leaseLiabilityScheduleItemSearchRepository.search(query, pageable).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    public void saveAll(List<LeaseLiabilityScheduleItemDTO> scheduleItems) {

        leaseLiabilityScheduleItemRepository.saveAll(new ArrayList<>(leaseLiabilityScheduleItemMapper.toEntity(scheduleItems)));

        leaseLiabilityScheduleItemSearchRepository.saveAll(leaseLiabilityScheduleItemMapper.toEntity(scheduleItems));
    }

    @Override
    public int updateActivationByCompilation(Long compilationId, boolean active) {
        log.debug("Request to update activation state for compilation {} to {}", compilationId, active);
        int affected = leaseLiabilityScheduleItemRepository.updateActiveStateByCompilation(compilationId, active);
        // TODO update with queue
        if (affected > 0) {
            leaseLiabilityCompilationService.updateActiveStateByCompilation(compilationId, active);
            Pageable pageable = PageRequest.of(0, SEARCH_INDEX_BATCH_SIZE);
            Page<LeaseLiabilityScheduleItem> page =
                leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(compilationId, pageable);
            while (!page.isEmpty()) {
                leaseLiabilityScheduleItemSearchRepository.saveAll(page.getContent());
                if (!page.hasNext()) {
                    break;
                }
                pageable = page.nextPageable();
                page = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(compilationId, pageable);
            }
        }
        return affected;
    }
}
