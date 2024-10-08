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

import io.github.erp.domain.DepreciationReport;
import io.github.erp.repository.DepreciationReportRepository;
import io.github.erp.repository.search.DepreciationReportSearchRepository;
import io.github.erp.service.DepreciationReportService;
import io.github.erp.service.dto.DepreciationReportDTO;
import io.github.erp.service.mapper.DepreciationReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationReport}.
 */
@Service
@Transactional
public class DepreciationReportServiceImpl implements DepreciationReportService {

    private final Logger log = LoggerFactory.getLogger(DepreciationReportServiceImpl.class);

    private final DepreciationReportRepository depreciationReportRepository;

    private final DepreciationReportMapper depreciationReportMapper;

    private final DepreciationReportSearchRepository depreciationReportSearchRepository;

    public DepreciationReportServiceImpl(
        DepreciationReportRepository depreciationReportRepository,
        DepreciationReportMapper depreciationReportMapper,
        DepreciationReportSearchRepository depreciationReportSearchRepository
    ) {
        this.depreciationReportRepository = depreciationReportRepository;
        this.depreciationReportMapper = depreciationReportMapper;
        this.depreciationReportSearchRepository = depreciationReportSearchRepository;
    }

    @Override
    public DepreciationReportDTO save(DepreciationReportDTO depreciationReportDTO) {
        log.debug("Request to save DepreciationReport : {}", depreciationReportDTO);
        DepreciationReport depreciationReport = depreciationReportMapper.toEntity(depreciationReportDTO);
        depreciationReport = depreciationReportRepository.save(depreciationReport);
        DepreciationReportDTO result = depreciationReportMapper.toDto(depreciationReport);
        depreciationReportSearchRepository.save(depreciationReport);
        return result;
    }

    @Override
    public Optional<DepreciationReportDTO> partialUpdate(DepreciationReportDTO depreciationReportDTO) {
        log.debug("Request to partially update DepreciationReport : {}", depreciationReportDTO);

        return depreciationReportRepository
            .findById(depreciationReportDTO.getId())
            .map(existingDepreciationReport -> {
                depreciationReportMapper.partialUpdate(existingDepreciationReport, depreciationReportDTO);

                return existingDepreciationReport;
            })
            .map(depreciationReportRepository::save)
            .map(savedDepreciationReport -> {
                depreciationReportSearchRepository.save(savedDepreciationReport);

                return savedDepreciationReport;
            })
            .map(depreciationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationReports");
        return depreciationReportRepository.findAll(pageable).map(depreciationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationReportDTO> findOne(Long id) {
        log.debug("Request to get DepreciationReport : {}", id);
        return depreciationReportRepository.findById(id).map(depreciationReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationReport : {}", id);
        depreciationReportRepository.deleteById(id);
        depreciationReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationReports for query {}", query);
        return depreciationReportSearchRepository.search(query, pageable).map(depreciationReportMapper::toDto);
    }
}
