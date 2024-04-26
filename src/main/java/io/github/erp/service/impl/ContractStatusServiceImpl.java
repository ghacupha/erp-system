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

import io.github.erp.domain.ContractStatus;
import io.github.erp.repository.ContractStatusRepository;
import io.github.erp.repository.search.ContractStatusSearchRepository;
import io.github.erp.service.ContractStatusService;
import io.github.erp.service.dto.ContractStatusDTO;
import io.github.erp.service.mapper.ContractStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContractStatus}.
 */
@Service
@Transactional
public class ContractStatusServiceImpl implements ContractStatusService {

    private final Logger log = LoggerFactory.getLogger(ContractStatusServiceImpl.class);

    private final ContractStatusRepository contractStatusRepository;

    private final ContractStatusMapper contractStatusMapper;

    private final ContractStatusSearchRepository contractStatusSearchRepository;

    public ContractStatusServiceImpl(
        ContractStatusRepository contractStatusRepository,
        ContractStatusMapper contractStatusMapper,
        ContractStatusSearchRepository contractStatusSearchRepository
    ) {
        this.contractStatusRepository = contractStatusRepository;
        this.contractStatusMapper = contractStatusMapper;
        this.contractStatusSearchRepository = contractStatusSearchRepository;
    }

    @Override
    public ContractStatusDTO save(ContractStatusDTO contractStatusDTO) {
        log.debug("Request to save ContractStatus : {}", contractStatusDTO);
        ContractStatus contractStatus = contractStatusMapper.toEntity(contractStatusDTO);
        contractStatus = contractStatusRepository.save(contractStatus);
        ContractStatusDTO result = contractStatusMapper.toDto(contractStatus);
        contractStatusSearchRepository.save(contractStatus);
        return result;
    }

    @Override
    public Optional<ContractStatusDTO> partialUpdate(ContractStatusDTO contractStatusDTO) {
        log.debug("Request to partially update ContractStatus : {}", contractStatusDTO);

        return contractStatusRepository
            .findById(contractStatusDTO.getId())
            .map(existingContractStatus -> {
                contractStatusMapper.partialUpdate(existingContractStatus, contractStatusDTO);

                return existingContractStatus;
            })
            .map(contractStatusRepository::save)
            .map(savedContractStatus -> {
                contractStatusSearchRepository.save(savedContractStatus);

                return savedContractStatus;
            })
            .map(contractStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContractStatuses");
        return contractStatusRepository.findAll(pageable).map(contractStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractStatusDTO> findOne(Long id) {
        log.debug("Request to get ContractStatus : {}", id);
        return contractStatusRepository.findById(id).map(contractStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContractStatus : {}", id);
        contractStatusRepository.deleteById(id);
        contractStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContractStatuses for query {}", query);
        return contractStatusSearchRepository.search(query, pageable).map(contractStatusMapper::toDto);
    }
}
