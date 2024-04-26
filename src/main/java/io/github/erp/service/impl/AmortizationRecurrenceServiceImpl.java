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

import io.github.erp.domain.AmortizationRecurrence;
import io.github.erp.repository.AmortizationRecurrenceRepository;
import io.github.erp.repository.search.AmortizationRecurrenceSearchRepository;
import io.github.erp.service.AmortizationRecurrenceService;
import io.github.erp.service.dto.AmortizationRecurrenceDTO;
import io.github.erp.service.mapper.AmortizationRecurrenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AmortizationRecurrence}.
 */
@Service
@Transactional
public class AmortizationRecurrenceServiceImpl implements AmortizationRecurrenceService {

    private final Logger log = LoggerFactory.getLogger(AmortizationRecurrenceServiceImpl.class);

    private final AmortizationRecurrenceRepository amortizationRecurrenceRepository;

    private final AmortizationRecurrenceMapper amortizationRecurrenceMapper;

    private final AmortizationRecurrenceSearchRepository amortizationRecurrenceSearchRepository;

    public AmortizationRecurrenceServiceImpl(
        AmortizationRecurrenceRepository amortizationRecurrenceRepository,
        AmortizationRecurrenceMapper amortizationRecurrenceMapper,
        AmortizationRecurrenceSearchRepository amortizationRecurrenceSearchRepository
    ) {
        this.amortizationRecurrenceRepository = amortizationRecurrenceRepository;
        this.amortizationRecurrenceMapper = amortizationRecurrenceMapper;
        this.amortizationRecurrenceSearchRepository = amortizationRecurrenceSearchRepository;
    }

    @Override
    public AmortizationRecurrenceDTO save(AmortizationRecurrenceDTO amortizationRecurrenceDTO) {
        log.debug("Request to save AmortizationRecurrence : {}", amortizationRecurrenceDTO);
        AmortizationRecurrence amortizationRecurrence = amortizationRecurrenceMapper.toEntity(amortizationRecurrenceDTO);
        amortizationRecurrence = amortizationRecurrenceRepository.save(amortizationRecurrence);
        AmortizationRecurrenceDTO result = amortizationRecurrenceMapper.toDto(amortizationRecurrence);
        amortizationRecurrenceSearchRepository.save(amortizationRecurrence);
        return result;
    }

    @Override
    public Optional<AmortizationRecurrenceDTO> partialUpdate(AmortizationRecurrenceDTO amortizationRecurrenceDTO) {
        log.debug("Request to partially update AmortizationRecurrence : {}", amortizationRecurrenceDTO);

        return amortizationRecurrenceRepository
            .findById(amortizationRecurrenceDTO.getId())
            .map(existingAmortizationRecurrence -> {
                amortizationRecurrenceMapper.partialUpdate(existingAmortizationRecurrence, amortizationRecurrenceDTO);

                return existingAmortizationRecurrence;
            })
            .map(amortizationRecurrenceRepository::save)
            .map(savedAmortizationRecurrence -> {
                amortizationRecurrenceSearchRepository.save(savedAmortizationRecurrence);

                return savedAmortizationRecurrence;
            })
            .map(amortizationRecurrenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationRecurrenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmortizationRecurrences");
        return amortizationRecurrenceRepository.findAll(pageable).map(amortizationRecurrenceMapper::toDto);
    }

    public Page<AmortizationRecurrenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return amortizationRecurrenceRepository.findAllWithEagerRelationships(pageable).map(amortizationRecurrenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmortizationRecurrenceDTO> findOne(Long id) {
        log.debug("Request to get AmortizationRecurrence : {}", id);
        return amortizationRecurrenceRepository.findOneWithEagerRelationships(id).map(amortizationRecurrenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmortizationRecurrence : {}", id);
        amortizationRecurrenceRepository.deleteById(id);
        amortizationRecurrenceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationRecurrenceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmortizationRecurrences for query {}", query);
        return amortizationRecurrenceSearchRepository.search(query, pageable).map(amortizationRecurrenceMapper::toDto);
    }
}
