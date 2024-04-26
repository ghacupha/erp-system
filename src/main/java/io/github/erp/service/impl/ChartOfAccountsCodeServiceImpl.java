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

import io.github.erp.domain.ChartOfAccountsCode;
import io.github.erp.repository.ChartOfAccountsCodeRepository;
import io.github.erp.repository.search.ChartOfAccountsCodeSearchRepository;
import io.github.erp.service.ChartOfAccountsCodeService;
import io.github.erp.service.dto.ChartOfAccountsCodeDTO;
import io.github.erp.service.mapper.ChartOfAccountsCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChartOfAccountsCode}.
 */
@Service
@Transactional
public class ChartOfAccountsCodeServiceImpl implements ChartOfAccountsCodeService {

    private final Logger log = LoggerFactory.getLogger(ChartOfAccountsCodeServiceImpl.class);

    private final ChartOfAccountsCodeRepository chartOfAccountsCodeRepository;

    private final ChartOfAccountsCodeMapper chartOfAccountsCodeMapper;

    private final ChartOfAccountsCodeSearchRepository chartOfAccountsCodeSearchRepository;

    public ChartOfAccountsCodeServiceImpl(
        ChartOfAccountsCodeRepository chartOfAccountsCodeRepository,
        ChartOfAccountsCodeMapper chartOfAccountsCodeMapper,
        ChartOfAccountsCodeSearchRepository chartOfAccountsCodeSearchRepository
    ) {
        this.chartOfAccountsCodeRepository = chartOfAccountsCodeRepository;
        this.chartOfAccountsCodeMapper = chartOfAccountsCodeMapper;
        this.chartOfAccountsCodeSearchRepository = chartOfAccountsCodeSearchRepository;
    }

    @Override
    public ChartOfAccountsCodeDTO save(ChartOfAccountsCodeDTO chartOfAccountsCodeDTO) {
        log.debug("Request to save ChartOfAccountsCode : {}", chartOfAccountsCodeDTO);
        ChartOfAccountsCode chartOfAccountsCode = chartOfAccountsCodeMapper.toEntity(chartOfAccountsCodeDTO);
        chartOfAccountsCode = chartOfAccountsCodeRepository.save(chartOfAccountsCode);
        ChartOfAccountsCodeDTO result = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);
        chartOfAccountsCodeSearchRepository.save(chartOfAccountsCode);
        return result;
    }

    @Override
    public Optional<ChartOfAccountsCodeDTO> partialUpdate(ChartOfAccountsCodeDTO chartOfAccountsCodeDTO) {
        log.debug("Request to partially update ChartOfAccountsCode : {}", chartOfAccountsCodeDTO);

        return chartOfAccountsCodeRepository
            .findById(chartOfAccountsCodeDTO.getId())
            .map(existingChartOfAccountsCode -> {
                chartOfAccountsCodeMapper.partialUpdate(existingChartOfAccountsCode, chartOfAccountsCodeDTO);

                return existingChartOfAccountsCode;
            })
            .map(chartOfAccountsCodeRepository::save)
            .map(savedChartOfAccountsCode -> {
                chartOfAccountsCodeSearchRepository.save(savedChartOfAccountsCode);

                return savedChartOfAccountsCode;
            })
            .map(chartOfAccountsCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccountsCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChartOfAccountsCodes");
        return chartOfAccountsCodeRepository.findAll(pageable).map(chartOfAccountsCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChartOfAccountsCodeDTO> findOne(Long id) {
        log.debug("Request to get ChartOfAccountsCode : {}", id);
        return chartOfAccountsCodeRepository.findById(id).map(chartOfAccountsCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChartOfAccountsCode : {}", id);
        chartOfAccountsCodeRepository.deleteById(id);
        chartOfAccountsCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccountsCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChartOfAccountsCodes for query {}", query);
        return chartOfAccountsCodeSearchRepository.search(query, pageable).map(chartOfAccountsCodeMapper::toDto);
    }
}
