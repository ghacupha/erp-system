package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 11 (Caleb Series) Server ver 0.7.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
