package io.github.erp.service.impl;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.RouMonthlyDepreciationReport;
import io.github.erp.repository.RouMonthlyDepreciationReportRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportSearchRepository;
import io.github.erp.service.RouMonthlyDepreciationReportService;
import io.github.erp.service.dto.RouMonthlyDepreciationReportDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouMonthlyDepreciationReport}.
 */
@Service
@Transactional
public class RouMonthlyDepreciationReportServiceImpl implements RouMonthlyDepreciationReportService {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportServiceImpl.class);

    private final RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository;

    private final RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper;

    private final RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository;

    public RouMonthlyDepreciationReportServiceImpl(
        RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository,
        RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper,
        RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository
    ) {
        this.rouMonthlyDepreciationReportRepository = rouMonthlyDepreciationReportRepository;
        this.rouMonthlyDepreciationReportMapper = rouMonthlyDepreciationReportMapper;
        this.rouMonthlyDepreciationReportSearchRepository = rouMonthlyDepreciationReportSearchRepository;
    }

    @Override
    public RouMonthlyDepreciationReportDTO save(RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO) {
        log.debug("Request to save RouMonthlyDepreciationReport : {}", rouMonthlyDepreciationReportDTO);
        RouMonthlyDepreciationReport rouMonthlyDepreciationReport = rouMonthlyDepreciationReportMapper.toEntity(
            rouMonthlyDepreciationReportDTO
        );
        rouMonthlyDepreciationReport = rouMonthlyDepreciationReportRepository.save(rouMonthlyDepreciationReport);
        RouMonthlyDepreciationReportDTO result = rouMonthlyDepreciationReportMapper.toDto(rouMonthlyDepreciationReport);
        rouMonthlyDepreciationReportSearchRepository.save(rouMonthlyDepreciationReport);
        return result;
    }

    @Override
    public Optional<RouMonthlyDepreciationReportDTO> partialUpdate(RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO) {
        log.debug("Request to partially update RouMonthlyDepreciationReport : {}", rouMonthlyDepreciationReportDTO);

        return rouMonthlyDepreciationReportRepository
            .findById(rouMonthlyDepreciationReportDTO.getId())
            .map(existingRouMonthlyDepreciationReport -> {
                rouMonthlyDepreciationReportMapper.partialUpdate(existingRouMonthlyDepreciationReport, rouMonthlyDepreciationReportDTO);

                return existingRouMonthlyDepreciationReport;
            })
            .map(rouMonthlyDepreciationReportRepository::save)
            .map(savedRouMonthlyDepreciationReport -> {
                rouMonthlyDepreciationReportSearchRepository.save(savedRouMonthlyDepreciationReport);

                return savedRouMonthlyDepreciationReport;
            })
            .map(rouMonthlyDepreciationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouMonthlyDepreciationReports");
        return rouMonthlyDepreciationReportRepository.findAll(pageable).map(rouMonthlyDepreciationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouMonthlyDepreciationReportDTO> findOne(Long id) {
        log.debug("Request to get RouMonthlyDepreciationReport : {}", id);
        return rouMonthlyDepreciationReportRepository.findById(id).map(rouMonthlyDepreciationReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouMonthlyDepreciationReport : {}", id);
        rouMonthlyDepreciationReportRepository.deleteById(id);
        rouMonthlyDepreciationReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouMonthlyDepreciationReports for query {}", query);
        return rouMonthlyDepreciationReportSearchRepository.search(query, pageable).map(rouMonthlyDepreciationReportMapper::toDto);
    }
}
