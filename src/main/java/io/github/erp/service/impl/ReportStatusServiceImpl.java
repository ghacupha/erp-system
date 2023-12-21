package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.ReportStatus;
import io.github.erp.repository.ReportStatusRepository;
import io.github.erp.repository.search.ReportStatusSearchRepository;
import io.github.erp.service.ReportStatusService;
import io.github.erp.service.dto.ReportStatusDTO;
import io.github.erp.service.mapper.ReportStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportStatus}.
 */
@Service
@Transactional
public class ReportStatusServiceImpl implements ReportStatusService {

    private final Logger log = LoggerFactory.getLogger(ReportStatusServiceImpl.class);

    private final ReportStatusRepository reportStatusRepository;

    private final ReportStatusMapper reportStatusMapper;

    private final ReportStatusSearchRepository reportStatusSearchRepository;

    public ReportStatusServiceImpl(
        ReportStatusRepository reportStatusRepository,
        ReportStatusMapper reportStatusMapper,
        ReportStatusSearchRepository reportStatusSearchRepository
    ) {
        this.reportStatusRepository = reportStatusRepository;
        this.reportStatusMapper = reportStatusMapper;
        this.reportStatusSearchRepository = reportStatusSearchRepository;
    }

    @Override
    public ReportStatusDTO save(ReportStatusDTO reportStatusDTO) {
        log.debug("Request to save ReportStatus : {}", reportStatusDTO);
        ReportStatus reportStatus = reportStatusMapper.toEntity(reportStatusDTO);
        reportStatus = reportStatusRepository.save(reportStatus);
        ReportStatusDTO result = reportStatusMapper.toDto(reportStatus);
        reportStatusSearchRepository.save(reportStatus);
        return result;
    }

    @Override
    public Optional<ReportStatusDTO> partialUpdate(ReportStatusDTO reportStatusDTO) {
        log.debug("Request to partially update ReportStatus : {}", reportStatusDTO);

        return reportStatusRepository
            .findById(reportStatusDTO.getId())
            .map(existingReportStatus -> {
                reportStatusMapper.partialUpdate(existingReportStatus, reportStatusDTO);

                return existingReportStatus;
            })
            .map(reportStatusRepository::save)
            .map(savedReportStatus -> {
                reportStatusSearchRepository.save(savedReportStatus);

                return savedReportStatus;
            })
            .map(reportStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportStatuses");
        return reportStatusRepository.findAll(pageable).map(reportStatusMapper::toDto);
    }

    public Page<ReportStatusDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportStatusRepository.findAllWithEagerRelationships(pageable).map(reportStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportStatusDTO> findOne(Long id) {
        log.debug("Request to get ReportStatus : {}", id);
        return reportStatusRepository.findOneWithEagerRelationships(id).map(reportStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportStatus : {}", id);
        reportStatusRepository.deleteById(id);
        reportStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportStatuses for query {}", query);
        return reportStatusSearchRepository.search(query, pageable).map(reportStatusMapper::toDto);
    }
}
