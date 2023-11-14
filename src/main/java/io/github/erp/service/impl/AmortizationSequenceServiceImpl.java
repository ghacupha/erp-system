package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.AmortizationSequence;
import io.github.erp.repository.AmortizationSequenceRepository;
import io.github.erp.repository.search.AmortizationSequenceSearchRepository;
import io.github.erp.service.AmortizationSequenceService;
import io.github.erp.service.dto.AmortizationSequenceDTO;
import io.github.erp.service.mapper.AmortizationSequenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AmortizationSequence}.
 */
@Service
@Transactional
public class AmortizationSequenceServiceImpl implements AmortizationSequenceService {

    private final Logger log = LoggerFactory.getLogger(AmortizationSequenceServiceImpl.class);

    private final AmortizationSequenceRepository amortizationSequenceRepository;

    private final AmortizationSequenceMapper amortizationSequenceMapper;

    private final AmortizationSequenceSearchRepository amortizationSequenceSearchRepository;

    public AmortizationSequenceServiceImpl(
        AmortizationSequenceRepository amortizationSequenceRepository,
        AmortizationSequenceMapper amortizationSequenceMapper,
        AmortizationSequenceSearchRepository amortizationSequenceSearchRepository
    ) {
        this.amortizationSequenceRepository = amortizationSequenceRepository;
        this.amortizationSequenceMapper = amortizationSequenceMapper;
        this.amortizationSequenceSearchRepository = amortizationSequenceSearchRepository;
    }

    @Override
    public AmortizationSequenceDTO save(AmortizationSequenceDTO amortizationSequenceDTO) {
        log.debug("Request to save AmortizationSequence : {}", amortizationSequenceDTO);
        AmortizationSequence amortizationSequence = amortizationSequenceMapper.toEntity(amortizationSequenceDTO);
        amortizationSequence = amortizationSequenceRepository.save(amortizationSequence);
        AmortizationSequenceDTO result = amortizationSequenceMapper.toDto(amortizationSequence);
        amortizationSequenceSearchRepository.save(amortizationSequence);
        return result;
    }

    @Override
    public Optional<AmortizationSequenceDTO> partialUpdate(AmortizationSequenceDTO amortizationSequenceDTO) {
        log.debug("Request to partially update AmortizationSequence : {}", amortizationSequenceDTO);

        return amortizationSequenceRepository
            .findById(amortizationSequenceDTO.getId())
            .map(existingAmortizationSequence -> {
                amortizationSequenceMapper.partialUpdate(existingAmortizationSequence, amortizationSequenceDTO);

                return existingAmortizationSequence;
            })
            .map(amortizationSequenceRepository::save)
            .map(savedAmortizationSequence -> {
                amortizationSequenceSearchRepository.save(savedAmortizationSequence);

                return savedAmortizationSequence;
            })
            .map(amortizationSequenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationSequenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmortizationSequences");
        return amortizationSequenceRepository.findAll(pageable).map(amortizationSequenceMapper::toDto);
    }

    public Page<AmortizationSequenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return amortizationSequenceRepository.findAllWithEagerRelationships(pageable).map(amortizationSequenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmortizationSequenceDTO> findOne(Long id) {
        log.debug("Request to get AmortizationSequence : {}", id);
        return amortizationSequenceRepository.findOneWithEagerRelationships(id).map(amortizationSequenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmortizationSequence : {}", id);
        amortizationSequenceRepository.deleteById(id);
        amortizationSequenceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationSequenceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmortizationSequences for query {}", query);
        return amortizationSequenceSearchRepository.search(query, pageable).map(amortizationSequenceMapper::toDto);
    }
}
