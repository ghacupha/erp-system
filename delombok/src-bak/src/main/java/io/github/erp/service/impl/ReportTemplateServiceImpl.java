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

import io.github.erp.domain.ReportTemplate;
import io.github.erp.repository.ReportTemplateRepository;
import io.github.erp.repository.search.ReportTemplateSearchRepository;
import io.github.erp.service.ReportTemplateService;
import io.github.erp.service.dto.ReportTemplateDTO;
import io.github.erp.service.mapper.ReportTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportTemplate}.
 */
@Service
@Transactional
public class ReportTemplateServiceImpl implements ReportTemplateService {

    private final Logger log = LoggerFactory.getLogger(ReportTemplateServiceImpl.class);

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateMapper reportTemplateMapper;

    private final ReportTemplateSearchRepository reportTemplateSearchRepository;

    public ReportTemplateServiceImpl(
        ReportTemplateRepository reportTemplateRepository,
        ReportTemplateMapper reportTemplateMapper,
        ReportTemplateSearchRepository reportTemplateSearchRepository
    ) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
        this.reportTemplateSearchRepository = reportTemplateSearchRepository;
    }

    @Override
    public ReportTemplateDTO save(ReportTemplateDTO reportTemplateDTO) {
        log.debug("Request to save ReportTemplate : {}", reportTemplateDTO);
        ReportTemplate reportTemplate = reportTemplateMapper.toEntity(reportTemplateDTO);
        reportTemplate = reportTemplateRepository.save(reportTemplate);
        ReportTemplateDTO result = reportTemplateMapper.toDto(reportTemplate);
        reportTemplateSearchRepository.save(reportTemplate);
        return result;
    }

    @Override
    public Optional<ReportTemplateDTO> partialUpdate(ReportTemplateDTO reportTemplateDTO) {
        log.debug("Request to partially update ReportTemplate : {}", reportTemplateDTO);

        return reportTemplateRepository
            .findById(reportTemplateDTO.getId())
            .map(existingReportTemplate -> {
                reportTemplateMapper.partialUpdate(existingReportTemplate, reportTemplateDTO);

                return existingReportTemplate;
            })
            .map(reportTemplateRepository::save)
            .map(savedReportTemplate -> {
                reportTemplateSearchRepository.save(savedReportTemplate);

                return savedReportTemplate;
            })
            .map(reportTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportTemplates");
        return reportTemplateRepository.findAll(pageable).map(reportTemplateMapper::toDto);
    }

    public Page<ReportTemplateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportTemplateRepository.findAllWithEagerRelationships(pageable).map(reportTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportTemplateDTO> findOne(Long id) {
        log.debug("Request to get ReportTemplate : {}", id);
        return reportTemplateRepository.findOneWithEagerRelationships(id).map(reportTemplateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportTemplate : {}", id);
        reportTemplateRepository.deleteById(id);
        reportTemplateSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportTemplates for query {}", query);
        return reportTemplateSearchRepository.search(query, pageable).map(reportTemplateMapper::toDto);
    }
}
