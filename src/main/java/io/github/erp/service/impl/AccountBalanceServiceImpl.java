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

import io.github.erp.domain.AccountBalance;
import io.github.erp.repository.AccountBalanceRepository;
import io.github.erp.repository.search.AccountBalanceSearchRepository;
import io.github.erp.service.AccountBalanceService;
import io.github.erp.service.dto.AccountBalanceDTO;
import io.github.erp.service.mapper.AccountBalanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountBalance}.
 */
@Service
@Transactional
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(AccountBalanceServiceImpl.class);

    private final AccountBalanceRepository accountBalanceRepository;

    private final AccountBalanceMapper accountBalanceMapper;

    private final AccountBalanceSearchRepository accountBalanceSearchRepository;

    public AccountBalanceServiceImpl(
        AccountBalanceRepository accountBalanceRepository,
        AccountBalanceMapper accountBalanceMapper,
        AccountBalanceSearchRepository accountBalanceSearchRepository
    ) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.accountBalanceMapper = accountBalanceMapper;
        this.accountBalanceSearchRepository = accountBalanceSearchRepository;
    }

    @Override
    public AccountBalanceDTO save(AccountBalanceDTO accountBalanceDTO) {
        log.debug("Request to save AccountBalance : {}", accountBalanceDTO);
        AccountBalance accountBalance = accountBalanceMapper.toEntity(accountBalanceDTO);
        accountBalance = accountBalanceRepository.save(accountBalance);
        AccountBalanceDTO result = accountBalanceMapper.toDto(accountBalance);
        accountBalanceSearchRepository.save(accountBalance);
        return result;
    }

    @Override
    public Optional<AccountBalanceDTO> partialUpdate(AccountBalanceDTO accountBalanceDTO) {
        log.debug("Request to partially update AccountBalance : {}", accountBalanceDTO);

        return accountBalanceRepository
            .findById(accountBalanceDTO.getId())
            .map(existingAccountBalance -> {
                accountBalanceMapper.partialUpdate(existingAccountBalance, accountBalanceDTO);

                return existingAccountBalance;
            })
            .map(accountBalanceRepository::save)
            .map(savedAccountBalance -> {
                accountBalanceSearchRepository.save(savedAccountBalance);

                return savedAccountBalance;
            })
            .map(accountBalanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountBalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountBalances");
        return accountBalanceRepository.findAll(pageable).map(accountBalanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountBalanceDTO> findOne(Long id) {
        log.debug("Request to get AccountBalance : {}", id);
        return accountBalanceRepository.findById(id).map(accountBalanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountBalance : {}", id);
        accountBalanceRepository.deleteById(id);
        accountBalanceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountBalanceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountBalances for query {}", query);
        return accountBalanceSearchRepository.search(query, pageable).map(accountBalanceMapper::toDto);
    }
}
