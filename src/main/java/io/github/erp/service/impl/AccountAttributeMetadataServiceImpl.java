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

import io.github.erp.domain.AccountAttributeMetadata;
import io.github.erp.repository.AccountAttributeMetadataRepository;
import io.github.erp.repository.search.AccountAttributeMetadataSearchRepository;
import io.github.erp.service.AccountAttributeMetadataService;
import io.github.erp.service.dto.AccountAttributeMetadataDTO;
import io.github.erp.service.mapper.AccountAttributeMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountAttributeMetadata}.
 */
@Service
@Transactional
public class AccountAttributeMetadataServiceImpl implements AccountAttributeMetadataService {

    private final Logger log = LoggerFactory.getLogger(AccountAttributeMetadataServiceImpl.class);

    private final AccountAttributeMetadataRepository accountAttributeMetadataRepository;

    private final AccountAttributeMetadataMapper accountAttributeMetadataMapper;

    private final AccountAttributeMetadataSearchRepository accountAttributeMetadataSearchRepository;

    public AccountAttributeMetadataServiceImpl(
        AccountAttributeMetadataRepository accountAttributeMetadataRepository,
        AccountAttributeMetadataMapper accountAttributeMetadataMapper,
        AccountAttributeMetadataSearchRepository accountAttributeMetadataSearchRepository
    ) {
        this.accountAttributeMetadataRepository = accountAttributeMetadataRepository;
        this.accountAttributeMetadataMapper = accountAttributeMetadataMapper;
        this.accountAttributeMetadataSearchRepository = accountAttributeMetadataSearchRepository;
    }

    @Override
    public AccountAttributeMetadataDTO save(AccountAttributeMetadataDTO accountAttributeMetadataDTO) {
        log.debug("Request to save AccountAttributeMetadata : {}", accountAttributeMetadataDTO);
        AccountAttributeMetadata accountAttributeMetadata = accountAttributeMetadataMapper.toEntity(accountAttributeMetadataDTO);
        accountAttributeMetadata = accountAttributeMetadataRepository.save(accountAttributeMetadata);
        AccountAttributeMetadataDTO result = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);
        accountAttributeMetadataSearchRepository.save(accountAttributeMetadata);
        return result;
    }

    @Override
    public Optional<AccountAttributeMetadataDTO> partialUpdate(AccountAttributeMetadataDTO accountAttributeMetadataDTO) {
        log.debug("Request to partially update AccountAttributeMetadata : {}", accountAttributeMetadataDTO);

        return accountAttributeMetadataRepository
            .findById(accountAttributeMetadataDTO.getId())
            .map(existingAccountAttributeMetadata -> {
                accountAttributeMetadataMapper.partialUpdate(existingAccountAttributeMetadata, accountAttributeMetadataDTO);

                return existingAccountAttributeMetadata;
            })
            .map(accountAttributeMetadataRepository::save)
            .map(savedAccountAttributeMetadata -> {
                accountAttributeMetadataSearchRepository.save(savedAccountAttributeMetadata);

                return savedAccountAttributeMetadata;
            })
            .map(accountAttributeMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountAttributeMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountAttributeMetadata");
        return accountAttributeMetadataRepository.findAll(pageable).map(accountAttributeMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountAttributeMetadataDTO> findOne(Long id) {
        log.debug("Request to get AccountAttributeMetadata : {}", id);
        return accountAttributeMetadataRepository.findById(id).map(accountAttributeMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountAttributeMetadata : {}", id);
        accountAttributeMetadataRepository.deleteById(id);
        accountAttributeMetadataSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountAttributeMetadataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountAttributeMetadata for query {}", query);
        return accountAttributeMetadataSearchRepository.search(query, pageable).map(accountAttributeMetadataMapper::toDto);
    }
}
