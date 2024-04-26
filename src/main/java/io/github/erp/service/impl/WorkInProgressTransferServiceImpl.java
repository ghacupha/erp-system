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

import io.github.erp.domain.WorkInProgressTransfer;
import io.github.erp.repository.WorkInProgressTransferRepository;
import io.github.erp.repository.search.WorkInProgressTransferSearchRepository;
import io.github.erp.service.WorkInProgressTransferService;
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import io.github.erp.service.mapper.WorkInProgressTransferMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkInProgressTransfer}.
 */
@Service
@Transactional
public class WorkInProgressTransferServiceImpl implements WorkInProgressTransferService {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressTransferServiceImpl.class);

    private final WorkInProgressTransferRepository workInProgressTransferRepository;

    private final WorkInProgressTransferMapper workInProgressTransferMapper;

    private final WorkInProgressTransferSearchRepository workInProgressTransferSearchRepository;

    public WorkInProgressTransferServiceImpl(
        WorkInProgressTransferRepository workInProgressTransferRepository,
        WorkInProgressTransferMapper workInProgressTransferMapper,
        WorkInProgressTransferSearchRepository workInProgressTransferSearchRepository
    ) {
        this.workInProgressTransferRepository = workInProgressTransferRepository;
        this.workInProgressTransferMapper = workInProgressTransferMapper;
        this.workInProgressTransferSearchRepository = workInProgressTransferSearchRepository;
    }

    @Override
    public WorkInProgressTransferDTO save(WorkInProgressTransferDTO workInProgressTransferDTO) {
        log.debug("Request to save WorkInProgressTransfer : {}", workInProgressTransferDTO);
        WorkInProgressTransfer workInProgressTransfer = workInProgressTransferMapper.toEntity(workInProgressTransferDTO);
        workInProgressTransfer = workInProgressTransferRepository.save(workInProgressTransfer);
        WorkInProgressTransferDTO result = workInProgressTransferMapper.toDto(workInProgressTransfer);
        workInProgressTransferSearchRepository.save(workInProgressTransfer);
        return result;
    }

    @Override
    public Optional<WorkInProgressTransferDTO> partialUpdate(WorkInProgressTransferDTO workInProgressTransferDTO) {
        log.debug("Request to partially update WorkInProgressTransfer : {}", workInProgressTransferDTO);

        return workInProgressTransferRepository
            .findById(workInProgressTransferDTO.getId())
            .map(existingWorkInProgressTransfer -> {
                workInProgressTransferMapper.partialUpdate(existingWorkInProgressTransfer, workInProgressTransferDTO);

                return existingWorkInProgressTransfer;
            })
            .map(workInProgressTransferRepository::save)
            .map(savedWorkInProgressTransfer -> {
                workInProgressTransferSearchRepository.save(savedWorkInProgressTransfer);

                return savedWorkInProgressTransfer;
            })
            .map(workInProgressTransferMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressTransferDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkInProgressTransfers");
        return workInProgressTransferRepository.findAll(pageable).map(workInProgressTransferMapper::toDto);
    }

    public Page<WorkInProgressTransferDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workInProgressTransferRepository.findAllWithEagerRelationships(pageable).map(workInProgressTransferMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkInProgressTransferDTO> findOne(Long id) {
        log.debug("Request to get WorkInProgressTransfer : {}", id);
        return workInProgressTransferRepository.findOneWithEagerRelationships(id).map(workInProgressTransferMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkInProgressTransfer : {}", id);
        workInProgressTransferRepository.deleteById(id);
        workInProgressTransferSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressTransferDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkInProgressTransfers for query {}", query);
        return workInProgressTransferSearchRepository.search(query, pageable).map(workInProgressTransferMapper::toDto);
    }
}
