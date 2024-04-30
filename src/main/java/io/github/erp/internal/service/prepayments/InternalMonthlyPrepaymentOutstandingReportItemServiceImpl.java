package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.MonthlyPrepaymentOutstandingReportItem;
import io.github.erp.internal.model.mapping.MonthlyPrepaymentOutstandingReportInternalMapper;
import io.github.erp.internal.repository.InternalMonthlyPrepaymentOutstandingReportItemRepository;
import io.github.erp.repository.search.MonthlyPrepaymentOutstandingReportItemSearchRepository;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.dto.MonthlyPrepaymentOutstandingReportItemDTO;
import io.github.erp.service.mapper.MonthlyPrepaymentOutstandingReportItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MonthlyPrepaymentOutstandingReportItem}.
 */
@Service
@Transactional
public class InternalMonthlyPrepaymentOutstandingReportItemServiceImpl implements InternalMonthlyPrepaymentOutstandingReportItemService {

    private final Logger log = LoggerFactory.getLogger(InternalMonthlyPrepaymentOutstandingReportItemServiceImpl.class);

    private final InternalMonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository;

    private final MonthlyPrepaymentOutstandingReportItemMapper monthlyPrepaymentOutstandingReportItemMapper;

    private final MonthlyPrepaymentOutstandingReportItemSearchRepository monthlyPrepaymentOutstandingReportItemSearchRepository;

    private final MonthlyPrepaymentOutstandingReportInternalMapper monthlyPrepaymentOutstandingReportInternalMapper;

    public InternalMonthlyPrepaymentOutstandingReportItemServiceImpl(
        InternalMonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository,
        MonthlyPrepaymentOutstandingReportItemMapper monthlyPrepaymentOutstandingReportItemMapper,
        MonthlyPrepaymentOutstandingReportItemSearchRepository monthlyPrepaymentOutstandingReportItemSearchRepository,
        MonthlyPrepaymentOutstandingReportInternalMapper monthlyPrepaymentOutstandingReportInternalMapper) {
        this.monthlyPrepaymentOutstandingReportItemRepository = monthlyPrepaymentOutstandingReportItemRepository;
        this.monthlyPrepaymentOutstandingReportItemMapper = monthlyPrepaymentOutstandingReportItemMapper;
        this.monthlyPrepaymentOutstandingReportItemSearchRepository = monthlyPrepaymentOutstandingReportItemSearchRepository;
        this.monthlyPrepaymentOutstandingReportInternalMapper = monthlyPrepaymentOutstandingReportInternalMapper;
    }

    @Override
    public MonthlyPrepaymentOutstandingReportItemDTO save(
        MonthlyPrepaymentOutstandingReportItemDTO monthlyPrepaymentOutstandingReportItemDTO
    ) {
        log.debug("Request to save MonthlyPrepaymentOutstandingReportItem : {}", monthlyPrepaymentOutstandingReportItemDTO);
        MonthlyPrepaymentOutstandingReportItem monthlyPrepaymentOutstandingReportItem = monthlyPrepaymentOutstandingReportItemMapper.toEntity(
            monthlyPrepaymentOutstandingReportItemDTO
        );
        monthlyPrepaymentOutstandingReportItem =
            monthlyPrepaymentOutstandingReportItemRepository.save(monthlyPrepaymentOutstandingReportItem);
        MonthlyPrepaymentOutstandingReportItemDTO result = monthlyPrepaymentOutstandingReportItemMapper.toDto(
            monthlyPrepaymentOutstandingReportItem
        );
        monthlyPrepaymentOutstandingReportItemSearchRepository.save(monthlyPrepaymentOutstandingReportItem);
        return result;
    }

    @Override
    public Optional<MonthlyPrepaymentOutstandingReportItemDTO> partialUpdate(
        MonthlyPrepaymentOutstandingReportItemDTO monthlyPrepaymentOutstandingReportItemDTO
    ) {
        log.debug("Request to partially update MonthlyPrepaymentOutstandingReportItem : {}", monthlyPrepaymentOutstandingReportItemDTO);

        return monthlyPrepaymentOutstandingReportItemRepository
            .findById(monthlyPrepaymentOutstandingReportItemDTO.getId())
            .map(existingMonthlyPrepaymentOutstandingReportItem -> {
                monthlyPrepaymentOutstandingReportItemMapper.partialUpdate(
                    existingMonthlyPrepaymentOutstandingReportItem,
                    monthlyPrepaymentOutstandingReportItemDTO
                );

                return existingMonthlyPrepaymentOutstandingReportItem;
            })
            .map(monthlyPrepaymentOutstandingReportItemRepository::save)
            .map(savedMonthlyPrepaymentOutstandingReportItem -> {
                monthlyPrepaymentOutstandingReportItemSearchRepository.save(savedMonthlyPrepaymentOutstandingReportItem);

                return savedMonthlyPrepaymentOutstandingReportItem;
            })
            .map(monthlyPrepaymentOutstandingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentOutstandingReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MonthlyPrepaymentOutstandingReportItems");
        return monthlyPrepaymentOutstandingReportItemRepository.findAll(pageable).map(monthlyPrepaymentOutstandingReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentOutstandingReportItemDTO> findAllWithStartAndEndDate(Pageable pageable, FiscalYearDTO fiscalYearDTO) {
        log.debug("Request to get all MonthlyPrepaymentOutstandingReportItems");

        return monthlyPrepaymentOutstandingReportItemRepository
            .findReportItemsByFiscalPeriod(fiscalYearDTO.getStartDate(), fiscalYearDTO.getEndDate(), pageable)
            .map(monthlyPrepaymentOutstandingReportInternalMapper::toValue2);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonthlyPrepaymentOutstandingReportItemDTO> findOne(Long id) {
        log.debug("Request to get MonthlyPrepaymentOutstandingReportItem : {}", id);
        return monthlyPrepaymentOutstandingReportItemRepository.findById(id).map(monthlyPrepaymentOutstandingReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MonthlyPrepaymentOutstandingReportItem : {}", id);
        monthlyPrepaymentOutstandingReportItemRepository.deleteById(id);
        monthlyPrepaymentOutstandingReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentOutstandingReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MonthlyPrepaymentOutstandingReportItems for query {}", query);
        return monthlyPrepaymentOutstandingReportItemSearchRepository
            .search(query, pageable)
            .map(monthlyPrepaymentOutstandingReportItemMapper::toDto);
    }
}
