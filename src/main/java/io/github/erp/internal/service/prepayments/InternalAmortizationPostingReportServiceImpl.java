package io.github.erp.internal.service.prepayments;

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
import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalAmortizationPostingReportRepository;
import io.github.erp.repository.search.AmortizationPostingReportSearchRepository;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.mapper.AmortizationPostingReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AmortizationPostingReport}.
 */
@Service
@Transactional
public class InternalAmortizationPostingReportServiceImpl implements InternalAmortizationPostingReportService {

    private final Logger log = LoggerFactory.getLogger(InternalAmortizationPostingReportServiceImpl.class);

    private final InternalAmortizationPostingReportRepository amortizationPostingReportRepository;

    private final AmortizationPostingReportMapper amortizationPostingReportMapper;

    private final AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository;

    private final Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> amortizationPostingInternalMapping;

    public InternalAmortizationPostingReportServiceImpl(
        InternalAmortizationPostingReportRepository amortizationPostingReportRepository,
        AmortizationPostingReportMapper amortizationPostingReportMapper,
        AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository,
        Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> amortizationPostingInternalMapping) {
        this.amortizationPostingReportRepository = amortizationPostingReportRepository;
        this.amortizationPostingReportMapper = amortizationPostingReportMapper;
        this.amortizationPostingReportSearchRepository = amortizationPostingReportSearchRepository;
        this.amortizationPostingInternalMapping = amortizationPostingInternalMapping;
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

    /**
     * Seek to find a list of amortization posting entries due on a particular date and in
     * particular such a date is determined by the configured last date of the amortization
     * period.All items in the
     *
     * @param reportDate This is the reportDate parameter on which date the system is to have
     *                   posted entries for amortization
     * @return List of instances whose end of amortization period aligns with the query date
     */
    @Override
    public Optional<List<AmortizationPostingReportDTO>> findAllByReportDate(LocalDate reportDate) {
        log.debug("Request to find all amortization entries whose amortization period end date is {}", reportDate);

        return amortizationPostingReportRepository.findByAllReportDate(reportDate)
            .map(amortizationPostingInternalMapping::toValue2);
    }
}
