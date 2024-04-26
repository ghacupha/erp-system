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
