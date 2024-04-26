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

import io.github.erp.domain.ExcelReportExport;
import io.github.erp.repository.ExcelReportExportRepository;
import io.github.erp.repository.search.ExcelReportExportSearchRepository;
import io.github.erp.service.ExcelReportExportService;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.mapper.ExcelReportExportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExcelReportExport}.
 */
@Service
@Transactional
public class ExcelReportExportServiceImpl implements ExcelReportExportService {

    private final Logger log = LoggerFactory.getLogger(ExcelReportExportServiceImpl.class);

    private final ExcelReportExportRepository excelReportExportRepository;

    private final ExcelReportExportMapper excelReportExportMapper;

    private final ExcelReportExportSearchRepository excelReportExportSearchRepository;

    public ExcelReportExportServiceImpl(
        ExcelReportExportRepository excelReportExportRepository,
        ExcelReportExportMapper excelReportExportMapper,
        ExcelReportExportSearchRepository excelReportExportSearchRepository
    ) {
        this.excelReportExportRepository = excelReportExportRepository;
        this.excelReportExportMapper = excelReportExportMapper;
        this.excelReportExportSearchRepository = excelReportExportSearchRepository;
    }

    @Override
    public ExcelReportExportDTO save(ExcelReportExportDTO excelReportExportDTO) {
        log.debug("Request to save ExcelReportExport : {}", excelReportExportDTO);
        ExcelReportExport excelReportExport = excelReportExportMapper.toEntity(excelReportExportDTO);
        excelReportExport = excelReportExportRepository.save(excelReportExport);
        ExcelReportExportDTO result = excelReportExportMapper.toDto(excelReportExport);
        excelReportExportSearchRepository.save(excelReportExport);
        return result;
    }

    @Override
    public Optional<ExcelReportExportDTO> partialUpdate(ExcelReportExportDTO excelReportExportDTO) {
        log.debug("Request to partially update ExcelReportExport : {}", excelReportExportDTO);

        return excelReportExportRepository
            .findById(excelReportExportDTO.getId())
            .map(existingExcelReportExport -> {
                excelReportExportMapper.partialUpdate(existingExcelReportExport, excelReportExportDTO);

                return existingExcelReportExport;
            })
            .map(excelReportExportRepository::save)
            .map(savedExcelReportExport -> {
                excelReportExportSearchRepository.save(savedExcelReportExport);

                return savedExcelReportExport;
            })
            .map(excelReportExportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExcelReportExportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExcelReportExports");
        return excelReportExportRepository.findAll(pageable).map(excelReportExportMapper::toDto);
    }

    public Page<ExcelReportExportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return excelReportExportRepository.findAllWithEagerRelationships(pageable).map(excelReportExportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExcelReportExportDTO> findOne(Long id) {
        log.debug("Request to get ExcelReportExport : {}", id);
        return excelReportExportRepository.findOneWithEagerRelationships(id).map(excelReportExportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExcelReportExport : {}", id);
        excelReportExportRepository.deleteById(id);
        excelReportExportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExcelReportExportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExcelReportExports for query {}", query);
        return excelReportExportSearchRepository.search(query, pageable).map(excelReportExportMapper::toDto);
    }
}
