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

import io.github.erp.domain.AccountStatusType;
import io.github.erp.repository.AccountStatusTypeRepository;
import io.github.erp.repository.search.AccountStatusTypeSearchRepository;
import io.github.erp.service.AccountStatusTypeService;
import io.github.erp.service.dto.AccountStatusTypeDTO;
import io.github.erp.service.mapper.AccountStatusTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountStatusType}.
 */
@Service
@Transactional
public class AccountStatusTypeServiceImpl implements AccountStatusTypeService {

    private final Logger log = LoggerFactory.getLogger(AccountStatusTypeServiceImpl.class);

    private final AccountStatusTypeRepository accountStatusTypeRepository;

    private final AccountStatusTypeMapper accountStatusTypeMapper;

    private final AccountStatusTypeSearchRepository accountStatusTypeSearchRepository;

    public AccountStatusTypeServiceImpl(
        AccountStatusTypeRepository accountStatusTypeRepository,
        AccountStatusTypeMapper accountStatusTypeMapper,
        AccountStatusTypeSearchRepository accountStatusTypeSearchRepository
    ) {
        this.accountStatusTypeRepository = accountStatusTypeRepository;
        this.accountStatusTypeMapper = accountStatusTypeMapper;
        this.accountStatusTypeSearchRepository = accountStatusTypeSearchRepository;
    }

    @Override
    public AccountStatusTypeDTO save(AccountStatusTypeDTO accountStatusTypeDTO) {
        log.debug("Request to save AccountStatusType : {}", accountStatusTypeDTO);
        AccountStatusType accountStatusType = accountStatusTypeMapper.toEntity(accountStatusTypeDTO);
        accountStatusType = accountStatusTypeRepository.save(accountStatusType);
        AccountStatusTypeDTO result = accountStatusTypeMapper.toDto(accountStatusType);
        accountStatusTypeSearchRepository.save(accountStatusType);
        return result;
    }

    @Override
    public Optional<AccountStatusTypeDTO> partialUpdate(AccountStatusTypeDTO accountStatusTypeDTO) {
        log.debug("Request to partially update AccountStatusType : {}", accountStatusTypeDTO);

        return accountStatusTypeRepository
            .findById(accountStatusTypeDTO.getId())
            .map(existingAccountStatusType -> {
                accountStatusTypeMapper.partialUpdate(existingAccountStatusType, accountStatusTypeDTO);

                return existingAccountStatusType;
            })
            .map(accountStatusTypeRepository::save)
            .map(savedAccountStatusType -> {
                accountStatusTypeSearchRepository.save(savedAccountStatusType);

                return savedAccountStatusType;
            })
            .map(accountStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountStatusTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountStatusTypes");
        return accountStatusTypeRepository.findAll(pageable).map(accountStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountStatusTypeDTO> findOne(Long id) {
        log.debug("Request to get AccountStatusType : {}", id);
        return accountStatusTypeRepository.findById(id).map(accountStatusTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountStatusType : {}", id);
        accountStatusTypeRepository.deleteById(id);
        accountStatusTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountStatusTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountStatusTypes for query {}", query);
        return accountStatusTypeSearchRepository.search(query, pageable).map(accountStatusTypeMapper::toDto);
    }
}
