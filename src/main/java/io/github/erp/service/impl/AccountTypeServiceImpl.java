package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
