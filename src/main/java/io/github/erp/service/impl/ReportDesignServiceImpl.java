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

import io.github.erp.domain.ReportDesign;
import io.github.erp.repository.ReportDesignRepository;
import io.github.erp.repository.search.ReportDesignSearchRepository;
import io.github.erp.service.ReportDesignService;
import io.github.erp.service.dto.ReportDesignDTO;
import io.github.erp.service.mapper.ReportDesignMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportDesign}.
 */
@Service
@Transactional
public class ReportDesignServiceImpl implements ReportDesignService {

    private final Logger log = LoggerFactory.getLogger(ReportDesignServiceImpl.class);

    private final ReportDesignRepository reportDesignRepository;

    private final ReportDesignMapper reportDesignMapper;

    private final ReportDesignSearchRepository reportDesignSearchRepository;

    public ReportDesignServiceImpl(
        ReportDesignRepository reportDesignRepository,
        ReportDesignMapper reportDesignMapper,
        ReportDesignSearchRepository reportDesignSearchRepository
    ) {
        this.reportDesignRepository = reportDesignRepository;
        this.reportDesignMapper = reportDesignMapper;
        this.reportDesignSearchRepository = reportDesignSearchRepository;
    }

    @Override
    public ReportDesignDTO save(ReportDesignDTO reportDesignDTO) {
        log.debug("Request to save ReportDesign : {}", reportDesignDTO);
        ReportDesign reportDesign = reportDesignMapper.toEntity(reportDesignDTO);
        reportDesign = reportDesignRepository.save(reportDesign);
        ReportDesignDTO result = reportDesignMapper.toDto(reportDesign);
        reportDesignSearchRepository.save(reportDesign);
        return result;
    }

    @Override
    public Optional<ReportDesignDTO> partialUpdate(ReportDesignDTO reportDesignDTO) {
        log.debug("Request to partially update ReportDesign : {}", reportDesignDTO);

        return reportDesignRepository
            .findById(reportDesignDTO.getId())
            .map(existingReportDesign -> {
                reportDesignMapper.partialUpdate(existingReportDesign, reportDesignDTO);

                return existingReportDesign;
            })
            .map(reportDesignRepository::save)
            .map(savedReportDesign -> {
                reportDesignSearchRepository.save(savedReportDesign);

                return savedReportDesign;
            })
            .map(reportDesignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportDesignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportDesigns");
        return reportDesignRepository.findAll(pageable).map(reportDesignMapper::toDto);
    }

    public Page<ReportDesignDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportDesignRepository.findAllWithEagerRelationships(pageable).map(reportDesignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportDesignDTO> findOne(Long id) {
        log.debug("Request to get ReportDesign : {}", id);
        return reportDesignRepository.findOneWithEagerRelationships(id).map(reportDesignMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportDesign : {}", id);
        reportDesignRepository.deleteById(id);
        reportDesignSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportDesignDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportDesigns for query {}", query);
        return reportDesignSearchRepository.search(query, pageable).map(reportDesignMapper::toDto);
    }
}
