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

import io.github.erp.domain.RouAccountBalanceReport;
import io.github.erp.repository.RouAccountBalanceReportRepository;
import io.github.erp.repository.search.RouAccountBalanceReportSearchRepository;
import io.github.erp.service.RouAccountBalanceReportService;
import io.github.erp.service.dto.RouAccountBalanceReportDTO;
import io.github.erp.service.mapper.RouAccountBalanceReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouAccountBalanceReport}.
 */
@Service
@Transactional
public class RouAccountBalanceReportServiceImpl implements RouAccountBalanceReportService {

    private final Logger log = LoggerFactory.getLogger(RouAccountBalanceReportServiceImpl.class);

    private final RouAccountBalanceReportRepository rouAccountBalanceReportRepository;

    private final RouAccountBalanceReportMapper rouAccountBalanceReportMapper;

    private final RouAccountBalanceReportSearchRepository rouAccountBalanceReportSearchRepository;

    public RouAccountBalanceReportServiceImpl(
        RouAccountBalanceReportRepository rouAccountBalanceReportRepository,
        RouAccountBalanceReportMapper rouAccountBalanceReportMapper,
        RouAccountBalanceReportSearchRepository rouAccountBalanceReportSearchRepository
    ) {
        this.rouAccountBalanceReportRepository = rouAccountBalanceReportRepository;
        this.rouAccountBalanceReportMapper = rouAccountBalanceReportMapper;
        this.rouAccountBalanceReportSearchRepository = rouAccountBalanceReportSearchRepository;
    }

    @Override
    public RouAccountBalanceReportDTO save(RouAccountBalanceReportDTO rouAccountBalanceReportDTO) {
        log.debug("Request to save RouAccountBalanceReport : {}", rouAccountBalanceReportDTO);
        RouAccountBalanceReport rouAccountBalanceReport = rouAccountBalanceReportMapper.toEntity(rouAccountBalanceReportDTO);
        rouAccountBalanceReport = rouAccountBalanceReportRepository.save(rouAccountBalanceReport);
        RouAccountBalanceReportDTO result = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);
        rouAccountBalanceReportSearchRepository.save(rouAccountBalanceReport);
        return result;
    }

    @Override
    public Optional<RouAccountBalanceReportDTO> partialUpdate(RouAccountBalanceReportDTO rouAccountBalanceReportDTO) {
        log.debug("Request to partially update RouAccountBalanceReport : {}", rouAccountBalanceReportDTO);

        return rouAccountBalanceReportRepository
            .findById(rouAccountBalanceReportDTO.getId())
            .map(existingRouAccountBalanceReport -> {
                rouAccountBalanceReportMapper.partialUpdate(existingRouAccountBalanceReport, rouAccountBalanceReportDTO);

                return existingRouAccountBalanceReport;
            })
            .map(rouAccountBalanceReportRepository::save)
            .map(savedRouAccountBalanceReport -> {
                rouAccountBalanceReportSearchRepository.save(savedRouAccountBalanceReport);

                return savedRouAccountBalanceReport;
            })
            .map(rouAccountBalanceReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAccountBalanceReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouAccountBalanceReports");
        return rouAccountBalanceReportRepository.findAll(pageable).map(rouAccountBalanceReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouAccountBalanceReportDTO> findOne(Long id) {
        log.debug("Request to get RouAccountBalanceReport : {}", id);
        return rouAccountBalanceReportRepository.findById(id).map(rouAccountBalanceReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouAccountBalanceReport : {}", id);
        rouAccountBalanceReportRepository.deleteById(id);
        rouAccountBalanceReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouAccountBalanceReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouAccountBalanceReports for query {}", query);
        return rouAccountBalanceReportSearchRepository.search(query, pageable).map(rouAccountBalanceReportMapper::toDto);
    }
}
