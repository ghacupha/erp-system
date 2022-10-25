package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
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
