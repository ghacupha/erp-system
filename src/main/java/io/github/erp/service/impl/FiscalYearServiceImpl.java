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

import io.github.erp.domain.FiscalYear;
import io.github.erp.repository.FiscalYearRepository;
import io.github.erp.repository.search.FiscalYearSearchRepository;
import io.github.erp.service.FiscalYearService;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.mapper.FiscalYearMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FiscalYear}.
 */
@Service
@Transactional
public class FiscalYearServiceImpl implements FiscalYearService {

    private final Logger log = LoggerFactory.getLogger(FiscalYearServiceImpl.class);

    private final FiscalYearRepository fiscalYearRepository;

    private final FiscalYearMapper fiscalYearMapper;

    private final FiscalYearSearchRepository fiscalYearSearchRepository;

    public FiscalYearServiceImpl(
        FiscalYearRepository fiscalYearRepository,
        FiscalYearMapper fiscalYearMapper,
        FiscalYearSearchRepository fiscalYearSearchRepository
    ) {
        this.fiscalYearRepository = fiscalYearRepository;
        this.fiscalYearMapper = fiscalYearMapper;
        this.fiscalYearSearchRepository = fiscalYearSearchRepository;
    }

    @Override
    public FiscalYearDTO save(FiscalYearDTO fiscalYearDTO) {
        log.debug("Request to save FiscalYear : {}", fiscalYearDTO);
        FiscalYear fiscalYear = fiscalYearMapper.toEntity(fiscalYearDTO);
        fiscalYear = fiscalYearRepository.save(fiscalYear);
        FiscalYearDTO result = fiscalYearMapper.toDto(fiscalYear);
        fiscalYearSearchRepository.save(fiscalYear);
        return result;
    }

    @Override
    public Optional<FiscalYearDTO> partialUpdate(FiscalYearDTO fiscalYearDTO) {
        log.debug("Request to partially update FiscalYear : {}", fiscalYearDTO);

        return fiscalYearRepository
            .findById(fiscalYearDTO.getId())
            .map(existingFiscalYear -> {
                fiscalYearMapper.partialUpdate(existingFiscalYear, fiscalYearDTO);

                return existingFiscalYear;
            })
            .map(fiscalYearRepository::save)
            .map(savedFiscalYear -> {
                fiscalYearSearchRepository.save(savedFiscalYear);

                return savedFiscalYear;
            })
            .map(fiscalYearMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalYearDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FiscalYears");
        return fiscalYearRepository.findAll(pageable).map(fiscalYearMapper::toDto);
    }

    public Page<FiscalYearDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fiscalYearRepository.findAllWithEagerRelationships(pageable).map(fiscalYearMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiscalYearDTO> findOne(Long id) {
        log.debug("Request to get FiscalYear : {}", id);
        return fiscalYearRepository.findOneWithEagerRelationships(id).map(fiscalYearMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FiscalYear : {}", id);
        fiscalYearRepository.deleteById(id);
        fiscalYearSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalYearDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FiscalYears for query {}", query);
        return fiscalYearSearchRepository.search(query, pageable).map(fiscalYearMapper::toDto);
    }
}
