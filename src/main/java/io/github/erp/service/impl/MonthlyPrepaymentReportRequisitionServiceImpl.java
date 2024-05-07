package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.MonthlyPrepaymentReportRequisition;
import io.github.erp.repository.MonthlyPrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.MonthlyPrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.MonthlyPrepaymentReportRequisitionService;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.MonthlyPrepaymentReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MonthlyPrepaymentReportRequisition}.
 */
@Service
@Transactional
public class MonthlyPrepaymentReportRequisitionServiceImpl implements MonthlyPrepaymentReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(MonthlyPrepaymentReportRequisitionServiceImpl.class);

    private final MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository;

    private final MonthlyPrepaymentReportRequisitionMapper monthlyPrepaymentReportRequisitionMapper;

    private final MonthlyPrepaymentReportRequisitionSearchRepository monthlyPrepaymentReportRequisitionSearchRepository;

    public MonthlyPrepaymentReportRequisitionServiceImpl(
        MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository,
        MonthlyPrepaymentReportRequisitionMapper monthlyPrepaymentReportRequisitionMapper,
        MonthlyPrepaymentReportRequisitionSearchRepository monthlyPrepaymentReportRequisitionSearchRepository
    ) {
        this.monthlyPrepaymentReportRequisitionRepository = monthlyPrepaymentReportRequisitionRepository;
        this.monthlyPrepaymentReportRequisitionMapper = monthlyPrepaymentReportRequisitionMapper;
        this.monthlyPrepaymentReportRequisitionSearchRepository = monthlyPrepaymentReportRequisitionSearchRepository;
    }

    @Override
    public MonthlyPrepaymentReportRequisitionDTO save(MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO) {
        log.debug("Request to save MonthlyPrepaymentReportRequisition : {}", monthlyPrepaymentReportRequisitionDTO);
        MonthlyPrepaymentReportRequisition monthlyPrepaymentReportRequisition = monthlyPrepaymentReportRequisitionMapper.toEntity(
            monthlyPrepaymentReportRequisitionDTO
        );
        monthlyPrepaymentReportRequisition = monthlyPrepaymentReportRequisitionRepository.save(monthlyPrepaymentReportRequisition);
        MonthlyPrepaymentReportRequisitionDTO result = monthlyPrepaymentReportRequisitionMapper.toDto(monthlyPrepaymentReportRequisition);
        monthlyPrepaymentReportRequisitionSearchRepository.save(monthlyPrepaymentReportRequisition);
        return result;
    }

    @Override
    public Optional<MonthlyPrepaymentReportRequisitionDTO> partialUpdate(
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO
    ) {
        log.debug("Request to partially update MonthlyPrepaymentReportRequisition : {}", monthlyPrepaymentReportRequisitionDTO);

        return monthlyPrepaymentReportRequisitionRepository
            .findById(monthlyPrepaymentReportRequisitionDTO.getId())
            .map(existingMonthlyPrepaymentReportRequisition -> {
                monthlyPrepaymentReportRequisitionMapper.partialUpdate(
                    existingMonthlyPrepaymentReportRequisition,
                    monthlyPrepaymentReportRequisitionDTO
                );

                return existingMonthlyPrepaymentReportRequisition;
            })
            .map(monthlyPrepaymentReportRequisitionRepository::save)
            .map(savedMonthlyPrepaymentReportRequisition -> {
                monthlyPrepaymentReportRequisitionSearchRepository.save(savedMonthlyPrepaymentReportRequisition);

                return savedMonthlyPrepaymentReportRequisition;
            })
            .map(monthlyPrepaymentReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MonthlyPrepaymentReportRequisitions");
        return monthlyPrepaymentReportRequisitionRepository.findAll(pageable).map(monthlyPrepaymentReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonthlyPrepaymentReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get MonthlyPrepaymentReportRequisition : {}", id);
        return monthlyPrepaymentReportRequisitionRepository.findById(id).map(monthlyPrepaymentReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MonthlyPrepaymentReportRequisition : {}", id);
        monthlyPrepaymentReportRequisitionRepository.deleteById(id);
        monthlyPrepaymentReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MonthlyPrepaymentReportRequisitions for query {}", query);
        return monthlyPrepaymentReportRequisitionSearchRepository
            .search(query, pageable)
            .map(monthlyPrepaymentReportRequisitionMapper::toDto);
    }
}
