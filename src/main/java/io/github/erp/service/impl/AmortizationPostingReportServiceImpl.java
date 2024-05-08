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

import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.repository.AmortizationPostingReportRepository;
import io.github.erp.repository.search.AmortizationPostingReportSearchRepository;
import io.github.erp.service.AmortizationPostingReportService;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.mapper.AmortizationPostingReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AmortizationPostingReport}.
 */
@Service
@Transactional
public class AmortizationPostingReportServiceImpl implements AmortizationPostingReportService {

    private final Logger log = LoggerFactory.getLogger(AmortizationPostingReportServiceImpl.class);

    private final AmortizationPostingReportRepository amortizationPostingReportRepository;

    private final AmortizationPostingReportMapper amortizationPostingReportMapper;

    private final AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository;

    public AmortizationPostingReportServiceImpl(
        AmortizationPostingReportRepository amortizationPostingReportRepository,
        AmortizationPostingReportMapper amortizationPostingReportMapper,
        AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository
    ) {
        this.amortizationPostingReportRepository = amortizationPostingReportRepository;
        this.amortizationPostingReportMapper = amortizationPostingReportMapper;
        this.amortizationPostingReportSearchRepository = amortizationPostingReportSearchRepository;
    }

    @Override
    public AmortizationPostingReportDTO save(AmortizationPostingReportDTO amortizationPostingReportDTO) {
        log.debug("Request to save AmortizationPostingReport : {}", amortizationPostingReportDTO);
        AmortizationPostingReport amortizationPostingReport = amortizationPostingReportMapper.toEntity(amortizationPostingReportDTO);
        amortizationPostingReport = amortizationPostingReportRepository.save(amortizationPostingReport);
        AmortizationPostingReportDTO result = amortizationPostingReportMapper.toDto(amortizationPostingReport);
        amortizationPostingReportSearchRepository.save(amortizationPostingReport);
        return result;
    }

    @Override
    public Optional<AmortizationPostingReportDTO> partialUpdate(AmortizationPostingReportDTO amortizationPostingReportDTO) {
        log.debug("Request to partially update AmortizationPostingReport : {}", amortizationPostingReportDTO);

        return amortizationPostingReportRepository
            .findById(amortizationPostingReportDTO.getId())
            .map(existingAmortizationPostingReport -> {
                amortizationPostingReportMapper.partialUpdate(existingAmortizationPostingReport, amortizationPostingReportDTO);

                return existingAmortizationPostingReport;
            })
            .map(amortizationPostingReportRepository::save)
            .map(savedAmortizationPostingReport -> {
                amortizationPostingReportSearchRepository.save(savedAmortizationPostingReport);

                return savedAmortizationPostingReport;
            })
            .map(amortizationPostingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationPostingReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmortizationPostingReports");
        return amortizationPostingReportRepository.findAll(pageable).map(amortizationPostingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmortizationPostingReportDTO> findOne(Long id) {
        log.debug("Request to get AmortizationPostingReport : {}", id);
        return amortizationPostingReportRepository.findById(id).map(amortizationPostingReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmortizationPostingReport : {}", id);
        amortizationPostingReportRepository.deleteById(id);
        amortizationPostingReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationPostingReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmortizationPostingReports for query {}", query);
        return amortizationPostingReportSearchRepository.search(query, pageable).map(amortizationPostingReportMapper::toDto);
    }
}
