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

import io.github.erp.domain.RouAssetNBVReportItem;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouAssetNBVReportItemInternal;
import io.github.erp.internal.repository.InternalRouAssetNBVReportItemRepository;
import io.github.erp.repository.search.RouAssetNBVReportItemSearchRepository;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
import io.github.erp.service.mapper.RouAssetNBVReportItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RouAssetNBVReportItem}.
 */
@Service
@Transactional
public class InternalRouAssetNBVReportItemServiceImpl implements InternalRouAssetNBVReportItemService {

    private final Logger log = LoggerFactory.getLogger(InternalRouAssetNBVReportItemServiceImpl.class);

    private final InternalRouAssetNBVReportItemRepository rouAssetNBVReportItemRepository;

    private final RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper;

    private final RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository;

    private final Mapping<RouAssetNBVReportItemInternal,RouAssetNBVReportItemDTO> rouAssetNBVReportItemInternalMapping;

    public InternalRouAssetNBVReportItemServiceImpl(
        InternalRouAssetNBVReportItemRepository rouAssetNBVReportItemRepository,
        RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper,
        RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository,
        Mapping<RouAssetNBVReportItemInternal, RouAssetNBVReportItemDTO> rouAssetNBVReportItemInternalMapping) {
        this.rouAssetNBVReportItemRepository = rouAssetNBVReportItemRepository;
        this.rouAssetNBVReportItemMapper = rouAssetNBVReportItemMapper;
        this.rouAssetNBVReportItemSearchRepository = rouAssetNBVReportItemSearchRepository;
        this.rouAssetNBVReportItemInternalMapping = rouAssetNBVReportItemInternalMapping;
    }

    @Override
    public RouAssetNBVReportItemDTO save(RouAssetNBVReportItemDTO rouAssetNBVReportItemDTO) {
        log.debug("Request to save RouAssetNBVReportItem : {}", rouAssetNBVReportItemDTO);
        RouAssetNBVReportItem rouAssetNBVReportItem = rouAssetNBVReportItemMapper.toEntity(rouAssetNBVReportItemDTO);
        rouAssetNBVReportItem = rouAssetNBVReportItemRepository.save(rouAssetNBVReportItem);
        RouAssetNBVReportItemDTO result = rouAssetNBVReportItemMapper.toDto(rouAssetNBVReportItem);
        rouAssetNBVReportItemSearchRepository.save(rouAssetNBVReportItem);
        return result;
    }

    @Override
    public Optional<RouAssetNBVReportItemDTO> partialUpdate(RouAssetNBVReportItemDTO rouAssetNBVReportItemDTO) {
        log.debug("Request to partially update RouAssetNBVReportItem : {}", rouAssetNBVReportItemDTO);

        return rouAssetNBVReportItemRepository
            .findById(rouAssetNBVReportItemDTO.getId())
            .map(existingRouAssetNBVReportItem -> {
                rouAssetNBVReportItemMapper.partialUpdate(existingRouAssetNBVReportItem, rouAssetNBVReportItemDTO);

                return existingRouAssetNBVReportItem;
            })
            .map(rouAssetNBVReportItemRepository::save)
            .map(savedRouAssetNBVReportItem -> {
                rouAssetNBVReportItemSearchRepository.save(savedRouAssetNBVReportItem);

                return savedRouAssetNBVReportItem;
            })
            .map(rouAssetNBVReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAssetNBVReportItems");
        return rouAssetNBVReportItemRepository.findAll(pageable).map(rouAssetNBVReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAssetNBVReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouAssetNBVReportItem : {}", id);
        return rouAssetNBVReportItemRepository.findById(id).map(rouAssetNBVReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAssetNBVReportItem : {}", id);
        rouAssetNBVReportItemRepository.deleteById(id);
        rouAssetNBVReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAssetNBVReportItems for query {}", query);
        return rouAssetNBVReportItemSearchRepository.search(query, pageable).map(rouAssetNBVReportItemMapper::toDto);
    }

    /**
     * Get all the rouAssetNBVReportItems.
     *
     * @param pageable      the pagination information.
     * @param leasePeriodId id of the leasePeriod in question
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportItemDTO> findAllAsAtPeriod(Pageable pageable, long leasePeriodId) {
        log.debug("Request for list of nbv items at the end of lease-period with id {}", leasePeriodId);

        return rouAssetNBVReportItemRepository.getAllByLeasePeriodId(pageable, leasePeriodId)
            .map(rouAssetNBVReportItemInternalMapping::toValue2);
    }
}
