package io.github.erp.internal.service.rou;

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
import io.github.erp.domain.RouDepreciationEntryReportItem;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import io.github.erp.internal.repository.InternalRouDepreciationEntryReportItemRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportItemSearchRepository;
import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RouDepreciationEntryReportItem}.
 */
@Service
@Transactional
public class InternalRouDepreciationEntryReportItemServiceImpl implements InternalRouDepreciationEntryReportItemService {

    private final Logger log = LoggerFactory.getLogger(InternalRouDepreciationEntryReportItemServiceImpl.class);

    private final InternalRouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository;

    private final RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper;

    private final RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository;

    private final Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationEntryReportItemDTO> rouDepreciationEntryReportItemMapping;

    public InternalRouDepreciationEntryReportItemServiceImpl(
        InternalRouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository,
        RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper,
        RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository,
        Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationEntryReportItemDTO> rouDepreciationEntryReportItemMapping) {
        this.rouDepreciationEntryReportItemRepository = rouDepreciationEntryReportItemRepository;
        this.rouDepreciationEntryReportItemMapper = rouDepreciationEntryReportItemMapper;
        this.rouDepreciationEntryReportItemSearchRepository = rouDepreciationEntryReportItemSearchRepository;
        this.rouDepreciationEntryReportItemMapping = rouDepreciationEntryReportItemMapping;
    }

    @Override
    public RouDepreciationEntryReportItemDTO save(RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO) {
        log.debug("Request to save RouDepreciationEntryReportItem : {}", rouDepreciationEntryReportItemDTO);
        RouDepreciationEntryReportItem rouDepreciationEntryReportItem = rouDepreciationEntryReportItemMapper.toEntity(
            rouDepreciationEntryReportItemDTO
        );
        rouDepreciationEntryReportItem = rouDepreciationEntryReportItemRepository.save(rouDepreciationEntryReportItem);
        RouDepreciationEntryReportItemDTO result = rouDepreciationEntryReportItemMapper.toDto(rouDepreciationEntryReportItem);
        rouDepreciationEntryReportItemSearchRepository.save(rouDepreciationEntryReportItem);
        return result;
    }

    @Override
    public Optional<RouDepreciationEntryReportItemDTO> partialUpdate(RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO) {
        log.debug("Request to partially update RouDepreciationEntryReportItem : {}", rouDepreciationEntryReportItemDTO);

        return rouDepreciationEntryReportItemRepository
            .findById(rouDepreciationEntryReportItemDTO.getId())
            .map(existingRouDepreciationEntryReportItem -> {
                rouDepreciationEntryReportItemMapper.partialUpdate(
                    existingRouDepreciationEntryReportItem,
                    rouDepreciationEntryReportItemDTO
                );

                return existingRouDepreciationEntryReportItem;
            })
            .map(rouDepreciationEntryReportItemRepository::save)
            .map(savedRouDepreciationEntryReportItem -> {
                rouDepreciationEntryReportItemSearchRepository.save(savedRouDepreciationEntryReportItem);

                return savedRouDepreciationEntryReportItem;
            })
            .map(rouDepreciationEntryReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationEntryReportItems");
        return rouDepreciationEntryReportItemRepository.allDepreciationItemsReport(pageable)
            .map(rouDepreciationEntryReportItemMapping::toValue2);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportItemDTO> findAllByMetadataId(Pageable pageable, Long metadataId) {
        log.debug("Request to get all RouDepreciationEntryReportItems");
        return rouDepreciationEntryReportItemRepository.getAllByMetadataId(pageable, metadataId)
            .map(rouDepreciationEntryReportItemMapping::toValue2);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationEntryReportItemDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationEntryReportItem : {}", id);
        return rouDepreciationEntryReportItemRepository.getOneByDepreciationEntryId(id)
            .map(rouDepreciationEntryReportItemMapping::toValue2);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationEntryReportItem : {}", id);
        rouDepreciationEntryReportItemRepository.deleteById(id);
        rouDepreciationEntryReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationEntryReportItems for query {}", query);
        return rouDepreciationEntryReportItemSearchRepository.search(query, pageable).map(rouDepreciationEntryReportItemMapper::toDto);
    }

    /**
     * This is the list of items posted for a given lease period
     *
     * @param pageable      the pagination information
     * @param leasePeriodId id of the lease period
     * @return list of depreciation entries for the model
     */
    @Override
    public Page<RouDepreciationEntryReportItemDTO> findAllByLeasePeriodId(Pageable pageable, Long leasePeriodId) {
        log.debug("Request to get items posted for a given lease period id : {}", leasePeriodId);
        return rouDepreciationEntryReportItemRepository.getAllByLeasePeriodId(pageable, leasePeriodId)
            .map(rouDepreciationEntryReportItemMapping::toValue2);
    }
}
