package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.5.0
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
