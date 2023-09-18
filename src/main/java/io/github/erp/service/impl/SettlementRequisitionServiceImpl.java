package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.domain.SettlementRequisition;
import io.github.erp.repository.SettlementRequisitionRepository;
import io.github.erp.repository.search.SettlementRequisitionSearchRepository;
import io.github.erp.service.SettlementRequisitionService;
import io.github.erp.service.dto.SettlementRequisitionDTO;
import io.github.erp.service.mapper.SettlementRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SettlementRequisition}.
 */
@Service
@Transactional
public class SettlementRequisitionServiceImpl implements SettlementRequisitionService {

    private final Logger log = LoggerFactory.getLogger(SettlementRequisitionServiceImpl.class);

    private final SettlementRequisitionRepository settlementRequisitionRepository;

    private final SettlementRequisitionMapper settlementRequisitionMapper;

    private final SettlementRequisitionSearchRepository settlementRequisitionSearchRepository;

    public SettlementRequisitionServiceImpl(
        SettlementRequisitionRepository settlementRequisitionRepository,
        SettlementRequisitionMapper settlementRequisitionMapper,
        SettlementRequisitionSearchRepository settlementRequisitionSearchRepository
    ) {
        this.settlementRequisitionRepository = settlementRequisitionRepository;
        this.settlementRequisitionMapper = settlementRequisitionMapper;
        this.settlementRequisitionSearchRepository = settlementRequisitionSearchRepository;
    }

    @Override
    public SettlementRequisitionDTO save(SettlementRequisitionDTO settlementRequisitionDTO) {
        log.debug("Request to save SettlementRequisition : {}", settlementRequisitionDTO);
        SettlementRequisition settlementRequisition = settlementRequisitionMapper.toEntity(settlementRequisitionDTO);
        settlementRequisition = settlementRequisitionRepository.save(settlementRequisition);
        SettlementRequisitionDTO result = settlementRequisitionMapper.toDto(settlementRequisition);
        settlementRequisitionSearchRepository.save(settlementRequisition);
        return result;
    }

    @Override
    public Optional<SettlementRequisitionDTO> partialUpdate(SettlementRequisitionDTO settlementRequisitionDTO) {
        log.debug("Request to partially update SettlementRequisition : {}", settlementRequisitionDTO);

        return settlementRequisitionRepository
            .findById(settlementRequisitionDTO.getId())
            .map(existingSettlementRequisition -> {
                settlementRequisitionMapper.partialUpdate(existingSettlementRequisition, settlementRequisitionDTO);

                return existingSettlementRequisition;
            })
            .map(settlementRequisitionRepository::save)
            .map(savedSettlementRequisition -> {
                settlementRequisitionSearchRepository.save(savedSettlementRequisition);

                return savedSettlementRequisition;
            })
            .map(settlementRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SettlementRequisitions");
        return settlementRequisitionRepository.findAll(pageable).map(settlementRequisitionMapper::toDto);
    }

    public Page<SettlementRequisitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return settlementRequisitionRepository.findAllWithEagerRelationships(pageable).map(settlementRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SettlementRequisitionDTO> findOne(Long id) {
        log.debug("Request to get SettlementRequisition : {}", id);
        return settlementRequisitionRepository.findOneWithEagerRelationships(id).map(settlementRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SettlementRequisition : {}", id);
        settlementRequisitionRepository.deleteById(id);
        settlementRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SettlementRequisitions for query {}", query);
        return settlementRequisitionSearchRepository.search(query, pageable).map(settlementRequisitionMapper::toDto);
    }
}
