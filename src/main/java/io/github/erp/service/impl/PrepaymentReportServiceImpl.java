package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.domain.PrepaymentReport;
import io.github.erp.repository.PrepaymentReportRepository;
import io.github.erp.repository.search.PrepaymentReportSearchRepository;
import io.github.erp.service.PrepaymentReportService;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.mapper.PrepaymentReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentReport}.
 */
@Service
@Transactional
public class PrepaymentReportServiceImpl implements PrepaymentReportService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportServiceImpl.class);

    private final PrepaymentReportRepository prepaymentReportRepository;

    private final PrepaymentReportMapper prepaymentReportMapper;

    private final PrepaymentReportSearchRepository prepaymentReportSearchRepository;

    public PrepaymentReportServiceImpl(
        PrepaymentReportRepository prepaymentReportRepository,
        PrepaymentReportMapper prepaymentReportMapper,
        PrepaymentReportSearchRepository prepaymentReportSearchRepository
    ) {
        this.prepaymentReportRepository = prepaymentReportRepository;
        this.prepaymentReportMapper = prepaymentReportMapper;
        this.prepaymentReportSearchRepository = prepaymentReportSearchRepository;
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
}
