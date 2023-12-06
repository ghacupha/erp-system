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
