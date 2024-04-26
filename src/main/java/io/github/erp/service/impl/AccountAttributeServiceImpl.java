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

import io.github.erp.domain.AccountAttribute;
import io.github.erp.repository.AccountAttributeRepository;
import io.github.erp.repository.search.AccountAttributeSearchRepository;
import io.github.erp.service.AccountAttributeService;
import io.github.erp.service.dto.AccountAttributeDTO;
import io.github.erp.service.mapper.AccountAttributeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountAttribute}.
 */
@Service
@Transactional
public class AccountAttributeServiceImpl implements AccountAttributeService {

    private final Logger log = LoggerFactory.getLogger(AccountAttributeServiceImpl.class);

    private final AccountAttributeRepository accountAttributeRepository;

    private final AccountAttributeMapper accountAttributeMapper;

    private final AccountAttributeSearchRepository accountAttributeSearchRepository;

    public AccountAttributeServiceImpl(
        AccountAttributeRepository accountAttributeRepository,
        AccountAttributeMapper accountAttributeMapper,
        AccountAttributeSearchRepository accountAttributeSearchRepository
    ) {
        this.accountAttributeRepository = accountAttributeRepository;
        this.accountAttributeMapper = accountAttributeMapper;
        this.accountAttributeSearchRepository = accountAttributeSearchRepository;
    }

    @Override
    public AccountAttributeDTO save(AccountAttributeDTO accountAttributeDTO) {
        log.debug("Request to save AccountAttribute : {}", accountAttributeDTO);
        AccountAttribute accountAttribute = accountAttributeMapper.toEntity(accountAttributeDTO);
        accountAttribute = accountAttributeRepository.save(accountAttribute);
        AccountAttributeDTO result = accountAttributeMapper.toDto(accountAttribute);
        accountAttributeSearchRepository.save(accountAttribute);
        return result;
    }

    @Override
    public Optional<AccountAttributeDTO> partialUpdate(AccountAttributeDTO accountAttributeDTO) {
        log.debug("Request to partially update AccountAttribute : {}", accountAttributeDTO);

        return accountAttributeRepository
            .findById(accountAttributeDTO.getId())
            .map(existingAccountAttribute -> {
                accountAttributeMapper.partialUpdate(existingAccountAttribute, accountAttributeDTO);

                return existingAccountAttribute;
            })
            .map(accountAttributeRepository::save)
            .map(savedAccountAttribute -> {
                accountAttributeSearchRepository.save(savedAccountAttribute);

                return savedAccountAttribute;
            })
            .map(accountAttributeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountAttributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountAttributes");
        return accountAttributeRepository.findAll(pageable).map(accountAttributeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountAttributeDTO> findOne(Long id) {
        log.debug("Request to get AccountAttribute : {}", id);
        return accountAttributeRepository.findById(id).map(accountAttributeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountAttribute : {}", id);
        accountAttributeRepository.deleteById(id);
        accountAttributeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountAttributeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountAttributes for query {}", query);
        return accountAttributeSearchRepository.search(query, pageable).map(accountAttributeMapper::toDto);
    }
}
