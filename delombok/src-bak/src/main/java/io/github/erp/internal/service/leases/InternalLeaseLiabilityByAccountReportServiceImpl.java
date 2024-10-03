package io.github.erp.internal.service.leases;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.domain.LeaseLiabilityByAccountReport;
import io.github.erp.repository.LeaseLiabilityByAccountReportRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportSearchRepository;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityByAccountReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseLiabilityByAccountReport}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityByAccountReportServiceImpl implements InternalLeaseLiabilityByAccountReportService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityByAccountReportServiceImpl.class);

    private final LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository;

    private final LeaseLiabilityByAccountReportMapper leaseLiabilityByAccountReportMapper;

    private final LeaseLiabilityByAccountReportSearchRepository leaseLiabilityByAccountReportSearchRepository;

    public InternalLeaseLiabilityByAccountReportServiceImpl(
        LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository,
        LeaseLiabilityByAccountReportMapper leaseLiabilityByAccountReportMapper,
        LeaseLiabilityByAccountReportSearchRepository leaseLiabilityByAccountReportSearchRepository
    ) {
        this.leaseLiabilityByAccountReportRepository = leaseLiabilityByAccountReportRepository;
        this.leaseLiabilityByAccountReportMapper = leaseLiabilityByAccountReportMapper;
        this.leaseLiabilityByAccountReportSearchRepository = leaseLiabilityByAccountReportSearchRepository;
    }

    @Override
    public LeaseLiabilityByAccountReportDTO save(LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO) {
        log.debug("Request to save LeaseLiabilityByAccountReport : {}", leaseLiabilityByAccountReportDTO);
        LeaseLiabilityByAccountReport leaseLiabilityByAccountReport = leaseLiabilityByAccountReportMapper.toEntity(
            leaseLiabilityByAccountReportDTO
        );
        leaseLiabilityByAccountReport = leaseLiabilityByAccountReportRepository.save(leaseLiabilityByAccountReport);
        LeaseLiabilityByAccountReportDTO result = leaseLiabilityByAccountReportMapper.toDto(leaseLiabilityByAccountReport);
        leaseLiabilityByAccountReportSearchRepository.save(leaseLiabilityByAccountReport);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityByAccountReportDTO> partialUpdate(LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO) {
        log.debug("Request to partially update LeaseLiabilityByAccountReport : {}", leaseLiabilityByAccountReportDTO);

        return leaseLiabilityByAccountReportRepository
            .findById(leaseLiabilityByAccountReportDTO.getId())
            .map(existingLeaseLiabilityByAccountReport -> {
                leaseLiabilityByAccountReportMapper.partialUpdate(existingLeaseLiabilityByAccountReport, leaseLiabilityByAccountReportDTO);

                return existingLeaseLiabilityByAccountReport;
            })
            .map(leaseLiabilityByAccountReportRepository::save)
            .map(savedLeaseLiabilityByAccountReport -> {
                leaseLiabilityByAccountReportSearchRepository.save(savedLeaseLiabilityByAccountReport);

                return savedLeaseLiabilityByAccountReport;
            })
            .map(leaseLiabilityByAccountReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityByAccountReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityByAccountReports");
        return leaseLiabilityByAccountReportRepository.findAll(pageable).map(leaseLiabilityByAccountReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityByAccountReportDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityByAccountReport : {}", id);
        return leaseLiabilityByAccountReportRepository.findById(id).map(leaseLiabilityByAccountReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityByAccountReport : {}", id);
        leaseLiabilityByAccountReportRepository.deleteById(id);
        leaseLiabilityByAccountReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityByAccountReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityByAccountReports for query {}", query);
        return leaseLiabilityByAccountReportSearchRepository.search(query, pageable).map(leaseLiabilityByAccountReportMapper::toDto);
    }
}
