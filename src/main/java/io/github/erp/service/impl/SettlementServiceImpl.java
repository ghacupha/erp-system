package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.2.0
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

import io.github.erp.domain.Settlement;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.service.SettlementService;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.mapper.SettlementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Settlement}.
 */
@Service
@Transactional
public class SettlementServiceImpl implements SettlementService {

    private final Logger log = LoggerFactory.getLogger(SettlementServiceImpl.class);

    private final SettlementRepository settlementRepository;

    private final SettlementMapper settlementMapper;

    private final SettlementSearchRepository settlementSearchRepository;

    public SettlementServiceImpl(
        SettlementRepository settlementRepository,
        SettlementMapper settlementMapper,
        SettlementSearchRepository settlementSearchRepository
    ) {
        this.settlementRepository = settlementRepository;
        this.settlementMapper = settlementMapper;
        this.settlementSearchRepository = settlementSearchRepository;
    }

    @Override
    public SettlementDTO save(SettlementDTO settlementDTO) {
        log.debug("Request to save Settlement : {}", settlementDTO);
        Settlement settlement = settlementMapper.toEntity(settlementDTO);
        settlement = settlementRepository.save(settlement);
        SettlementDTO result = settlementMapper.toDto(settlement);
        settlementSearchRepository.save(settlement);
        return result;
    }

    @Override
    public Optional<SettlementDTO> partialUpdate(SettlementDTO settlementDTO) {
        log.debug("Request to partially update Settlement : {}", settlementDTO);

        return settlementRepository
            .findById(settlementDTO.getId())
            .map(existingSettlement -> {
                settlementMapper.partialUpdate(existingSettlement, settlementDTO);

                return existingSettlement;
            })
            .map(settlementRepository::save)
            .map(savedSettlement -> {
                settlementSearchRepository.save(savedSettlement);

                return savedSettlement;
            })
            .map(settlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Settlements");
        return settlementRepository.findAll(pageable).map(settlementMapper::toDto);
    }

    public Page<SettlementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return settlementRepository.findAllWithEagerRelationships(pageable).map(settlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SettlementDTO> findOne(Long id) {
        log.debug("Request to get Settlement : {}", id);
        return settlementRepository.findOneWithEagerRelationships(id).map(settlementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Settlement : {}", id);
        settlementRepository.deleteById(id);
        settlementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Settlements for query {}", query);
        return settlementSearchRepository.search(query, pageable).map(settlementMapper::toDto);
    }
}
