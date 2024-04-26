package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.PerformanceOfForeignSubsidiaries;
import io.github.erp.repository.PerformanceOfForeignSubsidiariesRepository;
import io.github.erp.repository.search.PerformanceOfForeignSubsidiariesSearchRepository;
import io.github.erp.service.PerformanceOfForeignSubsidiariesService;
import io.github.erp.service.dto.PerformanceOfForeignSubsidiariesDTO;
import io.github.erp.service.mapper.PerformanceOfForeignSubsidiariesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PerformanceOfForeignSubsidiaries}.
 */
@Service
@Transactional
public class PerformanceOfForeignSubsidiariesServiceImpl implements PerformanceOfForeignSubsidiariesService {

    private final Logger log = LoggerFactory.getLogger(PerformanceOfForeignSubsidiariesServiceImpl.class);

    private final PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository;

    private final PerformanceOfForeignSubsidiariesMapper performanceOfForeignSubsidiariesMapper;

    private final PerformanceOfForeignSubsidiariesSearchRepository performanceOfForeignSubsidiariesSearchRepository;

    public PerformanceOfForeignSubsidiariesServiceImpl(
        PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository,
        PerformanceOfForeignSubsidiariesMapper performanceOfForeignSubsidiariesMapper,
        PerformanceOfForeignSubsidiariesSearchRepository performanceOfForeignSubsidiariesSearchRepository
    ) {
        this.performanceOfForeignSubsidiariesRepository = performanceOfForeignSubsidiariesRepository;
        this.performanceOfForeignSubsidiariesMapper = performanceOfForeignSubsidiariesMapper;
        this.performanceOfForeignSubsidiariesSearchRepository = performanceOfForeignSubsidiariesSearchRepository;
    }

    @Override
    public PerformanceOfForeignSubsidiariesDTO save(PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO) {
        log.debug("Request to save PerformanceOfForeignSubsidiaries : {}", performanceOfForeignSubsidiariesDTO);
        PerformanceOfForeignSubsidiaries performanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesMapper.toEntity(
            performanceOfForeignSubsidiariesDTO
        );
        performanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesRepository.save(performanceOfForeignSubsidiaries);
        PerformanceOfForeignSubsidiariesDTO result = performanceOfForeignSubsidiariesMapper.toDto(performanceOfForeignSubsidiaries);
        performanceOfForeignSubsidiariesSearchRepository.save(performanceOfForeignSubsidiaries);
        return result;
    }

    @Override
    public Optional<PerformanceOfForeignSubsidiariesDTO> partialUpdate(
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO
    ) {
        log.debug("Request to partially update PerformanceOfForeignSubsidiaries : {}", performanceOfForeignSubsidiariesDTO);

        return performanceOfForeignSubsidiariesRepository
            .findById(performanceOfForeignSubsidiariesDTO.getId())
            .map(existingPerformanceOfForeignSubsidiaries -> {
                performanceOfForeignSubsidiariesMapper.partialUpdate(
                    existingPerformanceOfForeignSubsidiaries,
                    performanceOfForeignSubsidiariesDTO
                );

                return existingPerformanceOfForeignSubsidiaries;
            })
            .map(performanceOfForeignSubsidiariesRepository::save)
            .map(savedPerformanceOfForeignSubsidiaries -> {
                performanceOfForeignSubsidiariesSearchRepository.save(savedPerformanceOfForeignSubsidiaries);

                return savedPerformanceOfForeignSubsidiaries;
            })
            .map(performanceOfForeignSubsidiariesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerformanceOfForeignSubsidiariesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PerformanceOfForeignSubsidiaries");
        return performanceOfForeignSubsidiariesRepository.findAll(pageable).map(performanceOfForeignSubsidiariesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerformanceOfForeignSubsidiariesDTO> findOne(Long id) {
        log.debug("Request to get PerformanceOfForeignSubsidiaries : {}", id);
        return performanceOfForeignSubsidiariesRepository.findById(id).map(performanceOfForeignSubsidiariesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PerformanceOfForeignSubsidiaries : {}", id);
        performanceOfForeignSubsidiariesRepository.deleteById(id);
        performanceOfForeignSubsidiariesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerformanceOfForeignSubsidiariesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PerformanceOfForeignSubsidiaries for query {}", query);
        return performanceOfForeignSubsidiariesSearchRepository.search(query, pageable).map(performanceOfForeignSubsidiariesMapper::toDto);
    }
}
