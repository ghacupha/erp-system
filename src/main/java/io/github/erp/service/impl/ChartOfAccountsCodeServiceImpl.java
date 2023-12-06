package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
