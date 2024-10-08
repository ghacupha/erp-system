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
