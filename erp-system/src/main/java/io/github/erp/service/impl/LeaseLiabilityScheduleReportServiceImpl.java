package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.LeaseLiabilityScheduleReport;
import io.github.erp.repository.LeaseLiabilityScheduleReportRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportSearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleReportService;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseLiabilityScheduleReport}.
 */
@Service
@Transactional
public class LeaseLiabilityScheduleReportServiceImpl implements LeaseLiabilityScheduleReportService {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleReportServiceImpl.class);

    private final LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository;

    private final LeaseLiabilityScheduleReportMapper leaseLiabilityScheduleReportMapper;

    private final LeaseLiabilityScheduleReportSearchRepository leaseLiabilityScheduleReportSearchRepository;

    public LeaseLiabilityScheduleReportServiceImpl(
        LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository,
        LeaseLiabilityScheduleReportMapper leaseLiabilityScheduleReportMapper,
        LeaseLiabilityScheduleReportSearchRepository leaseLiabilityScheduleReportSearchRepository
    ) {
        this.leaseLiabilityScheduleReportRepository = leaseLiabilityScheduleReportRepository;
        this.leaseLiabilityScheduleReportMapper = leaseLiabilityScheduleReportMapper;
        this.leaseLiabilityScheduleReportSearchRepository = leaseLiabilityScheduleReportSearchRepository;
    }

    @Override
    public LeaseLiabilityScheduleReportDTO save(LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO) {
        log.debug("Request to save LeaseLiabilityScheduleReport : {}", leaseLiabilityScheduleReportDTO);
        LeaseLiabilityScheduleReport leaseLiabilityScheduleReport = leaseLiabilityScheduleReportMapper.toEntity(
            leaseLiabilityScheduleReportDTO
        );
        leaseLiabilityScheduleReport = leaseLiabilityScheduleReportRepository.save(leaseLiabilityScheduleReport);
        LeaseLiabilityScheduleReportDTO result = leaseLiabilityScheduleReportMapper.toDto(leaseLiabilityScheduleReport);
        leaseLiabilityScheduleReportSearchRepository.save(leaseLiabilityScheduleReport);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityScheduleReportDTO> partialUpdate(LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO) {
        log.debug("Request to partially update LeaseLiabilityScheduleReport : {}", leaseLiabilityScheduleReportDTO);

        return leaseLiabilityScheduleReportRepository
            .findById(leaseLiabilityScheduleReportDTO.getId())
            .map(existingLeaseLiabilityScheduleReport -> {
                leaseLiabilityScheduleReportMapper.partialUpdate(existingLeaseLiabilityScheduleReport, leaseLiabilityScheduleReportDTO);

                return existingLeaseLiabilityScheduleReport;
            })
            .map(leaseLiabilityScheduleReportRepository::save)
            .map(savedLeaseLiabilityScheduleReport -> {
                leaseLiabilityScheduleReportSearchRepository.save(savedLeaseLiabilityScheduleReport);

                return savedLeaseLiabilityScheduleReport;
            })
            .map(leaseLiabilityScheduleReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityScheduleReports");
        return leaseLiabilityScheduleReportRepository.findAll(pageable).map(leaseLiabilityScheduleReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityScheduleReportDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityScheduleReport : {}", id);
        return leaseLiabilityScheduleReportRepository.findById(id).map(leaseLiabilityScheduleReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityScheduleReport : {}", id);
        leaseLiabilityScheduleReportRepository.deleteById(id);
        leaseLiabilityScheduleReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityScheduleReports for query {}", query);
        return leaseLiabilityScheduleReportSearchRepository.search(query, pageable).map(leaseLiabilityScheduleReportMapper::toDto);
    }
}
