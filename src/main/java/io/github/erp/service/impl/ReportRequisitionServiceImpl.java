package io.github.erp.service.impl;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
