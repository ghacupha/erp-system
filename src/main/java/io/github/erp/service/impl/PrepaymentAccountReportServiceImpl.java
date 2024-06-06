package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PrepaymentAccountReport;
import io.github.erp.repository.PrepaymentAccountReportRepository;
import io.github.erp.repository.search.PrepaymentAccountReportSearchRepository;
import io.github.erp.service.PrepaymentAccountReportService;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportMapper;
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
public class PrepaymentAccountReportServiceImpl implements PrepaymentAccountReportService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountReportServiceImpl.class);

    private final PrepaymentAccountReportRepository prepaymentAccountReportRepository;

    private final PrepaymentAccountReportMapper prepaymentAccountReportMapper;

    private final PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository;

    public PrepaymentAccountReportServiceImpl(
        PrepaymentAccountReportRepository prepaymentAccountReportRepository,
        PrepaymentAccountReportMapper prepaymentAccountReportMapper,
        PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository
    ) {
        this.prepaymentAccountReportRepository = prepaymentAccountReportRepository;
        this.prepaymentAccountReportMapper = prepaymentAccountReportMapper;
        this.prepaymentAccountReportSearchRepository = prepaymentAccountReportSearchRepository;
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
}
