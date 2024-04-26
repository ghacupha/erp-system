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
