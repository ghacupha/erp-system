package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.domain.RouAssetListReport;
import io.github.erp.repository.RouAssetListReportRepository;
import io.github.erp.repository.search.RouAssetListReportSearchRepository;
import io.github.erp.service.RouAssetListReportService;
import io.github.erp.service.dto.RouAssetListReportDTO;
import io.github.erp.service.mapper.RouAssetListReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouAssetListReport}.
 */
@Service
@Transactional
public class RouAssetListReportServiceImpl implements RouAssetListReportService {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportServiceImpl.class);

    private final RouAssetListReportRepository rouAssetListReportRepository;

    private final RouAssetListReportMapper rouAssetListReportMapper;

    private final RouAssetListReportSearchRepository rouAssetListReportSearchRepository;

    public RouAssetListReportServiceImpl(
        RouAssetListReportRepository rouAssetListReportRepository,
        RouAssetListReportMapper rouAssetListReportMapper,
        RouAssetListReportSearchRepository rouAssetListReportSearchRepository
    ) {
        this.rouAssetListReportRepository = rouAssetListReportRepository;
        this.rouAssetListReportMapper = rouAssetListReportMapper;
        this.rouAssetListReportSearchRepository = rouAssetListReportSearchRepository;
    }

    @Override
    public RouAssetListReportDTO save(RouAssetListReportDTO rouAssetListReportDTO) {
        log.debug("Request to save RouAssetListReport : {}", rouAssetListReportDTO);
        RouAssetListReport rouAssetListReport = rouAssetListReportMapper.toEntity(rouAssetListReportDTO);
        rouAssetListReport = rouAssetListReportRepository.save(rouAssetListReport);
        RouAssetListReportDTO result = rouAssetListReportMapper.toDto(rouAssetListReport);
        rouAssetListReportSearchRepository.save(rouAssetListReport);
        return result;
    }

    @Override
    public Optional<RouAssetListReportDTO> partialUpdate(RouAssetListReportDTO rouAssetListReportDTO) {
        log.debug("Request to partially update RouAssetListReport : {}", rouAssetListReportDTO);

        return rouAssetListReportRepository
            .findById(rouAssetListReportDTO.getId())
            .map(existingRouAssetListReport -> {
                rouAssetListReportMapper.partialUpdate(existingRouAssetListReport, rouAssetListReportDTO);

                return existingRouAssetListReport;
            })
            .map(rouAssetListReportRepository::save)
            .map(savedRouAssetListReport -> {
                rouAssetListReportSearchRepository.save(savedRouAssetListReport);

                return savedRouAssetListReport;
            })
            .map(rouAssetListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetListReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAssetListReports");
        return rouAssetListReportRepository.findAll(pageable).map(rouAssetListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAssetListReportDTO> findOne(Long id) {
        log.debug("Request to get RouAssetListReport : {}", id);
        return rouAssetListReportRepository.findById(id).map(rouAssetListReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAssetListReport : {}", id);
        rouAssetListReportRepository.deleteById(id);
        rouAssetListReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetListReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAssetListReports for query {}", query);
        return rouAssetListReportSearchRepository.search(query, pageable).map(rouAssetListReportMapper::toDto);
    }
}
