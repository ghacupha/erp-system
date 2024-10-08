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

import io.github.erp.domain.LeaseLiabilityPostingReport;
import io.github.erp.repository.LeaseLiabilityPostingReportRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportSearchRepository;
import io.github.erp.service.LeaseLiabilityPostingReportService;
import io.github.erp.service.dto.LeaseLiabilityPostingReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityPostingReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseLiabilityPostingReport}.
 */
@Service
@Transactional
public class LeaseLiabilityPostingReportServiceImpl implements LeaseLiabilityPostingReportService {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityPostingReportServiceImpl.class);

    private final LeaseLiabilityPostingReportRepository leaseLiabilityPostingReportRepository;

    private final LeaseLiabilityPostingReportMapper leaseLiabilityPostingReportMapper;

    private final LeaseLiabilityPostingReportSearchRepository leaseLiabilityPostingReportSearchRepository;

    public LeaseLiabilityPostingReportServiceImpl(
        LeaseLiabilityPostingReportRepository leaseLiabilityPostingReportRepository,
        LeaseLiabilityPostingReportMapper leaseLiabilityPostingReportMapper,
        LeaseLiabilityPostingReportSearchRepository leaseLiabilityPostingReportSearchRepository
    ) {
        this.leaseLiabilityPostingReportRepository = leaseLiabilityPostingReportRepository;
        this.leaseLiabilityPostingReportMapper = leaseLiabilityPostingReportMapper;
        this.leaseLiabilityPostingReportSearchRepository = leaseLiabilityPostingReportSearchRepository;
    }

    @Override
    public LeaseLiabilityPostingReportDTO save(LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO) {
        log.debug("Request to save LeaseLiabilityPostingReport : {}", leaseLiabilityPostingReportDTO);
        LeaseLiabilityPostingReport leaseLiabilityPostingReport = leaseLiabilityPostingReportMapper.toEntity(
            leaseLiabilityPostingReportDTO
        );
        leaseLiabilityPostingReport = leaseLiabilityPostingReportRepository.save(leaseLiabilityPostingReport);
        LeaseLiabilityPostingReportDTO result = leaseLiabilityPostingReportMapper.toDto(leaseLiabilityPostingReport);
        leaseLiabilityPostingReportSearchRepository.save(leaseLiabilityPostingReport);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityPostingReportDTO> partialUpdate(LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO) {
        log.debug("Request to partially update LeaseLiabilityPostingReport : {}", leaseLiabilityPostingReportDTO);

        return leaseLiabilityPostingReportRepository
            .findById(leaseLiabilityPostingReportDTO.getId())
            .map(existingLeaseLiabilityPostingReport -> {
                leaseLiabilityPostingReportMapper.partialUpdate(existingLeaseLiabilityPostingReport, leaseLiabilityPostingReportDTO);

                return existingLeaseLiabilityPostingReport;
            })
            .map(leaseLiabilityPostingReportRepository::save)
            .map(savedLeaseLiabilityPostingReport -> {
                leaseLiabilityPostingReportSearchRepository.save(savedLeaseLiabilityPostingReport);

                return savedLeaseLiabilityPostingReport;
            })
            .map(leaseLiabilityPostingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityPostingReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityPostingReports");
        return leaseLiabilityPostingReportRepository.findAll(pageable).map(leaseLiabilityPostingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityPostingReportDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityPostingReport : {}", id);
        return leaseLiabilityPostingReportRepository.findById(id).map(leaseLiabilityPostingReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityPostingReport : {}", id);
        leaseLiabilityPostingReportRepository.deleteById(id);
        leaseLiabilityPostingReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityPostingReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityPostingReports for query {}", query);
        return leaseLiabilityPostingReportSearchRepository.search(query, pageable).map(leaseLiabilityPostingReportMapper::toDto);
    }
}
