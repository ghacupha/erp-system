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

import io.github.erp.domain.AccountOwnershipType;
import io.github.erp.repository.AccountOwnershipTypeRepository;
import io.github.erp.repository.search.AccountOwnershipTypeSearchRepository;
import io.github.erp.service.AccountOwnershipTypeService;
import io.github.erp.service.dto.AccountOwnershipTypeDTO;
import io.github.erp.service.mapper.AccountOwnershipTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountOwnershipType}.
 */
@Service
@Transactional
public class AccountOwnershipTypeServiceImpl implements AccountOwnershipTypeService {

    private final Logger log = LoggerFactory.getLogger(AccountOwnershipTypeServiceImpl.class);

    private final AccountOwnershipTypeRepository accountOwnershipTypeRepository;

    private final AccountOwnershipTypeMapper accountOwnershipTypeMapper;

    private final AccountOwnershipTypeSearchRepository accountOwnershipTypeSearchRepository;

    public AccountOwnershipTypeServiceImpl(
        AccountOwnershipTypeRepository accountOwnershipTypeRepository,
        AccountOwnershipTypeMapper accountOwnershipTypeMapper,
        AccountOwnershipTypeSearchRepository accountOwnershipTypeSearchRepository
    ) {
        this.accountOwnershipTypeRepository = accountOwnershipTypeRepository;
        this.accountOwnershipTypeMapper = accountOwnershipTypeMapper;
        this.accountOwnershipTypeSearchRepository = accountOwnershipTypeSearchRepository;
    }

    @Override
    public AccountOwnershipTypeDTO save(AccountOwnershipTypeDTO accountOwnershipTypeDTO) {
        log.debug("Request to save AccountOwnershipType : {}", accountOwnershipTypeDTO);
        AccountOwnershipType accountOwnershipType = accountOwnershipTypeMapper.toEntity(accountOwnershipTypeDTO);
        accountOwnershipType = accountOwnershipTypeRepository.save(accountOwnershipType);
        AccountOwnershipTypeDTO result = accountOwnershipTypeMapper.toDto(accountOwnershipType);
        accountOwnershipTypeSearchRepository.save(accountOwnershipType);
        return result;
    }

    @Override
    public Optional<AccountOwnershipTypeDTO> partialUpdate(AccountOwnershipTypeDTO accountOwnershipTypeDTO) {
        log.debug("Request to partially update AccountOwnershipType : {}", accountOwnershipTypeDTO);

        return accountOwnershipTypeRepository
            .findById(accountOwnershipTypeDTO.getId())
            .map(existingAccountOwnershipType -> {
                accountOwnershipTypeMapper.partialUpdate(existingAccountOwnershipType, accountOwnershipTypeDTO);

                return existingAccountOwnershipType;
            })
            .map(accountOwnershipTypeRepository::save)
            .map(savedAccountOwnershipType -> {
                accountOwnershipTypeSearchRepository.save(savedAccountOwnershipType);

                return savedAccountOwnershipType;
            })
            .map(accountOwnershipTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountOwnershipTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountOwnershipTypes");
        return accountOwnershipTypeRepository.findAll(pageable).map(accountOwnershipTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountOwnershipTypeDTO> findOne(Long id) {
        log.debug("Request to get AccountOwnershipType : {}", id);
        return accountOwnershipTypeRepository.findById(id).map(accountOwnershipTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountOwnershipType : {}", id);
        accountOwnershipTypeRepository.deleteById(id);
        accountOwnershipTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountOwnershipTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountOwnershipTypes for query {}", query);
        return accountOwnershipTypeSearchRepository.search(query, pageable).map(accountOwnershipTypeMapper::toDto);
    }
}
