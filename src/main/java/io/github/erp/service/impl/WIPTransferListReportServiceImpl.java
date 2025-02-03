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

import io.github.erp.domain.WIPTransferListReport;
import io.github.erp.repository.WIPTransferListReportRepository;
import io.github.erp.repository.search.WIPTransferListReportSearchRepository;
import io.github.erp.service.WIPTransferListReportService;
import io.github.erp.service.dto.WIPTransferListReportDTO;
import io.github.erp.service.mapper.WIPTransferListReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WIPTransferListReport}.
 */
@Service
@Transactional
public class WIPTransferListReportServiceImpl implements WIPTransferListReportService {

    private final Logger log = LoggerFactory.getLogger(WIPTransferListReportServiceImpl.class);

    private final WIPTransferListReportRepository wIPTransferListReportRepository;

    private final WIPTransferListReportMapper wIPTransferListReportMapper;

    private final WIPTransferListReportSearchRepository wIPTransferListReportSearchRepository;

    public WIPTransferListReportServiceImpl(
        WIPTransferListReportRepository wIPTransferListReportRepository,
        WIPTransferListReportMapper wIPTransferListReportMapper,
        WIPTransferListReportSearchRepository wIPTransferListReportSearchRepository
    ) {
        this.wIPTransferListReportRepository = wIPTransferListReportRepository;
        this.wIPTransferListReportMapper = wIPTransferListReportMapper;
        this.wIPTransferListReportSearchRepository = wIPTransferListReportSearchRepository;
    }

    @Override
    public WIPTransferListReportDTO save(WIPTransferListReportDTO wIPTransferListReportDTO) {
        log.debug("Request to save WIPTransferListReport : {}", wIPTransferListReportDTO);
        WIPTransferListReport wIPTransferListReport = wIPTransferListReportMapper.toEntity(wIPTransferListReportDTO);
        wIPTransferListReport = wIPTransferListReportRepository.save(wIPTransferListReport);
        WIPTransferListReportDTO result = wIPTransferListReportMapper.toDto(wIPTransferListReport);
        wIPTransferListReportSearchRepository.save(wIPTransferListReport);
        return result;
    }

    @Override
    public Optional<WIPTransferListReportDTO> partialUpdate(WIPTransferListReportDTO wIPTransferListReportDTO) {
        log.debug("Request to partially update WIPTransferListReport : {}", wIPTransferListReportDTO);

        return wIPTransferListReportRepository
            .findById(wIPTransferListReportDTO.getId())
            .map(existingWIPTransferListReport -> {
                wIPTransferListReportMapper.partialUpdate(existingWIPTransferListReport, wIPTransferListReportDTO);

                return existingWIPTransferListReport;
            })
            .map(wIPTransferListReportRepository::save)
            .map(savedWIPTransferListReport -> {
                wIPTransferListReportSearchRepository.save(savedWIPTransferListReport);

                return savedWIPTransferListReport;
            })
            .map(wIPTransferListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPTransferListReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPTransferListReports");
        return wIPTransferListReportRepository.findAll(pageable).map(wIPTransferListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPTransferListReportDTO> findOne(Long id) {
        log.debug("Request to get WIPTransferListReport : {}", id);
        return wIPTransferListReportRepository.findById(id).map(wIPTransferListReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPTransferListReport : {}", id);
        wIPTransferListReportRepository.deleteById(id);
        wIPTransferListReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPTransferListReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPTransferListReports for query {}", query);
        return wIPTransferListReportSearchRepository.search(query, pageable).map(wIPTransferListReportMapper::toDto);
    }
}
