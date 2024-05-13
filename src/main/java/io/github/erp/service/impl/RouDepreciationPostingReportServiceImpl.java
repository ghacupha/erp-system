package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.RouDepreciationPostingReport;
import io.github.erp.repository.RouDepreciationPostingReportRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportSearchRepository;
import io.github.erp.service.RouDepreciationPostingReportService;
import io.github.erp.service.dto.RouDepreciationPostingReportDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationPostingReport}.
 */
@Service
@Transactional
public class RouDepreciationPostingReportServiceImpl implements RouDepreciationPostingReportService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationPostingReportServiceImpl.class);

    private final RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository;

    private final RouDepreciationPostingReportMapper rouDepreciationPostingReportMapper;

    private final RouDepreciationPostingReportSearchRepository rouDepreciationPostingReportSearchRepository;

    public RouDepreciationPostingReportServiceImpl(
        RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository,
        RouDepreciationPostingReportMapper rouDepreciationPostingReportMapper,
        RouDepreciationPostingReportSearchRepository rouDepreciationPostingReportSearchRepository
    ) {
        this.rouDepreciationPostingReportRepository = rouDepreciationPostingReportRepository;
        this.rouDepreciationPostingReportMapper = rouDepreciationPostingReportMapper;
        this.rouDepreciationPostingReportSearchRepository = rouDepreciationPostingReportSearchRepository;
    }

    @Override
    public RouDepreciationPostingReportDTO save(RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO) {
        log.debug("Request to save RouDepreciationPostingReport : {}", rouDepreciationPostingReportDTO);
        RouDepreciationPostingReport rouDepreciationPostingReport = rouDepreciationPostingReportMapper.toEntity(
            rouDepreciationPostingReportDTO
        );
        rouDepreciationPostingReport = rouDepreciationPostingReportRepository.save(rouDepreciationPostingReport);
        RouDepreciationPostingReportDTO result = rouDepreciationPostingReportMapper.toDto(rouDepreciationPostingReport);
        rouDepreciationPostingReportSearchRepository.save(rouDepreciationPostingReport);
        return result;
    }

    @Override
    public Optional<RouDepreciationPostingReportDTO> partialUpdate(RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO) {
        log.debug("Request to partially update RouDepreciationPostingReport : {}", rouDepreciationPostingReportDTO);

        return rouDepreciationPostingReportRepository
            .findById(rouDepreciationPostingReportDTO.getId())
            .map(existingRouDepreciationPostingReport -> {
                rouDepreciationPostingReportMapper.partialUpdate(existingRouDepreciationPostingReport, rouDepreciationPostingReportDTO);

                return existingRouDepreciationPostingReport;
            })
            .map(rouDepreciationPostingReportRepository::save)
            .map(savedRouDepreciationPostingReport -> {
                rouDepreciationPostingReportSearchRepository.save(savedRouDepreciationPostingReport);

                return savedRouDepreciationPostingReport;
            })
            .map(rouDepreciationPostingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationPostingReports");
        return rouDepreciationPostingReportRepository.findAll(pageable).map(rouDepreciationPostingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationPostingReportDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationPostingReport : {}", id);
        return rouDepreciationPostingReportRepository.findById(id).map(rouDepreciationPostingReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationPostingReport : {}", id);
        rouDepreciationPostingReportRepository.deleteById(id);
        rouDepreciationPostingReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationPostingReports for query {}", query);
        return rouDepreciationPostingReportSearchRepository.search(query, pageable).map(rouDepreciationPostingReportMapper::toDto);
    }
}
