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

import io.github.erp.domain.WIPListReport;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.WIPListReportRepository;
import io.github.erp.repository.search.WIPListReportSearchRepository;
import io.github.erp.service.WIPListReportService;
import io.github.erp.service.dto.WIPListReportDTO;
import io.github.erp.service.mapper.WIPListReportMapper;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WIPListReport}.
 */
@Service
@Transactional
public class WIPListReportServiceImpl implements WIPListReportService {

    private final Logger log = LoggerFactory.getLogger(WIPListReportServiceImpl.class);

    private final WIPListReportRepository wIPListReportRepository;

    private final WIPListReportMapper wIPListReportMapper;

    private final WIPListReportSearchRepository wIPListReportSearchRepository;

    private final InternalApplicationUserDetailService internalApplicationUserDetailService;

    public WIPListReportServiceImpl(
        WIPListReportRepository wIPListReportRepository,
        WIPListReportMapper wIPListReportMapper,
        WIPListReportSearchRepository wIPListReportSearchRepository, InternalApplicationUserDetailService internalApplicationUserDetailService
    ) {
        this.wIPListReportRepository = wIPListReportRepository;
        this.wIPListReportMapper = wIPListReportMapper;
        this.wIPListReportSearchRepository = wIPListReportSearchRepository;
        this.internalApplicationUserDetailService = internalApplicationUserDetailService;
    }

    @Override
    public WIPListReportDTO save(WIPListReportDTO wIPListReportDTO) {
        log.debug("Request to save WIPListReport : {}", wIPListReportDTO);

        internalApplicationUserDetailService.getCurrentApplicationUser().ifPresent(wIPListReportDTO::setRequestedBy);

        WIPListReport wIPListReport = wIPListReportMapper.toEntity(wIPListReportDTO);
        wIPListReport = wIPListReportRepository.save(wIPListReport);
        WIPListReportDTO result = wIPListReportMapper.toDto(wIPListReport);
        wIPListReportSearchRepository.save(wIPListReport);
        return result;
    }

    @Override
    public Optional<WIPListReportDTO> partialUpdate(WIPListReportDTO wIPListReportDTO) {
        log.debug("Request to partially update WIPListReport : {}", wIPListReportDTO);

        return wIPListReportRepository
            .findById(wIPListReportDTO.getId())
            .map(existingWIPListReport -> {
                wIPListReportMapper.partialUpdate(existingWIPListReport, wIPListReportDTO);

                return existingWIPListReport;
            })
            .map(wIPListReportRepository::save)
            .map(savedWIPListReport -> {
                wIPListReportSearchRepository.save(savedWIPListReport);

                return savedWIPListReport;
            })
            .map(wIPListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPListReports");
        return wIPListReportRepository.findAll(pageable).map(wIPListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPListReportDTO> findOne(Long id) {
        log.debug("Request to get WIPListReport : {}", id);
        return wIPListReportRepository.findById(id).map(wIPListReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPListReport : {}", id);
        wIPListReportRepository.deleteById(id);
        wIPListReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPListReports for query {}", query);
        return wIPListReportSearchRepository.search(query, pageable).map(wIPListReportMapper::toDto);
    }
}
