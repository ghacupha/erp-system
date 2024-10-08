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

import io.github.erp.domain.BankTransactionType;
import io.github.erp.repository.BankTransactionTypeRepository;
import io.github.erp.repository.search.BankTransactionTypeSearchRepository;
import io.github.erp.service.BankTransactionTypeService;
import io.github.erp.service.dto.BankTransactionTypeDTO;
import io.github.erp.service.mapper.BankTransactionTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BankTransactionType}.
 */
@Service
@Transactional
public class BankTransactionTypeServiceImpl implements BankTransactionTypeService {

    private final Logger log = LoggerFactory.getLogger(BankTransactionTypeServiceImpl.class);

    private final BankTransactionTypeRepository bankTransactionTypeRepository;

    private final BankTransactionTypeMapper bankTransactionTypeMapper;

    private final BankTransactionTypeSearchRepository bankTransactionTypeSearchRepository;

    public BankTransactionTypeServiceImpl(
        BankTransactionTypeRepository bankTransactionTypeRepository,
        BankTransactionTypeMapper bankTransactionTypeMapper,
        BankTransactionTypeSearchRepository bankTransactionTypeSearchRepository
    ) {
        this.bankTransactionTypeRepository = bankTransactionTypeRepository;
        this.bankTransactionTypeMapper = bankTransactionTypeMapper;
        this.bankTransactionTypeSearchRepository = bankTransactionTypeSearchRepository;
    }

    @Override
    public BankTransactionTypeDTO save(BankTransactionTypeDTO bankTransactionTypeDTO) {
        log.debug("Request to save BankTransactionType : {}", bankTransactionTypeDTO);
        BankTransactionType bankTransactionType = bankTransactionTypeMapper.toEntity(bankTransactionTypeDTO);
        bankTransactionType = bankTransactionTypeRepository.save(bankTransactionType);
        BankTransactionTypeDTO result = bankTransactionTypeMapper.toDto(bankTransactionType);
        bankTransactionTypeSearchRepository.save(bankTransactionType);
        return result;
    }

    @Override
    public Optional<BankTransactionTypeDTO> partialUpdate(BankTransactionTypeDTO bankTransactionTypeDTO) {
        log.debug("Request to partially update BankTransactionType : {}", bankTransactionTypeDTO);

        return bankTransactionTypeRepository
            .findById(bankTransactionTypeDTO.getId())
            .map(existingBankTransactionType -> {
                bankTransactionTypeMapper.partialUpdate(existingBankTransactionType, bankTransactionTypeDTO);

                return existingBankTransactionType;
            })
            .map(bankTransactionTypeRepository::save)
            .map(savedBankTransactionType -> {
                bankTransactionTypeSearchRepository.save(savedBankTransactionType);

                return savedBankTransactionType;
            })
            .map(bankTransactionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankTransactionTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankTransactionTypes");
        return bankTransactionTypeRepository.findAll(pageable).map(bankTransactionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankTransactionTypeDTO> findOne(Long id) {
        log.debug("Request to get BankTransactionType : {}", id);
        return bankTransactionTypeRepository.findById(id).map(bankTransactionTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankTransactionType : {}", id);
        bankTransactionTypeRepository.deleteById(id);
        bankTransactionTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankTransactionTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BankTransactionTypes for query {}", query);
        return bankTransactionTypeSearchRepository.search(query, pageable).map(bankTransactionTypeMapper::toDto);
    }
}
