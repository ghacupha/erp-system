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

import io.github.erp.domain.LeaseSettlement;
import io.github.erp.repository.LeaseSettlementRepository;
import io.github.erp.repository.search.LeaseSettlementSearchRepository;
import io.github.erp.service.LeaseSettlementService;
import io.github.erp.service.dto.LeaseSettlementDTO;
import io.github.erp.service.mapper.LeaseSettlementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseSettlement}.
 */
@Service
@Transactional
public class LeaseSettlementServiceImpl implements LeaseSettlementService {

    private final Logger log = LoggerFactory.getLogger(LeaseSettlementServiceImpl.class);

    private final LeaseSettlementRepository leaseSettlementRepository;

    private final LeaseSettlementMapper leaseSettlementMapper;

    private final LeaseSettlementSearchRepository leaseSettlementSearchRepository;

    public LeaseSettlementServiceImpl(
        LeaseSettlementRepository leaseSettlementRepository,
        LeaseSettlementMapper leaseSettlementMapper,
        LeaseSettlementSearchRepository leaseSettlementSearchRepository
    ) {
        this.leaseSettlementRepository = leaseSettlementRepository;
        this.leaseSettlementMapper = leaseSettlementMapper;
        this.leaseSettlementSearchRepository = leaseSettlementSearchRepository;
    }

    @Override
    public LeaseSettlementDTO save(LeaseSettlementDTO leaseSettlementDTO) {
        log.debug("Request to save LeaseSettlement : {}", leaseSettlementDTO);
        LeaseSettlement leaseSettlement = leaseSettlementMapper.toEntity(leaseSettlementDTO);
        leaseSettlement = leaseSettlementRepository.save(leaseSettlement);
        LeaseSettlementDTO result = leaseSettlementMapper.toDto(leaseSettlement);
        leaseSettlementSearchRepository.save(leaseSettlement);
        return result;
    }

    @Override
    public Optional<LeaseSettlementDTO> partialUpdate(LeaseSettlementDTO leaseSettlementDTO) {
        log.debug("Request to partially update LeaseSettlement : {}", leaseSettlementDTO);

        return leaseSettlementRepository
            .findById(leaseSettlementDTO.getId())
            .map(existingLeaseSettlement -> {
                leaseSettlementMapper.partialUpdate(existingLeaseSettlement, leaseSettlementDTO);

                return existingLeaseSettlement;
            })
            .map(leaseSettlementRepository::save)
            .map(savedLeaseSettlement -> {
                leaseSettlementSearchRepository.save(savedLeaseSettlement);

                return savedLeaseSettlement;
            })
            .map(leaseSettlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseSettlementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseSettlements");
        return leaseSettlementRepository.findAll(pageable).map(leaseSettlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseSettlementDTO> findOne(Long id) {
        log.debug("Request to get LeaseSettlement : {}", id);
        return leaseSettlementRepository.findById(id).map(leaseSettlementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseSettlement : {}", id);
        leaseSettlementRepository.deleteById(id);
        leaseSettlementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseSettlementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseSettlements for query {}", query);
        return leaseSettlementSearchRepository.search(query, pageable).map(leaseSettlementMapper::toDto);
    }
}
