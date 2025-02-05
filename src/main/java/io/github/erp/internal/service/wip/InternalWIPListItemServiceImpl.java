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

import io.github.erp.domain.WIPListItem;
import io.github.erp.domain.WIPListItemREPO;
import io.github.erp.domain.WIPListItem_;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalWIPListItemRepository;
import io.github.erp.repository.WIPListItemRepository;
import io.github.erp.repository.search.WIPListItemSearchRepository;
import io.github.erp.service.WIPListItemQueryService;
import io.github.erp.service.WIPListItemService;
import io.github.erp.service.criteria.WIPListItemCriteria;
import io.github.erp.service.dto.WIPListItemDTO;
import io.github.erp.service.mapper.WIPListItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link WIPListItem}.
 */
@Service("internalWIPListItemService")
@Transactional
public class InternalWIPListItemServiceImpl
        extends WIPListItemQueryService
        implements InternalWIPListItemService {

    private final Logger log = LoggerFactory.getLogger(InternalWIPListItemServiceImpl.class);

    private final InternalWIPListItemRepository wIPListItemRepository;

    private final WIPListItemMapper wIPListItemMapper;

    private final WIPListItemSearchRepository wIPListItemSearchRepository;

    private final Mapping<WIPListItemREPO, WIPListItemDTO> wipListItemREPOMapper;

    public InternalWIPListItemServiceImpl(
        WIPListItemRepository domainWIPListItemRepository,
        InternalWIPListItemRepository wIPListItemRepository,
        WIPListItemMapper wIPListItemMapper,
        WIPListItemSearchRepository wIPListItemSearchRepository,
        Mapping<WIPListItemREPO, WIPListItemDTO> wipListItemREPOMapper
    ) {
        super(domainWIPListItemRepository, wIPListItemMapper, wIPListItemSearchRepository);
        this.wIPListItemRepository = wIPListItemRepository;
        this.wIPListItemMapper = wIPListItemMapper;
        this.wIPListItemSearchRepository = wIPListItemSearchRepository;
        this.wipListItemREPOMapper = wipListItemREPOMapper;
    }

    @Override
    public WIPListItemDTO save(WIPListItemDTO wIPListItemDTO) {
        log.debug("Request to save WIPListItem : {}", wIPListItemDTO);
        WIPListItem wIPListItem = wIPListItemMapper.toEntity(wIPListItemDTO);
        wIPListItem = wIPListItemRepository.save(wIPListItem);
        WIPListItemDTO result = wIPListItemMapper.toDto(wIPListItem);
        wIPListItemSearchRepository.save(wIPListItem);
        return result;
    }

    @Override
    public Optional<WIPListItemDTO> partialUpdate(WIPListItemDTO wIPListItemDTO) {
        log.debug("Request to partially update WIPListItem : {}", wIPListItemDTO);

        return wIPListItemRepository
            .findById(wIPListItemDTO.getId())
            .map(existingWIPListItem -> {
                wIPListItemMapper.partialUpdate(existingWIPListItem, wIPListItemDTO);

                return existingWIPListItem;
            })
            .map(wIPListItemRepository::save)
            .map(savedWIPListItem -> {
                wIPListItemSearchRepository.save(savedWIPListItem);

                return savedWIPListItem;
            })
            .map(wIPListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPListItems");
        return wIPListItemRepository.findAll(pageable).map(wIPListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPListItemDTO> findOne(Long id) {
        log.debug("Request to get WIPListItem : {}", id);
        return wIPListItemRepository.findById(id).map(wIPListItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPListItem : {}", id);
        wIPListItemRepository.deleteById(id);
        wIPListItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPListItems for query {}", query);
        return wIPListItemSearchRepository.search(query, pageable).map(wIPListItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> findAllReportItemsByReportDate(Pageable pageable) {
        log.debug("Request to get all WIPListItems");
        return wIPListItemRepository.findAllReportItems(pageable).map(wipListItemREPOMapper::toValue2);
    }

    /**
     * Return a {@link Page} of {@link WIPListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> findByCriteria(WIPListItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPListItem> specification = super.createSpecification(criteria);
        return wIPListItemRepository.findAll(specification, page).map(wIPListItemMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link WIPListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Override
    public Page<WIPListItemDTO> findAll(WIPListItemCriteria criteria, Pageable page) {
        return findByCriteria(criteria, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPListItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WIPListItem> specification = createSpecification(criteria);
        return wIPListItemRepository.count(specification);
    }
}
