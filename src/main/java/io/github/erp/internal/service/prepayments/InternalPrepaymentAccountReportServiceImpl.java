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
import io.github.erp.domain.PrepaymentAccountReport;
import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalPrepaymentAccountReportRepository;
import io.github.erp.repository.search.PrepaymentAccountReportSearchRepository;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportMapper;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentAccountReport}.
 */
@Service
@Transactional
public class InternalPrepaymentAccountReportServiceImpl implements InternalPrepaymentAccountReportService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentAccountReportServiceImpl.class);

    private final InternalPrepaymentAccountReportRepository prepaymentAccountReportRepository;

    private final PrepaymentAccountReportMapper prepaymentAccountReportMapper;

    private final PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository;

    private final Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> prepaymentAccountReportDTOMapping;

    public InternalPrepaymentAccountReportServiceImpl(
        InternalPrepaymentAccountReportRepository prepaymentAccountReportRepository,
        PrepaymentAccountReportMapper prepaymentAccountReportMapper,
        PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository,
        Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> prepaymentAccountReportDTOMapping) {
        this.prepaymentAccountReportRepository = prepaymentAccountReportRepository;
        this.prepaymentAccountReportMapper = prepaymentAccountReportMapper;
        this.prepaymentAccountReportSearchRepository = prepaymentAccountReportSearchRepository;
        this.prepaymentAccountReportDTOMapping = prepaymentAccountReportDTOMapping;
    }

    @Override
    public PrepaymentAccountReportDTO save(PrepaymentAccountReportDTO prepaymentAccountReportDTO) {
        log.debug("Request to save PrepaymentAccountReport : {}", prepaymentAccountReportDTO);
        PrepaymentAccountReport prepaymentAccountReport = prepaymentAccountReportMapper.toEntity(prepaymentAccountReportDTO);
        prepaymentAccountReport = prepaymentAccountReportRepository.save(prepaymentAccountReport);
        PrepaymentAccountReportDTO result = prepaymentAccountReportMapper.toDto(prepaymentAccountReport);
        prepaymentAccountReportSearchRepository.save(prepaymentAccountReport);
        return result;
    }

    @Override
    public Optional<PrepaymentAccountReportDTO> partialUpdate(PrepaymentAccountReportDTO prepaymentAccountReportDTO) {
        log.debug("Request to partially update PrepaymentAccountReport : {}", prepaymentAccountReportDTO);

        return prepaymentAccountReportRepository
            .findById(prepaymentAccountReportDTO.getId())
            .map(existingPrepaymentAccountReport -> {
                prepaymentAccountReportMapper.partialUpdate(existingPrepaymentAccountReport, prepaymentAccountReportDTO);

                return existingPrepaymentAccountReport;
            })
            .map(prepaymentAccountReportRepository::save)
            .map(savedPrepaymentAccountReport -> {
                prepaymentAccountReportSearchRepository.save(savedPrepaymentAccountReport);

                return savedPrepaymentAccountReport;
            })
            .map(prepaymentAccountReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentAccountReports");
        return prepaymentAccountReportRepository.findAll(pageable).map(prepaymentAccountReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentAccountReportDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentAccountReport : {}", id);
        return prepaymentAccountReportRepository.findById(id).map(prepaymentAccountReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentAccountReport : {}", id);
        prepaymentAccountReportRepository.deleteById(id);
        prepaymentAccountReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentAccountReports for query {}", query);
        return prepaymentAccountReportSearchRepository.search(query, pageable).map(prepaymentAccountReportMapper::toDto);
    }

    /**
     * Fetch the prepayment account report items as at a given date
     *
     * @param reportDate
     * @param pageable   the pagination information
     * @return list of prepayment report items
     */
    @Override
    public Page<PrepaymentAccountReportDTO> findAllByReportDate(LocalDate reportDate, Pageable pageable) {

        return prepaymentAccountReportRepository.findAllByReportDate(reportDate, pageable)
            .map(prepaymentAccountReportDTOMapping::toValue2);
    }

    /**
     * Fetch the account report as a single item value by specified date
     *
     * @param reportDate date specified for account balance
     * @param id         id of the specified transaction-account
     * @return account value for specified date
     */
    @Override
    public Optional<PrepaymentAccountReportDTO> findOneByReportDate(LocalDate reportDate, long id) {

        return prepaymentAccountReportRepository.findOneByReportDate(reportDate, id)
                .map(prepaymentAccountReportDTOMapping::toValue2);
    }
}
