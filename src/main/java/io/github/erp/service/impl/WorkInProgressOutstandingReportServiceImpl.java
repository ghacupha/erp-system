package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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

import io.github.erp.domain.WorkInProgressOutstandingReport;
import io.github.erp.repository.WorkInProgressOutstandingReportRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportSearchRepository;
import io.github.erp.service.WorkInProgressOutstandingReportService;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkInProgressOutstandingReport}.
 */
@Service
@Transactional
public class WorkInProgressOutstandingReportServiceImpl implements WorkInProgressOutstandingReportService {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOutstandingReportServiceImpl.class);

    private final WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository;

    private final WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper;

    private final WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository;

    public WorkInProgressOutstandingReportServiceImpl(
        WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository,
        WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper,
        WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository
    ) {
        this.workInProgressOutstandingReportRepository = workInProgressOutstandingReportRepository;
        this.workInProgressOutstandingReportMapper = workInProgressOutstandingReportMapper;
        this.workInProgressOutstandingReportSearchRepository = workInProgressOutstandingReportSearchRepository;
    }

    @Override
    public WorkInProgressOutstandingReportDTO save(WorkInProgressOutstandingReportDTO workInProgressOutstandingReportDTO) {
        log.debug("Request to save WorkInProgressOutstandingReport : {}", workInProgressOutstandingReportDTO);
        WorkInProgressOutstandingReport workInProgressOutstandingReport = workInProgressOutstandingReportMapper.toEntity(
            workInProgressOutstandingReportDTO
        );
        workInProgressOutstandingReport = workInProgressOutstandingReportRepository.save(workInProgressOutstandingReport);
        WorkInProgressOutstandingReportDTO result = workInProgressOutstandingReportMapper.toDto(workInProgressOutstandingReport);
        workInProgressOutstandingReportSearchRepository.save(workInProgressOutstandingReport);
        return result;
    }

    @Override
    public Optional<WorkInProgressOutstandingReportDTO> partialUpdate(
        WorkInProgressOutstandingReportDTO workInProgressOutstandingReportDTO
    ) {
        log.debug("Request to partially update WorkInProgressOutstandingReport : {}", workInProgressOutstandingReportDTO);

        return workInProgressOutstandingReportRepository
            .findById(workInProgressOutstandingReportDTO.getId())
            .map(existingWorkInProgressOutstandingReport -> {
                workInProgressOutstandingReportMapper.partialUpdate(
                    existingWorkInProgressOutstandingReport,
                    workInProgressOutstandingReportDTO
                );

                return existingWorkInProgressOutstandingReport;
            })
            .map(workInProgressOutstandingReportRepository::save)
            .map(savedWorkInProgressOutstandingReport -> {
                workInProgressOutstandingReportSearchRepository.save(savedWorkInProgressOutstandingReport);

                return savedWorkInProgressOutstandingReport;
            })
            .map(workInProgressOutstandingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkInProgressOutstandingReports");
        return workInProgressOutstandingReportRepository.findAll(pageable).map(workInProgressOutstandingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkInProgressOutstandingReportDTO> findOne(Long id) {
        log.debug("Request to get WorkInProgressOutstandingReport : {}", id);
        return workInProgressOutstandingReportRepository.findById(id).map(workInProgressOutstandingReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkInProgressOutstandingReport : {}", id);
        workInProgressOutstandingReportRepository.deleteById(id);
        workInProgressOutstandingReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkInProgressOutstandingReports for query {}", query);
        return workInProgressOutstandingReportSearchRepository.search(query, pageable).map(workInProgressOutstandingReportMapper::toDto);
    }
}
