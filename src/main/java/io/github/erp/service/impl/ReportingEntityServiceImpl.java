package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.ReportingEntity;
import io.github.erp.repository.ReportingEntityRepository;
import io.github.erp.repository.search.ReportingEntitySearchRepository;
import io.github.erp.service.ReportingEntityService;
import io.github.erp.service.dto.ReportingEntityDTO;
import io.github.erp.service.mapper.ReportingEntityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportingEntity}.
 */
@Service
@Transactional
public class ReportingEntityServiceImpl implements ReportingEntityService {

    private final Logger log = LoggerFactory.getLogger(ReportingEntityServiceImpl.class);

    private final ReportingEntityRepository reportingEntityRepository;

    private final ReportingEntityMapper reportingEntityMapper;

    private final ReportingEntitySearchRepository reportingEntitySearchRepository;

    public ReportingEntityServiceImpl(
        ReportingEntityRepository reportingEntityRepository,
        ReportingEntityMapper reportingEntityMapper,
        ReportingEntitySearchRepository reportingEntitySearchRepository
    ) {
        this.reportingEntityRepository = reportingEntityRepository;
        this.reportingEntityMapper = reportingEntityMapper;
        this.reportingEntitySearchRepository = reportingEntitySearchRepository;
    }

    @Override
    public ReportingEntityDTO save(ReportingEntityDTO reportingEntityDTO) {
        log.debug("Request to save ReportingEntity : {}", reportingEntityDTO);
        ReportingEntity reportingEntity = reportingEntityMapper.toEntity(reportingEntityDTO);
        reportingEntity = reportingEntityRepository.save(reportingEntity);
        ReportingEntityDTO result = reportingEntityMapper.toDto(reportingEntity);
        reportingEntitySearchRepository.save(reportingEntity);
        return result;
    }

    @Override
    public Optional<ReportingEntityDTO> partialUpdate(ReportingEntityDTO reportingEntityDTO) {
        log.debug("Request to partially update ReportingEntity : {}", reportingEntityDTO);

        return reportingEntityRepository
            .findById(reportingEntityDTO.getId())
            .map(existingReportingEntity -> {
                reportingEntityMapper.partialUpdate(existingReportingEntity, reportingEntityDTO);

                return existingReportingEntity;
            })
            .map(reportingEntityRepository::save)
            .map(savedReportingEntity -> {
                reportingEntitySearchRepository.save(savedReportingEntity);

                return savedReportingEntity;
            })
            .map(reportingEntityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportingEntityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportingEntities");
        return reportingEntityRepository.findAll(pageable).map(reportingEntityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportingEntityDTO> findOne(Long id) {
        log.debug("Request to get ReportingEntity : {}", id);
        return reportingEntityRepository.findById(id).map(reportingEntityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportingEntity : {}", id);
        reportingEntityRepository.deleteById(id);
        reportingEntitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportingEntityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportingEntities for query {}", query);
        return reportingEntitySearchRepository.search(query, pageable).map(reportingEntityMapper::toDto);
    }
}
