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
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilitySearchRepository;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

/**
 * Service Implementation for managing {@link LeaseLiabilityScheduleItem}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityScheduleItemServiceImpl implements InternalLeaseLiabilityScheduleItemService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityScheduleItemServiceImpl.class);

    private final InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    private final LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;
    private final LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository;
    private final LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;
    private final InternalLeaseLiabilityRepository leaseLiabilityRepository;
    private final LeaseLiabilitySearchRepository leaseLiabilitySearchRepository;

    public InternalLeaseLiabilityScheduleItemServiceImpl(
        InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper,
        LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository,
        LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository,
        InternalLeaseLiabilityRepository leaseLiabilityRepository,
        LeaseLiabilitySearchRepository leaseLiabilitySearchRepository) {
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityScheduleItemMapper = leaseLiabilityScheduleItemMapper;
        this.leaseLiabilityScheduleItemSearchRepository = leaseLiabilityScheduleItemSearchRepository;
        this.leaseLiabilityCompilationRepository = leaseLiabilityCompilationRepository;
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseLiabilitySearchRepository = leaseLiabilitySearchRepository;
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
        if (scheduleItems == null || scheduleItems.isEmpty()) {
            return;
        }

        List<LeaseLiabilityScheduleItemDTO> items = scheduleItems.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (items.isEmpty()) {
            return;
        }

        Map<Long, List<LeaseLiabilityScheduleItemDTO>> itemsByCompilation = new HashMap<>();
        for (LeaseLiabilityScheduleItemDTO item : items) {
            Long compilationId = extractCompilationId(item);
            itemsByCompilation.computeIfAbsent(compilationId, key -> new ArrayList<>()).add(item);
        }

        List<LeaseLiabilityScheduleItem> entitiesToPersist = new ArrayList<>();
        for (Map.Entry<Long, List<LeaseLiabilityScheduleItemDTO>> entry : itemsByCompilation.entrySet()) {
            Long compilationId = entry.getKey();
            Map<String, LeaseLiabilityScheduleItem> existingByKey = new HashMap<>();
            for (LeaseLiabilityScheduleItem existing : leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(compilationId)) {
                buildBusinessKey(existing).ifPresent(key -> existingByKey.putIfAbsent(key, existing));
            }

            for (LeaseLiabilityScheduleItemDTO dto : entry.getValue()) {
                LeaseLiabilityScheduleItem entity = leaseLiabilityScheduleItemMapper.toEntity(dto);
                buildBusinessKey(dto)
                    .map(existingByKey::get)
                    .ifPresent(existing -> entity.setId(existing.getId()));
                entitiesToPersist.add(entity);
            }
        }

        if (entitiesToPersist.isEmpty()) {
            return;
        }

        List<LeaseLiabilityScheduleItem> persisted = leaseLiabilityScheduleItemRepository.saveAll(entitiesToPersist);
        // leaseLiabilityScheduleItemSearchRepository.saveAll(persisted);

        Set<Long> processedLiabilityIds = persisted
            .stream()
            .map(LeaseLiabilityScheduleItem::getLeaseLiability)
            .filter(Objects::nonNull)
            .map(LeaseLiability::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (!processedLiabilityIds.isEmpty()) {
            List<LeaseLiability> liabilities = leaseLiabilityRepository.findAllById(processedLiabilityIds);
            List<LeaseLiability> changedLiabilities = liabilities
                .stream()
                .filter(liability -> !Boolean.TRUE.equals(liability.getHasBeenFullyAmortised()))
                .peek(liability -> liability.setHasBeenFullyAmortised(Boolean.TRUE))
                .collect(Collectors.toList());

            if (!changedLiabilities.isEmpty()) {
                List<LeaseLiability> savedLiabilities = leaseLiabilityRepository.saveAll(changedLiabilities);
                // leaseLiabilitySearchRepository.saveAll(savedLiabilities);
            }
        }
    }

    private Long extractCompilationId(LeaseLiabilityScheduleItemDTO item) {
        LeaseLiabilityCompilationDTO compilation = item.getLeaseLiabilityCompilation();
        if (compilation == null || compilation.getId() == null) {
            throw new IllegalArgumentException("Lease liability compilation id is required for schedule item persistence");
        }
        return compilation.getId();
    }

    private Optional<String> buildBusinessKey(LeaseLiabilityScheduleItemDTO dto) {
        LeaseLiabilityDTO liability = dto.getLeaseLiability();
        LeaseRepaymentPeriodDTO period = dto.getLeasePeriod();
        Long liabilityId = liability != null ? liability.getId() : null;
        Long periodId = period != null ? period.getId() : null;
        return buildBusinessKey(liabilityId, periodId);
    }

    private Optional<String> buildBusinessKey(LeaseLiabilityScheduleItem entity) {
        LeaseLiability liability = entity.getLeaseLiability();
        LeaseRepaymentPeriod period = entity.getLeasePeriod();
        Long liabilityId = liability != null ? liability.getId() : null;
        Long periodId = period != null ? period.getId() : null;
        return buildBusinessKey(liabilityId, periodId);
    }

    private Optional<String> buildBusinessKey(Long liabilityId, Long periodId) {
        if (liabilityId == null || periodId == null) {
            return Optional.empty();
        }
        return Optional.of(liabilityId + ":" + periodId);
    }

    @Override
    public int updateActivationByCompilation(Long compilationId, boolean active) {
        log.debug("Request to update activation state for compilation {} to {}", compilationId, active);
        int affected = leaseLiabilityScheduleItemRepository.updateActiveStateByCompilation(compilationId, active);
        if (affected > 0) {
            affected += updateCompilationActiveFlag(compilationId, active);
        }
        return affected;
    }

    private int updateCompilationActiveFlag(Long compilationId, boolean active) {
        return leaseLiabilityCompilationRepository.updateActiveStateById(compilationId, active);
    }
}
