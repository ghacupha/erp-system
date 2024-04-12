package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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

import io.github.erp.domain.RouAssetNBVReport;
import io.github.erp.repository.RouAssetNBVReportRepository;
import io.github.erp.repository.search.RouAssetNBVReportSearchRepository;
import io.github.erp.service.RouAssetNBVReportService;
import io.github.erp.service.dto.RouAssetNBVReportDTO;
import io.github.erp.service.mapper.RouAssetNBVReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouAssetNBVReport}.
 */
@Service
@Transactional
public class RouAssetNBVReportServiceImpl implements RouAssetNBVReportService {

    private final Logger log = LoggerFactory.getLogger(RouAssetNBVReportServiceImpl.class);

    private final RouAssetNBVReportRepository rouAssetNBVReportRepository;

    private final RouAssetNBVReportMapper rouAssetNBVReportMapper;

    private final RouAssetNBVReportSearchRepository rouAssetNBVReportSearchRepository;

    public RouAssetNBVReportServiceImpl(
        RouAssetNBVReportRepository rouAssetNBVReportRepository,
        RouAssetNBVReportMapper rouAssetNBVReportMapper,
        RouAssetNBVReportSearchRepository rouAssetNBVReportSearchRepository
    ) {
        this.rouAssetNBVReportRepository = rouAssetNBVReportRepository;
        this.rouAssetNBVReportMapper = rouAssetNBVReportMapper;
        this.rouAssetNBVReportSearchRepository = rouAssetNBVReportSearchRepository;
    }

    @Override
    public RouAssetNBVReportDTO save(RouAssetNBVReportDTO rouAssetNBVReportDTO) {
        log.debug("Request to save RouAssetNBVReport : {}", rouAssetNBVReportDTO);
        RouAssetNBVReport rouAssetNBVReport = rouAssetNBVReportMapper.toEntity(rouAssetNBVReportDTO);
        rouAssetNBVReport = rouAssetNBVReportRepository.save(rouAssetNBVReport);
        RouAssetNBVReportDTO result = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);
        rouAssetNBVReportSearchRepository.save(rouAssetNBVReport);
        return result;
    }

    @Override
    public Optional<RouAssetNBVReportDTO> partialUpdate(RouAssetNBVReportDTO rouAssetNBVReportDTO) {
        log.debug("Request to partially update RouAssetNBVReport : {}", rouAssetNBVReportDTO);

        return rouAssetNBVReportRepository
            .findById(rouAssetNBVReportDTO.getId())
            .map(existingRouAssetNBVReport -> {
                rouAssetNBVReportMapper.partialUpdate(existingRouAssetNBVReport, rouAssetNBVReportDTO);

                return existingRouAssetNBVReport;
            })
            .map(rouAssetNBVReportRepository::save)
            .map(savedRouAssetNBVReport -> {
                rouAssetNBVReportSearchRepository.save(savedRouAssetNBVReport);

                return savedRouAssetNBVReport;
            })
            .map(rouAssetNBVReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAssetNBVReports");
        return rouAssetNBVReportRepository.findAll(pageable).map(rouAssetNBVReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAssetNBVReportDTO> findOne(Long id) {
        log.debug("Request to get RouAssetNBVReport : {}", id);
        return rouAssetNBVReportRepository.findById(id).map(rouAssetNBVReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAssetNBVReport : {}", id);
        rouAssetNBVReportRepository.deleteById(id);
        rouAssetNBVReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAssetNBVReports for query {}", query);
        return rouAssetNBVReportSearchRepository.search(query, pageable).map(rouAssetNBVReportMapper::toDto);
    }
}
