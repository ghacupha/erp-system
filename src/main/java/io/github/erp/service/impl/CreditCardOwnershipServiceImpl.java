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

import io.github.erp.domain.CreditCardOwnership;
import io.github.erp.repository.CreditCardOwnershipRepository;
import io.github.erp.repository.search.CreditCardOwnershipSearchRepository;
import io.github.erp.service.CreditCardOwnershipService;
import io.github.erp.service.dto.CreditCardOwnershipDTO;
import io.github.erp.service.mapper.CreditCardOwnershipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CreditCardOwnership}.
 */
@Service
@Transactional
public class CreditCardOwnershipServiceImpl implements CreditCardOwnershipService {

    private final Logger log = LoggerFactory.getLogger(CreditCardOwnershipServiceImpl.class);

    private final CreditCardOwnershipRepository creditCardOwnershipRepository;

    private final CreditCardOwnershipMapper creditCardOwnershipMapper;

    private final CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository;

    public CreditCardOwnershipServiceImpl(
        CreditCardOwnershipRepository creditCardOwnershipRepository,
        CreditCardOwnershipMapper creditCardOwnershipMapper,
        CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository
    ) {
        this.creditCardOwnershipRepository = creditCardOwnershipRepository;
        this.creditCardOwnershipMapper = creditCardOwnershipMapper;
        this.creditCardOwnershipSearchRepository = creditCardOwnershipSearchRepository;
    }

    @Override
    public CreditCardOwnershipDTO save(CreditCardOwnershipDTO creditCardOwnershipDTO) {
        log.debug("Request to save CreditCardOwnership : {}", creditCardOwnershipDTO);
        CreditCardOwnership creditCardOwnership = creditCardOwnershipMapper.toEntity(creditCardOwnershipDTO);
        creditCardOwnership = creditCardOwnershipRepository.save(creditCardOwnership);
        CreditCardOwnershipDTO result = creditCardOwnershipMapper.toDto(creditCardOwnership);
        creditCardOwnershipSearchRepository.save(creditCardOwnership);
        return result;
    }

    @Override
    public Optional<CreditCardOwnershipDTO> partialUpdate(CreditCardOwnershipDTO creditCardOwnershipDTO) {
        log.debug("Request to partially update CreditCardOwnership : {}", creditCardOwnershipDTO);

        return creditCardOwnershipRepository
            .findById(creditCardOwnershipDTO.getId())
            .map(existingCreditCardOwnership -> {
                creditCardOwnershipMapper.partialUpdate(existingCreditCardOwnership, creditCardOwnershipDTO);

                return existingCreditCardOwnership;
            })
            .map(creditCardOwnershipRepository::save)
            .map(savedCreditCardOwnership -> {
                creditCardOwnershipSearchRepository.save(savedCreditCardOwnership);

                return savedCreditCardOwnership;
            })
            .map(creditCardOwnershipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardOwnershipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreditCardOwnerships");
        return creditCardOwnershipRepository.findAll(pageable).map(creditCardOwnershipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardOwnershipDTO> findOne(Long id) {
        log.debug("Request to get CreditCardOwnership : {}", id);
        return creditCardOwnershipRepository.findById(id).map(creditCardOwnershipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreditCardOwnership : {}", id);
        creditCardOwnershipRepository.deleteById(id);
        creditCardOwnershipSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardOwnershipDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CreditCardOwnerships for query {}", query);
        return creditCardOwnershipSearchRepository.search(query, pageable).map(creditCardOwnershipMapper::toDto);
    }
}
