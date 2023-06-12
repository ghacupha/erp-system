package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
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

import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.service.PrepaymentAmortizationService;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.service.mapper.PrepaymentAmortizationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentAmortization}.
 */
@Service
@Transactional
public class PrepaymentAmortizationServiceImpl implements PrepaymentAmortizationService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAmortizationServiceImpl.class);

    private final PrepaymentAmortizationRepository prepaymentAmortizationRepository;

    private final PrepaymentAmortizationMapper prepaymentAmortizationMapper;

    private final PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository;

    public PrepaymentAmortizationServiceImpl(
        PrepaymentAmortizationRepository prepaymentAmortizationRepository,
        PrepaymentAmortizationMapper prepaymentAmortizationMapper,
        PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository
    ) {
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
        this.prepaymentAmortizationMapper = prepaymentAmortizationMapper;
        this.prepaymentAmortizationSearchRepository = prepaymentAmortizationSearchRepository;
    }

    @Override
    public PrepaymentAmortizationDTO save(PrepaymentAmortizationDTO prepaymentAmortizationDTO) {
        log.debug("Request to save PrepaymentAmortization : {}", prepaymentAmortizationDTO);
        PrepaymentAmortization prepaymentAmortization = prepaymentAmortizationMapper.toEntity(prepaymentAmortizationDTO);
        prepaymentAmortization = prepaymentAmortizationRepository.save(prepaymentAmortization);
        PrepaymentAmortizationDTO result = prepaymentAmortizationMapper.toDto(prepaymentAmortization);
        prepaymentAmortizationSearchRepository.save(prepaymentAmortization);
        return result;
    }

    @Override
    public Optional<PrepaymentAmortizationDTO> partialUpdate(PrepaymentAmortizationDTO prepaymentAmortizationDTO) {
        log.debug("Request to partially update PrepaymentAmortization : {}", prepaymentAmortizationDTO);

        return prepaymentAmortizationRepository
            .findById(prepaymentAmortizationDTO.getId())
            .map(existingPrepaymentAmortization -> {
                prepaymentAmortizationMapper.partialUpdate(existingPrepaymentAmortization, prepaymentAmortizationDTO);

                return existingPrepaymentAmortization;
            })
            .map(prepaymentAmortizationRepository::save)
            .map(savedPrepaymentAmortization -> {
                prepaymentAmortizationSearchRepository.save(savedPrepaymentAmortization);

                return savedPrepaymentAmortization;
            })
            .map(prepaymentAmortizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAmortizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentAmortizations");
        return prepaymentAmortizationRepository.findAll(pageable).map(prepaymentAmortizationMapper::toDto);
    }

    public Page<PrepaymentAmortizationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prepaymentAmortizationRepository.findAllWithEagerRelationships(pageable).map(prepaymentAmortizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentAmortizationDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentAmortization : {}", id);
        return prepaymentAmortizationRepository.findOneWithEagerRelationships(id).map(prepaymentAmortizationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentAmortization : {}", id);
        prepaymentAmortizationRepository.deleteById(id);
        prepaymentAmortizationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAmortizationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentAmortizations for query {}", query);
        return prepaymentAmortizationSearchRepository.search(query, pageable).map(prepaymentAmortizationMapper::toDto);
    }
}
