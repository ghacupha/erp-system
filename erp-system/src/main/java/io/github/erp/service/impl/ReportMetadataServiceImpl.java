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

import io.github.erp.domain.ReportMetadata;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.repository.search.ReportMetadataSearchRepository;
import io.github.erp.service.ReportMetadataService;
import io.github.erp.service.dto.ReportMetadataDTO;
import io.github.erp.service.mapper.ReportMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportMetadata}.
 */
@Service
@Transactional
public class ReportMetadataServiceImpl implements ReportMetadataService {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataServiceImpl.class);

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataMapper reportMetadataMapper;

    private final ReportMetadataSearchRepository reportMetadataSearchRepository;

    public ReportMetadataServiceImpl(
        ReportMetadataRepository reportMetadataRepository,
        ReportMetadataMapper reportMetadataMapper,
        ReportMetadataSearchRepository reportMetadataSearchRepository
    ) {
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataMapper = reportMetadataMapper;
        this.reportMetadataSearchRepository = reportMetadataSearchRepository;
    }

    @Override
    public ReportMetadataDTO save(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to save ReportMetadata : {}", reportMetadataDTO);
        ReportMetadata reportMetadata = reportMetadataMapper.toEntity(reportMetadataDTO);
        reportMetadata = reportMetadataRepository.save(reportMetadata);
        ReportMetadataDTO result = reportMetadataMapper.toDto(reportMetadata);
        reportMetadataSearchRepository.save(reportMetadata);
        return result;
    }

    @Override
    public Optional<ReportMetadataDTO> partialUpdate(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to partially update ReportMetadata : {}", reportMetadataDTO);

        return reportMetadataRepository
            .findById(reportMetadataDTO.getId())
            .map(existingReportMetadata -> {
                reportMetadataMapper.partialUpdate(existingReportMetadata, reportMetadataDTO);

                return existingReportMetadata;
            })
            .map(reportMetadataRepository::save)
            .map(savedReportMetadata -> {
                reportMetadataSearchRepository.save(savedReportMetadata);
                return savedReportMetadata;
            })
            .map(reportMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportMetadata");
        return reportMetadataRepository.findAll(pageable).map(reportMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportMetadataDTO> findOne(Long id) {
        log.debug("Request to get ReportMetadata : {}", id);
        return reportMetadataRepository.findById(id).map(reportMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportMetadata : {}", id);
        reportMetadataRepository.deleteById(id);
        reportMetadataSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportMetadataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportMetadata for query {}", query);
        return reportMetadataSearchRepository.search(query, pageable).map(reportMetadataMapper::toDto);
    }
}
