package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

import io.github.erp.domain.RouMonthlyDepreciationReport;
import io.github.erp.repository.RouMonthlyDepreciationReportRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportSearchRepository;
import io.github.erp.service.RouMonthlyDepreciationReportService;
import io.github.erp.service.dto.RouMonthlyDepreciationReportDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouMonthlyDepreciationReport}.
 */
@Service
@Transactional
public class RouMonthlyDepreciationReportServiceImpl implements RouMonthlyDepreciationReportService {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportServiceImpl.class);

    private final RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository;

    private final RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper;

    private final RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository;

    public RouMonthlyDepreciationReportServiceImpl(
        RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository,
        RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper,
        RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository
    ) {
        this.rouMonthlyDepreciationReportRepository = rouMonthlyDepreciationReportRepository;
        this.rouMonthlyDepreciationReportMapper = rouMonthlyDepreciationReportMapper;
        this.rouMonthlyDepreciationReportSearchRepository = rouMonthlyDepreciationReportSearchRepository;
    }

    @Override
    public RouMonthlyDepreciationReportDTO save(RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO) {
        log.debug("Request to save RouMonthlyDepreciationReport : {}", rouMonthlyDepreciationReportDTO);
        RouMonthlyDepreciationReport rouMonthlyDepreciationReport = rouMonthlyDepreciationReportMapper.toEntity(
            rouMonthlyDepreciationReportDTO
        );
        rouMonthlyDepreciationReport = rouMonthlyDepreciationReportRepository.save(rouMonthlyDepreciationReport);
        RouMonthlyDepreciationReportDTO result = rouMonthlyDepreciationReportMapper.toDto(rouMonthlyDepreciationReport);
        rouMonthlyDepreciationReportSearchRepository.save(rouMonthlyDepreciationReport);
        return result;
    }

    @Override
    public Optional<RouMonthlyDepreciationReportDTO> partialUpdate(RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO) {
        log.debug("Request to partially update RouMonthlyDepreciationReport : {}", rouMonthlyDepreciationReportDTO);

        return rouMonthlyDepreciationReportRepository
            .findById(rouMonthlyDepreciationReportDTO.getId())
            .map(existingRouMonthlyDepreciationReport -> {
                rouMonthlyDepreciationReportMapper.partialUpdate(existingRouMonthlyDepreciationReport, rouMonthlyDepreciationReportDTO);

                return existingRouMonthlyDepreciationReport;
            })
            .map(rouMonthlyDepreciationReportRepository::save)
            .map(savedRouMonthlyDepreciationReport -> {
                rouMonthlyDepreciationReportSearchRepository.save(savedRouMonthlyDepreciationReport);

                return savedRouMonthlyDepreciationReport;
            })
            .map(rouMonthlyDepreciationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouMonthlyDepreciationReports");
        return rouMonthlyDepreciationReportRepository.findAll(pageable).map(rouMonthlyDepreciationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouMonthlyDepreciationReportDTO> findOne(Long id) {
        log.debug("Request to get RouMonthlyDepreciationReport : {}", id);
        return rouMonthlyDepreciationReportRepository.findById(id).map(rouMonthlyDepreciationReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouMonthlyDepreciationReport : {}", id);
        rouMonthlyDepreciationReportRepository.deleteById(id);
        rouMonthlyDepreciationReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouMonthlyDepreciationReports for query {}", query);
        return rouMonthlyDepreciationReportSearchRepository.search(query, pageable).map(rouMonthlyDepreciationReportMapper::toDto);
    }
}
