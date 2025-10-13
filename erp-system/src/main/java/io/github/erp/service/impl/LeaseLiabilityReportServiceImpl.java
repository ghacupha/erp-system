package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.LeaseLiabilityReport;
import io.github.erp.repository.LeaseLiabilityReportRepository;
import io.github.erp.repository.search.LeaseLiabilityReportSearchRepository;
import io.github.erp.service.LeaseLiabilityReportService;
import io.github.erp.service.dto.LeaseLiabilityReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseLiabilityReport}.
 */
@Service
@Transactional
public class LeaseLiabilityReportServiceImpl implements LeaseLiabilityReportService {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityReportServiceImpl.class);

    private final LeaseLiabilityReportRepository leaseLiabilityReportRepository;

    private final LeaseLiabilityReportMapper leaseLiabilityReportMapper;

    private final LeaseLiabilityReportSearchRepository leaseLiabilityReportSearchRepository;

    public LeaseLiabilityReportServiceImpl(
        LeaseLiabilityReportRepository leaseLiabilityReportRepository,
        LeaseLiabilityReportMapper leaseLiabilityReportMapper,
        LeaseLiabilityReportSearchRepository leaseLiabilityReportSearchRepository
    ) {
        this.leaseLiabilityReportRepository = leaseLiabilityReportRepository;
        this.leaseLiabilityReportMapper = leaseLiabilityReportMapper;
        this.leaseLiabilityReportSearchRepository = leaseLiabilityReportSearchRepository;
    }

    @Override
    public LeaseLiabilityReportDTO save(LeaseLiabilityReportDTO leaseLiabilityReportDTO) {
        log.debug("Request to save LeaseLiabilityReport : {}", leaseLiabilityReportDTO);
        LeaseLiabilityReport leaseLiabilityReport = leaseLiabilityReportMapper.toEntity(leaseLiabilityReportDTO);
        leaseLiabilityReport = leaseLiabilityReportRepository.save(leaseLiabilityReport);
        LeaseLiabilityReportDTO result = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);
        leaseLiabilityReportSearchRepository.save(leaseLiabilityReport);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityReportDTO> partialUpdate(LeaseLiabilityReportDTO leaseLiabilityReportDTO) {
        log.debug("Request to partially update LeaseLiabilityReport : {}", leaseLiabilityReportDTO);

        return leaseLiabilityReportRepository
            .findById(leaseLiabilityReportDTO.getId())
            .map(existingLeaseLiabilityReport -> {
                leaseLiabilityReportMapper.partialUpdate(existingLeaseLiabilityReport, leaseLiabilityReportDTO);

                return existingLeaseLiabilityReport;
            })
            .map(leaseLiabilityReportRepository::save)
            .map(savedLeaseLiabilityReport -> {
                leaseLiabilityReportSearchRepository.save(savedLeaseLiabilityReport);

                return savedLeaseLiabilityReport;
            })
            .map(leaseLiabilityReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityReports");
        return leaseLiabilityReportRepository.findAll(pageable).map(leaseLiabilityReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityReportDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityReport : {}", id);
        return leaseLiabilityReportRepository.findById(id).map(leaseLiabilityReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityReport : {}", id);
        leaseLiabilityReportRepository.deleteById(id);
        leaseLiabilityReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityReports for query {}", query);
        return leaseLiabilityReportSearchRepository.search(query, pageable).map(leaseLiabilityReportMapper::toDto);
    }
}
