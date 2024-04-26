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

import io.github.erp.domain.AutonomousReport;
import io.github.erp.repository.AutonomousReportRepository;
import io.github.erp.repository.search.AutonomousReportSearchRepository;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.mapper.AutonomousReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AutonomousReport}.
 */
@Service
@Transactional
public class AutonomousReportServiceImpl implements AutonomousReportService {

    private final Logger log = LoggerFactory.getLogger(AutonomousReportServiceImpl.class);

    private final AutonomousReportRepository autonomousReportRepository;

    private final AutonomousReportMapper autonomousReportMapper;

    private final AutonomousReportSearchRepository autonomousReportSearchRepository;

    public AutonomousReportServiceImpl(
        AutonomousReportRepository autonomousReportRepository,
        AutonomousReportMapper autonomousReportMapper,
        AutonomousReportSearchRepository autonomousReportSearchRepository
    ) {
        this.autonomousReportRepository = autonomousReportRepository;
        this.autonomousReportMapper = autonomousReportMapper;
        this.autonomousReportSearchRepository = autonomousReportSearchRepository;
    }

    @Override
    public AutonomousReportDTO save(AutonomousReportDTO autonomousReportDTO) {
        log.debug("Request to save AutonomousReport : {}", autonomousReportDTO);
        AutonomousReport autonomousReport = autonomousReportMapper.toEntity(autonomousReportDTO);
        autonomousReport = autonomousReportRepository.save(autonomousReport);
        AutonomousReportDTO result = autonomousReportMapper.toDto(autonomousReport);
        autonomousReportSearchRepository.save(autonomousReport);
        return result;
    }

    @Override
    public Optional<AutonomousReportDTO> partialUpdate(AutonomousReportDTO autonomousReportDTO) {
        log.debug("Request to partially update AutonomousReport : {}", autonomousReportDTO);

        return autonomousReportRepository
            .findById(autonomousReportDTO.getId())
            .map(existingAutonomousReport -> {
                autonomousReportMapper.partialUpdate(existingAutonomousReport, autonomousReportDTO);

                return existingAutonomousReport;
            })
            .map(autonomousReportRepository::save)
            .map(savedAutonomousReport -> {
                autonomousReportSearchRepository.save(savedAutonomousReport);

                return savedAutonomousReport;
            })
            .map(autonomousReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AutonomousReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AutonomousReports");
        return autonomousReportRepository.findAll(pageable).map(autonomousReportMapper::toDto);
    }

    public Page<AutonomousReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return autonomousReportRepository.findAllWithEagerRelationships(pageable).map(autonomousReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutonomousReportDTO> findOne(Long id) {
        log.debug("Request to get AutonomousReport : {}", id);
        return autonomousReportRepository.findOneWithEagerRelationships(id).map(autonomousReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AutonomousReport : {}", id);
        autonomousReportRepository.deleteById(id);
        autonomousReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AutonomousReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AutonomousReports for query {}", query);
        return autonomousReportSearchRepository.search(query, pageable).map(autonomousReportMapper::toDto);
    }
}
