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

import io.github.erp.domain.RouDepreciationEntryReport;
import io.github.erp.repository.RouDepreciationEntryReportRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportSearchRepository;
import io.github.erp.service.RouDepreciationEntryReportService;
import io.github.erp.service.dto.RouDepreciationEntryReportDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationEntryReport}.
 */
@Service
@Transactional
public class RouDepreciationEntryReportServiceImpl implements RouDepreciationEntryReportService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportServiceImpl.class);

    private final RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository;

    private final RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper;

    private final RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository;

    public RouDepreciationEntryReportServiceImpl(
        RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository,
        RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper,
        RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository
    ) {
        this.rouDepreciationEntryReportRepository = rouDepreciationEntryReportRepository;
        this.rouDepreciationEntryReportMapper = rouDepreciationEntryReportMapper;
        this.rouDepreciationEntryReportSearchRepository = rouDepreciationEntryReportSearchRepository;
    }

    @Override
    public RouDepreciationEntryReportDTO save(RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO) {
        log.debug("Request to save RouDepreciationEntryReport : {}", rouDepreciationEntryReportDTO);
        RouDepreciationEntryReport rouDepreciationEntryReport = rouDepreciationEntryReportMapper.toEntity(rouDepreciationEntryReportDTO);
        rouDepreciationEntryReport = rouDepreciationEntryReportRepository.save(rouDepreciationEntryReport);
        RouDepreciationEntryReportDTO result = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);
        rouDepreciationEntryReportSearchRepository.save(rouDepreciationEntryReport);
        return result;
    }

    @Override
    public Optional<RouDepreciationEntryReportDTO> partialUpdate(RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO) {
        log.debug("Request to partially update RouDepreciationEntryReport : {}", rouDepreciationEntryReportDTO);

        return rouDepreciationEntryReportRepository
            .findById(rouDepreciationEntryReportDTO.getId())
            .map(existingRouDepreciationEntryReport -> {
                rouDepreciationEntryReportMapper.partialUpdate(existingRouDepreciationEntryReport, rouDepreciationEntryReportDTO);

                return existingRouDepreciationEntryReport;
            })
            .map(rouDepreciationEntryReportRepository::save)
            .map(savedRouDepreciationEntryReport -> {
                rouDepreciationEntryReportSearchRepository.save(savedRouDepreciationEntryReport);

                return savedRouDepreciationEntryReport;
            })
            .map(rouDepreciationEntryReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationEntryReports");
        return rouDepreciationEntryReportRepository.findAll(pageable).map(rouDepreciationEntryReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationEntryReportDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationEntryReport : {}", id);
        return rouDepreciationEntryReportRepository.findById(id).map(rouDepreciationEntryReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationEntryReport : {}", id);
        rouDepreciationEntryReportRepository.deleteById(id);
        rouDepreciationEntryReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationEntryReports for query {}", query);
        return rouDepreciationEntryReportSearchRepository.search(query, pageable).map(rouDepreciationEntryReportMapper::toDto);
    }
}
