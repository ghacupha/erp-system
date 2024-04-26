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

import io.github.erp.domain.FiscalMonth;
import io.github.erp.repository.FiscalMonthRepository;
import io.github.erp.repository.search.FiscalMonthSearchRepository;
import io.github.erp.service.FiscalMonthService;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.mapper.FiscalMonthMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FiscalMonth}.
 */
@Service
@Transactional
public class FiscalMonthServiceImpl implements FiscalMonthService {

    private final Logger log = LoggerFactory.getLogger(FiscalMonthServiceImpl.class);

    private final FiscalMonthRepository fiscalMonthRepository;

    private final FiscalMonthMapper fiscalMonthMapper;

    private final FiscalMonthSearchRepository fiscalMonthSearchRepository;

    public FiscalMonthServiceImpl(
        FiscalMonthRepository fiscalMonthRepository,
        FiscalMonthMapper fiscalMonthMapper,
        FiscalMonthSearchRepository fiscalMonthSearchRepository
    ) {
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.fiscalMonthSearchRepository = fiscalMonthSearchRepository;
    }

    @Override
    public FiscalMonthDTO save(FiscalMonthDTO fiscalMonthDTO) {
        log.debug("Request to save FiscalMonth : {}", fiscalMonthDTO);
        FiscalMonth fiscalMonth = fiscalMonthMapper.toEntity(fiscalMonthDTO);
        fiscalMonth = fiscalMonthRepository.save(fiscalMonth);
        FiscalMonthDTO result = fiscalMonthMapper.toDto(fiscalMonth);
        fiscalMonthSearchRepository.save(fiscalMonth);
        return result;
    }

    @Override
    public Optional<FiscalMonthDTO> partialUpdate(FiscalMonthDTO fiscalMonthDTO) {
        log.debug("Request to partially update FiscalMonth : {}", fiscalMonthDTO);

        return fiscalMonthRepository
            .findById(fiscalMonthDTO.getId())
            .map(existingFiscalMonth -> {
                fiscalMonthMapper.partialUpdate(existingFiscalMonth, fiscalMonthDTO);

                return existingFiscalMonth;
            })
            .map(fiscalMonthRepository::save)
            .map(savedFiscalMonth -> {
                fiscalMonthSearchRepository.save(savedFiscalMonth);

                return savedFiscalMonth;
            })
            .map(fiscalMonthMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalMonthDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FiscalMonths");
        return fiscalMonthRepository.findAll(pageable).map(fiscalMonthMapper::toDto);
    }

    public Page<FiscalMonthDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fiscalMonthRepository.findAllWithEagerRelationships(pageable).map(fiscalMonthMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiscalMonthDTO> findOne(Long id) {
        log.debug("Request to get FiscalMonth : {}", id);
        return fiscalMonthRepository.findOneWithEagerRelationships(id).map(fiscalMonthMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FiscalMonth : {}", id);
        fiscalMonthRepository.deleteById(id);
        fiscalMonthSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalMonthDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FiscalMonths for query {}", query);
        return fiscalMonthSearchRepository.search(query, pageable).map(fiscalMonthMapper::toDto);
    }
}
