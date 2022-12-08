package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.ReportRequisition;
import io.github.erp.repository.ReportRequisitionRepository;
import io.github.erp.repository.search.ReportRequisitionSearchRepository;
import io.github.erp.service.ReportRequisitionService;
import io.github.erp.service.dto.ReportRequisitionDTO;
import io.github.erp.service.mapper.ReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportRequisition}.
 */
@Service
@Transactional
public class ReportRequisitionServiceImpl implements ReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(ReportRequisitionServiceImpl.class);

    private final ReportRequisitionRepository reportRequisitionRepository;

    private final ReportRequisitionMapper reportRequisitionMapper;

    private final ReportRequisitionSearchRepository reportRequisitionSearchRepository;

    public ReportRequisitionServiceImpl(
        ReportRequisitionRepository reportRequisitionRepository,
        ReportRequisitionMapper reportRequisitionMapper,
        ReportRequisitionSearchRepository reportRequisitionSearchRepository
    ) {
        this.reportRequisitionRepository = reportRequisitionRepository;
        this.reportRequisitionMapper = reportRequisitionMapper;
        this.reportRequisitionSearchRepository = reportRequisitionSearchRepository;
    }

    @Override
    public ReportRequisitionDTO save(ReportRequisitionDTO reportRequisitionDTO) {
        log.debug("Request to save ReportRequisition : {}", reportRequisitionDTO);
        ReportRequisition reportRequisition = reportRequisitionMapper.toEntity(reportRequisitionDTO);
        reportRequisition = reportRequisitionRepository.save(reportRequisition);
        ReportRequisitionDTO result = reportRequisitionMapper.toDto(reportRequisition);
        reportRequisitionSearchRepository.save(reportRequisition);
        return result;
    }

    @Override
    public Optional<ReportRequisitionDTO> partialUpdate(ReportRequisitionDTO reportRequisitionDTO) {
        log.debug("Request to partially update ReportRequisition : {}", reportRequisitionDTO);

        return reportRequisitionRepository
            .findById(reportRequisitionDTO.getId())
            .map(existingReportRequisition -> {
                reportRequisitionMapper.partialUpdate(existingReportRequisition, reportRequisitionDTO);

                return existingReportRequisition;
            })
            .map(reportRequisitionRepository::save)
            .map(savedReportRequisition -> {
                reportRequisitionSearchRepository.save(savedReportRequisition);

                return savedReportRequisition;
            })
            .map(reportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportRequisitions");
        return reportRequisitionRepository.findAll(pageable).map(reportRequisitionMapper::toDto);
    }

    public Page<ReportRequisitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportRequisitionRepository.findAllWithEagerRelationships(pageable).map(reportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get ReportRequisition : {}", id);
        return reportRequisitionRepository.findOneWithEagerRelationships(id).map(reportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportRequisition : {}", id);
        reportRequisitionRepository.deleteById(id);
        reportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportRequisitions for query {}", query);
        return reportRequisitionSearchRepository.search(query, pageable).map(reportRequisitionMapper::toDto);
    }
}
