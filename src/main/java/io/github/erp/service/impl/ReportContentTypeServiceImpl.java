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

import io.github.erp.domain.ReportContentType;
import io.github.erp.repository.ReportContentTypeRepository;
import io.github.erp.repository.search.ReportContentTypeSearchRepository;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.dto.ReportContentTypeDTO;
import io.github.erp.service.mapper.ReportContentTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportContentType}.
 */
@Service
@Transactional
public class ReportContentTypeServiceImpl implements ReportContentTypeService {

    private final Logger log = LoggerFactory.getLogger(ReportContentTypeServiceImpl.class);

    private final ReportContentTypeRepository reportContentTypeRepository;

    private final ReportContentTypeMapper reportContentTypeMapper;

    private final ReportContentTypeSearchRepository reportContentTypeSearchRepository;

    public ReportContentTypeServiceImpl(
        ReportContentTypeRepository reportContentTypeRepository,
        ReportContentTypeMapper reportContentTypeMapper,
        ReportContentTypeSearchRepository reportContentTypeSearchRepository
    ) {
        this.reportContentTypeRepository = reportContentTypeRepository;
        this.reportContentTypeMapper = reportContentTypeMapper;
        this.reportContentTypeSearchRepository = reportContentTypeSearchRepository;
    }

    @Override
    public ReportContentTypeDTO save(ReportContentTypeDTO reportContentTypeDTO) {
        log.debug("Request to save ReportContentType : {}", reportContentTypeDTO);
        ReportContentType reportContentType = reportContentTypeMapper.toEntity(reportContentTypeDTO);
        reportContentType = reportContentTypeRepository.save(reportContentType);
        ReportContentTypeDTO result = reportContentTypeMapper.toDto(reportContentType);
        reportContentTypeSearchRepository.save(reportContentType);
        return result;
    }

    @Override
    public Optional<ReportContentTypeDTO> partialUpdate(ReportContentTypeDTO reportContentTypeDTO) {
        log.debug("Request to partially update ReportContentType : {}", reportContentTypeDTO);

        return reportContentTypeRepository
            .findById(reportContentTypeDTO.getId())
            .map(existingReportContentType -> {
                reportContentTypeMapper.partialUpdate(existingReportContentType, reportContentTypeDTO);

                return existingReportContentType;
            })
            .map(reportContentTypeRepository::save)
            .map(savedReportContentType -> {
                reportContentTypeSearchRepository.save(savedReportContentType);

                return savedReportContentType;
            })
            .map(reportContentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportContentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportContentTypes");
        return reportContentTypeRepository.findAll(pageable).map(reportContentTypeMapper::toDto);
    }

    public Page<ReportContentTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportContentTypeRepository.findAllWithEagerRelationships(pageable).map(reportContentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportContentTypeDTO> findOne(Long id) {
        log.debug("Request to get ReportContentType : {}", id);
        return reportContentTypeRepository.findOneWithEagerRelationships(id).map(reportContentTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportContentType : {}", id);
        reportContentTypeRepository.deleteById(id);
        reportContentTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportContentTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReportContentTypes for query {}", query);
        return reportContentTypeSearchRepository.search(query, pageable).map(reportContentTypeMapper::toDto);
    }
}
