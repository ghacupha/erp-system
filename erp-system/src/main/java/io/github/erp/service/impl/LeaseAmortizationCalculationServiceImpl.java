package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.LeaseAmortizationCalculation;
import io.github.erp.repository.LeaseAmortizationCalculationRepository;
import io.github.erp.repository.search.LeaseAmortizationCalculationSearchRepository;
import io.github.erp.service.LeaseAmortizationCalculationService;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.mapper.LeaseAmortizationCalculationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseAmortizationCalculation}.
 */
@Service
@Transactional
public class LeaseAmortizationCalculationServiceImpl implements LeaseAmortizationCalculationService {

    private final Logger log = LoggerFactory.getLogger(LeaseAmortizationCalculationServiceImpl.class);

    private final LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository;

    private final LeaseAmortizationCalculationMapper leaseAmortizationCalculationMapper;

    private final LeaseAmortizationCalculationSearchRepository leaseAmortizationCalculationSearchRepository;

    public LeaseAmortizationCalculationServiceImpl(
        LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository,
        LeaseAmortizationCalculationMapper leaseAmortizationCalculationMapper,
        LeaseAmortizationCalculationSearchRepository leaseAmortizationCalculationSearchRepository
    ) {
        this.leaseAmortizationCalculationRepository = leaseAmortizationCalculationRepository;
        this.leaseAmortizationCalculationMapper = leaseAmortizationCalculationMapper;
        this.leaseAmortizationCalculationSearchRepository = leaseAmortizationCalculationSearchRepository;
    }

    @Override
    public LeaseAmortizationCalculationDTO save(LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO) {
        log.debug("Request to save LeaseAmortizationCalculation : {}", leaseAmortizationCalculationDTO);
        LeaseAmortizationCalculation leaseAmortizationCalculation = leaseAmortizationCalculationMapper.toEntity(
            leaseAmortizationCalculationDTO
        );
        leaseAmortizationCalculation = leaseAmortizationCalculationRepository.save(leaseAmortizationCalculation);
        LeaseAmortizationCalculationDTO result = leaseAmortizationCalculationMapper.toDto(leaseAmortizationCalculation);
        leaseAmortizationCalculationSearchRepository.save(leaseAmortizationCalculation);
        return result;
    }

    @Override
    public Optional<LeaseAmortizationCalculationDTO> partialUpdate(LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO) {
        log.debug("Request to partially update LeaseAmortizationCalculation : {}", leaseAmortizationCalculationDTO);

        return leaseAmortizationCalculationRepository
            .findById(leaseAmortizationCalculationDTO.getId())
            .map(existingLeaseAmortizationCalculation -> {
                leaseAmortizationCalculationMapper.partialUpdate(existingLeaseAmortizationCalculation, leaseAmortizationCalculationDTO);

                return existingLeaseAmortizationCalculation;
            })
            .map(leaseAmortizationCalculationRepository::save)
            .map(savedLeaseAmortizationCalculation -> {
                leaseAmortizationCalculationSearchRepository.save(savedLeaseAmortizationCalculation);

                return savedLeaseAmortizationCalculation;
            })
            .map(leaseAmortizationCalculationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseAmortizationCalculationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseAmortizationCalculations");
        return leaseAmortizationCalculationRepository.findAll(pageable).map(leaseAmortizationCalculationMapper::toDto);
    }

    /**
     *  Get all the leaseAmortizationCalculations where LeaseLiability is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseAmortizationCalculationDTO> findAllWhereLeaseLiabilityIsNull() {
        log.debug("Request to get all leaseAmortizationCalculations where LeaseLiability is null");
        return StreamSupport
            .stream(leaseAmortizationCalculationRepository.findAll().spliterator(), false)
            .filter(leaseAmortizationCalculation -> leaseAmortizationCalculation.getLeaseLiability() == null)
            .map(leaseAmortizationCalculationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseAmortizationCalculationDTO> findOne(Long id) {
        log.debug("Request to get LeaseAmortizationCalculation : {}", id);
        return leaseAmortizationCalculationRepository.findById(id).map(leaseAmortizationCalculationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseAmortizationCalculation : {}", id);
        leaseAmortizationCalculationRepository.deleteById(id);
        leaseAmortizationCalculationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseAmortizationCalculationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseAmortizationCalculations for query {}", query);
        return leaseAmortizationCalculationSearchRepository.search(query, pageable).map(leaseAmortizationCalculationMapper::toDto);
    }
}
