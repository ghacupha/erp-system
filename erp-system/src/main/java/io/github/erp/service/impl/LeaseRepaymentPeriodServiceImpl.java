package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import io.github.erp.repository.search.LeaseRepaymentPeriodSearchRepository;
import io.github.erp.service.LeaseRepaymentPeriodService;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import io.github.erp.service.mapper.LeaseRepaymentPeriodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseRepaymentPeriod}.
 */
@Service
@Transactional
public class LeaseRepaymentPeriodServiceImpl implements LeaseRepaymentPeriodService {

    private final Logger log = LoggerFactory.getLogger(LeaseRepaymentPeriodServiceImpl.class);

    private final LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;

    private final LeaseRepaymentPeriodMapper leaseRepaymentPeriodMapper;

    private final LeaseRepaymentPeriodSearchRepository leaseRepaymentPeriodSearchRepository;

    public LeaseRepaymentPeriodServiceImpl(
        LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository,
        LeaseRepaymentPeriodMapper leaseRepaymentPeriodMapper,
        LeaseRepaymentPeriodSearchRepository leaseRepaymentPeriodSearchRepository
    ) {
        this.leaseRepaymentPeriodRepository = leaseRepaymentPeriodRepository;
        this.leaseRepaymentPeriodMapper = leaseRepaymentPeriodMapper;
        this.leaseRepaymentPeriodSearchRepository = leaseRepaymentPeriodSearchRepository;
    }

    @Override
    public LeaseRepaymentPeriodDTO save(LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO) {
        log.debug("Request to save LeaseRepaymentPeriod : {}", leaseRepaymentPeriodDTO);
        LeaseRepaymentPeriod leaseRepaymentPeriod = leaseRepaymentPeriodMapper.toEntity(leaseRepaymentPeriodDTO);
        leaseRepaymentPeriod = leaseRepaymentPeriodRepository.save(leaseRepaymentPeriod);
        LeaseRepaymentPeriodDTO result = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);
        leaseRepaymentPeriodSearchRepository.save(leaseRepaymentPeriod);
        return result;
    }

    @Override
    public Optional<LeaseRepaymentPeriodDTO> partialUpdate(LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO) {
        log.debug("Request to partially update LeaseRepaymentPeriod : {}", leaseRepaymentPeriodDTO);

        return leaseRepaymentPeriodRepository
            .findById(leaseRepaymentPeriodDTO.getId())
            .map(existingLeaseRepaymentPeriod -> {
                leaseRepaymentPeriodMapper.partialUpdate(existingLeaseRepaymentPeriod, leaseRepaymentPeriodDTO);

                return existingLeaseRepaymentPeriod;
            })
            .map(leaseRepaymentPeriodRepository::save)
            .map(savedLeaseRepaymentPeriod -> {
                leaseRepaymentPeriodSearchRepository.save(savedLeaseRepaymentPeriod);

                return savedLeaseRepaymentPeriod;
            })
            .map(leaseRepaymentPeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseRepaymentPeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseRepaymentPeriods");
        return leaseRepaymentPeriodRepository.findAll(pageable).map(leaseRepaymentPeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseRepaymentPeriodDTO> findOne(Long id) {
        log.debug("Request to get LeaseRepaymentPeriod : {}", id);
        return leaseRepaymentPeriodRepository.findById(id).map(leaseRepaymentPeriodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseRepaymentPeriod : {}", id);
        leaseRepaymentPeriodRepository.deleteById(id);
        leaseRepaymentPeriodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseRepaymentPeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseRepaymentPeriods for query {}", query);
        return leaseRepaymentPeriodSearchRepository.search(query, pageable).map(leaseRepaymentPeriodMapper::toDto);
    }
}
