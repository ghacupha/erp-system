package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.RouDepreciationPostingReportItem;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import io.github.erp.internal.repository.InternalRouDepreciationEntryReportItemRepository;
import io.github.erp.repository.RouDepreciationPostingReportItemRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportItemSearchRepository;
import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RouDepreciationPostingReportItem}.
 */
@Service
@Transactional
public class InternalRouDepreciationPostingReportItemServiceImpl implements InternalRouDepreciationPostingReportItemService {

    private final Logger log = LoggerFactory.getLogger(InternalRouDepreciationPostingReportItemServiceImpl.class);

    private final RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository;

    private final RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper;

    private final RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository;

    private final Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationPostingReportItemDTO> rouDepreciationPostingReportItemDTOMapping;
    private final InternalRouDepreciationEntryReportItemRepository internalRouDepreciationEntryReportItemRepository;

    public InternalRouDepreciationPostingReportItemServiceImpl(
        RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository,
        RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper,
        RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository,
        Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationPostingReportItemDTO> rouDepreciationPostingReportItemDTOMapping,
        InternalRouDepreciationEntryReportItemRepository internalRouDepreciationEntryReportItemRepository) {
        this.rouDepreciationPostingReportItemRepository = rouDepreciationPostingReportItemRepository;
        this.rouDepreciationPostingReportItemMapper = rouDepreciationPostingReportItemMapper;
        this.rouDepreciationPostingReportItemSearchRepository = rouDepreciationPostingReportItemSearchRepository;
        this.rouDepreciationPostingReportItemDTOMapping = rouDepreciationPostingReportItemDTOMapping;
        this.internalRouDepreciationEntryReportItemRepository = internalRouDepreciationEntryReportItemRepository;
    }

    @Override
    public RouDepreciationPostingReportItemDTO save(RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO) {
        log.debug("Request to save RouDepreciationPostingReportItem : {}", rouDepreciationPostingReportItemDTO);
        RouDepreciationPostingReportItem rouDepreciationPostingReportItem = rouDepreciationPostingReportItemMapper.toEntity(
            rouDepreciationPostingReportItemDTO
        );
        rouDepreciationPostingReportItem = rouDepreciationPostingReportItemRepository.save(rouDepreciationPostingReportItem);
        RouDepreciationPostingReportItemDTO result = rouDepreciationPostingReportItemMapper.toDto(rouDepreciationPostingReportItem);
        rouDepreciationPostingReportItemSearchRepository.save(rouDepreciationPostingReportItem);
        return result;
    }

    @Override
    public Optional<RouDepreciationPostingReportItemDTO> partialUpdate(
        RouDepreciationPostingReportItemDTO rouDepreciationPostingReportItemDTO
    ) {
        log.debug("Request to partially update RouDepreciationPostingReportItem : {}", rouDepreciationPostingReportItemDTO);

        return rouDepreciationPostingReportItemRepository
            .findById(rouDepreciationPostingReportItemDTO.getId())
            .map(existingRouDepreciationPostingReportItem -> {
                rouDepreciationPostingReportItemMapper.partialUpdate(
                    existingRouDepreciationPostingReportItem,
                    rouDepreciationPostingReportItemDTO
                );

                return existingRouDepreciationPostingReportItem;
            })
            .map(rouDepreciationPostingReportItemRepository::save)
            .map(savedRouDepreciationPostingReportItem -> {
                rouDepreciationPostingReportItemSearchRepository.save(savedRouDepreciationPostingReportItem);

                return savedRouDepreciationPostingReportItem;
            })
            .map(rouDepreciationPostingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationPostingReportItems");
        return rouDepreciationPostingReportItemRepository.findAll(pageable).map(rouDepreciationPostingReportItemMapper::toDto);
    }

    /**
     * Get all the rouDepreciationPostingReportItems for a specified lease period.
     *
     * @param pageable      the pagination information.
     * @param leasePeriodId the lease period iid
     * @return the list of entities.
     */
    @Override
    public Page<RouDepreciationPostingReportItemDTO> findAllByLeasePeriodId(Pageable pageable, long leasePeriodId) {
        log.debug("Request to get all RouDepreciationPostingReportItems, by lease-period id: {}", leasePeriodId);
        return internalRouDepreciationEntryReportItemRepository.getAllByLeasePeriodId(pageable, leasePeriodId)
        .map(rouDepreciationPostingReportItemDTOMapping::toValue2);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationPostingReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationPostingReportItem : {}", id);
        return rouDepreciationPostingReportItemRepository.findById(id).map(rouDepreciationPostingReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationPostingReportItem : {}", id);
        rouDepreciationPostingReportItemRepository.deleteById(id);
        rouDepreciationPostingReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationPostingReportItems for query {}", query);
        return rouDepreciationPostingReportItemSearchRepository.search(query, pageable).map(rouDepreciationPostingReportItemMapper::toDto);
    }
}
