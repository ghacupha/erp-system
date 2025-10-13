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
