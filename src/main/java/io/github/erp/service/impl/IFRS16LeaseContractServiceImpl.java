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

import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.search.IFRS16LeaseContractSearchRepository;
import io.github.erp.service.IFRS16LeaseContractService;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IFRS16LeaseContract}.
 */
@Service
@Transactional
public class IFRS16LeaseContractServiceImpl implements IFRS16LeaseContractService {

    private final Logger log = LoggerFactory.getLogger(IFRS16LeaseContractServiceImpl.class);

    private final IFRS16LeaseContractRepository iFRS16LeaseContractRepository;

    private final IFRS16LeaseContractMapper iFRS16LeaseContractMapper;

    private final IFRS16LeaseContractSearchRepository iFRS16LeaseContractSearchRepository;

    public IFRS16LeaseContractServiceImpl(
        IFRS16LeaseContractRepository iFRS16LeaseContractRepository,
        IFRS16LeaseContractMapper iFRS16LeaseContractMapper,
        IFRS16LeaseContractSearchRepository iFRS16LeaseContractSearchRepository
    ) {
        this.iFRS16LeaseContractRepository = iFRS16LeaseContractRepository;
        this.iFRS16LeaseContractMapper = iFRS16LeaseContractMapper;
        this.iFRS16LeaseContractSearchRepository = iFRS16LeaseContractSearchRepository;
    }

    @Override
    public IFRS16LeaseContractDTO save(IFRS16LeaseContractDTO iFRS16LeaseContractDTO) {
        log.debug("Request to save IFRS16LeaseContract : {}", iFRS16LeaseContractDTO);
        IFRS16LeaseContract iFRS16LeaseContract = iFRS16LeaseContractMapper.toEntity(iFRS16LeaseContractDTO);
        iFRS16LeaseContract = iFRS16LeaseContractRepository.save(iFRS16LeaseContract);
        IFRS16LeaseContractDTO result = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);
        iFRS16LeaseContractSearchRepository.save(iFRS16LeaseContract);
        return result;
    }

    @Override
    public Optional<IFRS16LeaseContractDTO> partialUpdate(IFRS16LeaseContractDTO iFRS16LeaseContractDTO) {
        log.debug("Request to partially update IFRS16LeaseContract : {}", iFRS16LeaseContractDTO);

        return iFRS16LeaseContractRepository
            .findById(iFRS16LeaseContractDTO.getId())
            .map(existingIFRS16LeaseContract -> {
                iFRS16LeaseContractMapper.partialUpdate(existingIFRS16LeaseContract, iFRS16LeaseContractDTO);

                return existingIFRS16LeaseContract;
            })
            .map(iFRS16LeaseContractRepository::save)
            .map(savedIFRS16LeaseContract -> {
                iFRS16LeaseContractSearchRepository.save(savedIFRS16LeaseContract);

                return savedIFRS16LeaseContract;
            })
            .map(iFRS16LeaseContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IFRS16LeaseContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IFRS16LeaseContracts");
        return iFRS16LeaseContractRepository.findAll(pageable).map(iFRS16LeaseContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IFRS16LeaseContractDTO> findOne(Long id) {
        log.debug("Request to get IFRS16LeaseContract : {}", id);
        return iFRS16LeaseContractRepository.findById(id).map(iFRS16LeaseContractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IFRS16LeaseContract : {}", id);
        iFRS16LeaseContractRepository.deleteById(id);
        iFRS16LeaseContractSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IFRS16LeaseContractDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IFRS16LeaseContracts for query {}", query);
        return iFRS16LeaseContractSearchRepository.search(query, pageable).map(iFRS16LeaseContractMapper::toDto);
    }
}
