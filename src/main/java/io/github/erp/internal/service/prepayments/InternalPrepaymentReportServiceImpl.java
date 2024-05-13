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
import io.github.erp.domain.PrepaymentReport;
import io.github.erp.internal.report.autonomousReport._maps.PrepaymentReportTupleMapper;
import io.github.erp.internal.repository.InternalPrepaymentReportRepository;
import io.github.erp.repository.search.PrepaymentReportSearchRepository;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.mapper.PrepaymentReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PrepaymentReport}.
 */
@Service
@Transactional
public class InternalPrepaymentReportServiceImpl implements InternalPrepaymentReportService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentReportServiceImpl.class);

    private final InternalPrepaymentReportRepository prepaymentReportRepository;

    private final PrepaymentReportMapper prepaymentReportMapper;

    private final PrepaymentReportSearchRepository prepaymentReportSearchRepository;

    private final PrepaymentReportTupleMapper prepaymentReportTupleMapper;

    public InternalPrepaymentReportServiceImpl(
        InternalPrepaymentReportRepository prepaymentReportRepository,
        PrepaymentReportMapper prepaymentReportMapper,
        PrepaymentReportSearchRepository prepaymentReportSearchRepository,
        PrepaymentReportTupleMapper prepaymentReportTupleMapper) {
        this.prepaymentReportRepository = prepaymentReportRepository;
        this.prepaymentReportMapper = prepaymentReportMapper;
        this.prepaymentReportSearchRepository = prepaymentReportSearchRepository;
        this.prepaymentReportTupleMapper = prepaymentReportTupleMapper;
    }

    @Override
    public PrepaymentReportDTO save(PrepaymentReportDTO prepaymentReportDTO) {
        log.debug("Request to save PrepaymentReport : {}", prepaymentReportDTO);
        PrepaymentReport prepaymentReport = prepaymentReportMapper.toEntity(prepaymentReportDTO);
        prepaymentReport = prepaymentReportRepository.save(prepaymentReport);
        PrepaymentReportDTO result = prepaymentReportMapper.toDto(prepaymentReport);
        prepaymentReportSearchRepository.save(prepaymentReport);
        return result;
    }

    @Override
    public Optional<PrepaymentReportDTO> partialUpdate(PrepaymentReportDTO prepaymentReportDTO) {
        log.debug("Request to partially update PrepaymentReport : {}", prepaymentReportDTO);

        return prepaymentReportRepository
            .findById(prepaymentReportDTO.getId())
            .map(existingPrepaymentReport -> {
                prepaymentReportMapper.partialUpdate(existingPrepaymentReport, prepaymentReportDTO);

                return existingPrepaymentReport;
            })
            .map(prepaymentReportRepository::save)
            .map(savedPrepaymentReport -> {
                prepaymentReportSearchRepository.save(savedPrepaymentReport);

                return savedPrepaymentReport;
            })
            .map(prepaymentReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentReports");
        return prepaymentReportRepository.findAll(pageable).map(prepaymentReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentReportDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentReport : {}", id);
        return prepaymentReportRepository.findById(id).map(prepaymentReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentReport : {}", id);
        prepaymentReportRepository.deleteById(id);
        prepaymentReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentReports for query {}", query);
        return prepaymentReportSearchRepository.search(query, pageable).map(prepaymentReportMapper::toDto);
    }

    /**
     * Returns report items calculated as at the reportDate parameter. The same do not have
     * parameters for page as the expectation is to derive the entire list
     *
     * @param reportDate This is the date of the report as at which the prepayment report is including
     *                   prepayment-account items and amortizing the same
     * @return Optional list of prepayment-report items with calculated outstanding values
     */
    @Override
    public Optional<List<PrepaymentReportDTO>> getReportListByReportDate(LocalDate reportDate) {
        return prepaymentReportRepository.findAllByReportDate(reportDate)
            .map(prepaymentReportTupleMapper::toValue2);
    }
}
