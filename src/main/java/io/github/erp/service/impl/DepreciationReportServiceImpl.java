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
