package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
