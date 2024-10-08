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
