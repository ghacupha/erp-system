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
