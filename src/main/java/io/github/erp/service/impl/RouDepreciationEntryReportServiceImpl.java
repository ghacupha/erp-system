package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.domain.RouDepreciationEntryReport;
import io.github.erp.repository.RouDepreciationEntryReportRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportSearchRepository;
import io.github.erp.service.RouDepreciationEntryReportService;
import io.github.erp.service.dto.RouDepreciationEntryReportDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationEntryReport}.
 */
@Service
@Transactional
public class RouDepreciationEntryReportServiceImpl implements RouDepreciationEntryReportService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportServiceImpl.class);

    private final RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository;

    private final RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper;

    private final RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository;

    public RouDepreciationEntryReportServiceImpl(
        RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository,
        RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper,
        RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository
    ) {
        this.rouDepreciationEntryReportRepository = rouDepreciationEntryReportRepository;
        this.rouDepreciationEntryReportMapper = rouDepreciationEntryReportMapper;
        this.rouDepreciationEntryReportSearchRepository = rouDepreciationEntryReportSearchRepository;
    }

    @Override
    public RouDepreciationEntryReportDTO save(RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO) {
        log.debug("Request to save RouDepreciationEntryReport : {}", rouDepreciationEntryReportDTO);
        RouDepreciationEntryReport rouDepreciationEntryReport = rouDepreciationEntryReportMapper.toEntity(rouDepreciationEntryReportDTO);
        rouDepreciationEntryReport = rouDepreciationEntryReportRepository.save(rouDepreciationEntryReport);
        RouDepreciationEntryReportDTO result = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);
        rouDepreciationEntryReportSearchRepository.save(rouDepreciationEntryReport);
        return result;
    }

    @Override
    public Optional<RouDepreciationEntryReportDTO> partialUpdate(RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO) {
        log.debug("Request to partially update RouDepreciationEntryReport : {}", rouDepreciationEntryReportDTO);

        return rouDepreciationEntryReportRepository
            .findById(rouDepreciationEntryReportDTO.getId())
            .map(existingRouDepreciationEntryReport -> {
                rouDepreciationEntryReportMapper.partialUpdate(existingRouDepreciationEntryReport, rouDepreciationEntryReportDTO);

                return existingRouDepreciationEntryReport;
            })
            .map(rouDepreciationEntryReportRepository::save)
            .map(savedRouDepreciationEntryReport -> {
                rouDepreciationEntryReportSearchRepository.save(savedRouDepreciationEntryReport);

                return savedRouDepreciationEntryReport;
            })
            .map(rouDepreciationEntryReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationEntryReports");
        return rouDepreciationEntryReportRepository.findAll(pageable).map(rouDepreciationEntryReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationEntryReportDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationEntryReport : {}", id);
        return rouDepreciationEntryReportRepository.findById(id).map(rouDepreciationEntryReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationEntryReport : {}", id);
        rouDepreciationEntryReportRepository.deleteById(id);
        rouDepreciationEntryReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationEntryReports for query {}", query);
        return rouDepreciationEntryReportSearchRepository.search(query, pageable).map(rouDepreciationEntryReportMapper::toDto);
    }
}
