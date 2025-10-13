package io.github.erp.internal.service.wip;

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

import io.github.erp.domain.WIPTransferListItem;
import io.github.erp.domain.WIPTransferListItemREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalWIPTransferListItemRepository;
import io.github.erp.repository.WIPTransferListItemRepository;
import io.github.erp.repository.search.WIPTransferListItemSearchRepository;
import io.github.erp.service.WIPTransferListItemQueryService;
import io.github.erp.service.criteria.WIPTransferListItemCriteria;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import io.github.erp.service.mapper.WIPTransferListItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link WIPTransferListItem}.
 */
@Service("internalWIPTransferListItemServiceImpl")
@Transactional
public class InternalWIPTransferListItemServiceImpl
    extends WIPTransferListItemQueryService
    implements InternalWIPTransferListItemService {

    private final Logger log = LoggerFactory.getLogger(InternalWIPTransferListItemServiceImpl.class);

    private final InternalWIPTransferListItemRepository wIPTransferListItemRepository;

    private final WIPTransferListItemMapper wIPTransferListItemMapper;

    private final Mapping<WIPTransferListItemREPO, WIPTransferListItemDTO> wipTransferListItemMapper;

    private final WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository;

    public InternalWIPTransferListItemServiceImpl(
        WIPTransferListItemRepository wIPTransferListItemRepository,
        InternalWIPTransferListItemRepository internalWIPTransferListItemRepository,
        WIPTransferListItemMapper wIPTransferListItemMapper, Mapping<WIPTransferListItemREPO, WIPTransferListItemDTO> wipTransferListItemMapper,
        WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository
    ) {
        super(wIPTransferListItemRepository, wIPTransferListItemMapper, wIPTransferListItemSearchRepository);
        this.wIPTransferListItemRepository = internalWIPTransferListItemRepository;
        this.wIPTransferListItemMapper = wIPTransferListItemMapper;
        this.wipTransferListItemMapper = wipTransferListItemMapper;
        this.wIPTransferListItemSearchRepository = wIPTransferListItemSearchRepository;
    }

    @Override
    public WIPTransferListItemDTO save(WIPTransferListItemDTO wIPTransferListItemDTO) {
        log.debug("Request to save WIPTransferListItem : {}", wIPTransferListItemDTO);
        WIPTransferListItem wIPTransferListItem = wIPTransferListItemMapper.toEntity(wIPTransferListItemDTO);
        wIPTransferListItem = wIPTransferListItemRepository.save(wIPTransferListItem);
        WIPTransferListItemDTO result = wIPTransferListItemMapper.toDto(wIPTransferListItem);
        wIPTransferListItemSearchRepository.save(wIPTransferListItem);
        return result;
    }

    @Override
    public Optional<WIPTransferListItemDTO> partialUpdate(WIPTransferListItemDTO wIPTransferListItemDTO) {
        log.debug("Request to partially update WIPTransferListItem : {}", wIPTransferListItemDTO);

        return wIPTransferListItemRepository
            .findById(wIPTransferListItemDTO.getId())
            .map(existingWIPTransferListItem -> {
                wIPTransferListItemMapper.partialUpdate(existingWIPTransferListItem, wIPTransferListItemDTO);

                return existingWIPTransferListItem;
            })
            .map(wIPTransferListItemRepository::save)
            .map(savedWIPTransferListItem -> {
                wIPTransferListItemSearchRepository.save(savedWIPTransferListItem);

                return savedWIPTransferListItem;
            })
            .map(wIPTransferListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPTransferListItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPTransferListItems");
        return wIPTransferListItemRepository.findAll(pageable).map(wIPTransferListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPTransferListItemDTO> findOne(Long id) {
        log.debug("Request to get WIPTransferListItem : {}", id);
        return wIPTransferListItemRepository.findById(id).map(wIPTransferListItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPTransferListItem : {}", id);
        wIPTransferListItemRepository.deleteById(id);
        wIPTransferListItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPTransferListItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPTransferListItems for query {}", query);
        return wIPTransferListItemSearchRepository.search(query, pageable).map(wIPTransferListItemMapper::toDto);
    }

    @Override
    public Page<WIPTransferListItemDTO> findAllReportItems(Pageable pageable) {
        log.debug("Request to get all WIPTransferListItems");
        return wIPTransferListItemRepository.findAllReportItems(pageable).map(wipTransferListItemMapper::toValue2);
    }

    /**
     * Return a {@link Page} of {@link WIPTransferListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Override
    public Page<WIPTransferListItemDTO> findAllSpecifiedReportItems(WIPTransferListItemCriteria criteria, Pageable page) {

        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPTransferListItem> specification = createSpecification(criteria);
        return wIPTransferListItemRepository.findAllSpecifiedReportItems(specification, page).map(wipTransferListItemMapper::toValue2);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPTransferListItemCriteria criteria) {
        return super.countByCriteria(criteria);
    }

    /**
     * Return a {@link Page} of {@link WIPTransferListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPTransferListItemDTO> findByCriteria(WIPTransferListItemCriteria criteria, Pageable page) {
        return super.findByCriteria(criteria, page);
    }
}
