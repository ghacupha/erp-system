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

import io.github.erp.domain.AccountType;
import io.github.erp.repository.AccountTypeRepository;
import io.github.erp.repository.search.AccountTypeSearchRepository;
import io.github.erp.service.AccountTypeService;
import io.github.erp.service.dto.AccountTypeDTO;
import io.github.erp.service.mapper.AccountTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountType}.
 */
@Service
@Transactional
public class AccountTypeServiceImpl implements AccountTypeService {

    private final Logger log = LoggerFactory.getLogger(AccountTypeServiceImpl.class);

    private final AccountTypeRepository accountTypeRepository;

    private final AccountTypeMapper accountTypeMapper;

    private final AccountTypeSearchRepository accountTypeSearchRepository;

    public AccountTypeServiceImpl(
        AccountTypeRepository accountTypeRepository,
        AccountTypeMapper accountTypeMapper,
        AccountTypeSearchRepository accountTypeSearchRepository
    ) {
        this.accountTypeRepository = accountTypeRepository;
        this.accountTypeMapper = accountTypeMapper;
        this.accountTypeSearchRepository = accountTypeSearchRepository;
    }

    @Override
    public AccountTypeDTO save(AccountTypeDTO accountTypeDTO) {
        log.debug("Request to save AccountType : {}", accountTypeDTO);
        AccountType accountType = accountTypeMapper.toEntity(accountTypeDTO);
        accountType = accountTypeRepository.save(accountType);
        AccountTypeDTO result = accountTypeMapper.toDto(accountType);
        accountTypeSearchRepository.save(accountType);
        return result;
    }

    @Override
    public Optional<AccountTypeDTO> partialUpdate(AccountTypeDTO accountTypeDTO) {
        log.debug("Request to partially update AccountType : {}", accountTypeDTO);

        return accountTypeRepository
            .findById(accountTypeDTO.getId())
            .map(existingAccountType -> {
                accountTypeMapper.partialUpdate(existingAccountType, accountTypeDTO);

                return existingAccountType;
            })
            .map(accountTypeRepository::save)
            .map(savedAccountType -> {
                accountTypeSearchRepository.save(savedAccountType);

                return savedAccountType;
            })
            .map(accountTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountTypes");
        return accountTypeRepository.findAll(pageable).map(accountTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountTypeDTO> findOne(Long id) {
        log.debug("Request to get AccountType : {}", id);
        return accountTypeRepository.findById(id).map(accountTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountType : {}", id);
        accountTypeRepository.deleteById(id);
        accountTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountTypes for query {}", query);
        return accountTypeSearchRepository.search(query, pageable).map(accountTypeMapper::toDto);
    }
}
