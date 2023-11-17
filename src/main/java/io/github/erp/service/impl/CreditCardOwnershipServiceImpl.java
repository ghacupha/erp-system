package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.CreditCardOwnership;
import io.github.erp.repository.CreditCardOwnershipRepository;
import io.github.erp.repository.search.CreditCardOwnershipSearchRepository;
import io.github.erp.service.CreditCardOwnershipService;
import io.github.erp.service.dto.CreditCardOwnershipDTO;
import io.github.erp.service.mapper.CreditCardOwnershipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CreditCardOwnership}.
 */
@Service
@Transactional
public class CreditCardOwnershipServiceImpl implements CreditCardOwnershipService {

    private final Logger log = LoggerFactory.getLogger(CreditCardOwnershipServiceImpl.class);

    private final CreditCardOwnershipRepository creditCardOwnershipRepository;

    private final CreditCardOwnershipMapper creditCardOwnershipMapper;

    private final CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository;

    public CreditCardOwnershipServiceImpl(
        CreditCardOwnershipRepository creditCardOwnershipRepository,
        CreditCardOwnershipMapper creditCardOwnershipMapper,
        CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository
    ) {
        this.creditCardOwnershipRepository = creditCardOwnershipRepository;
        this.creditCardOwnershipMapper = creditCardOwnershipMapper;
        this.creditCardOwnershipSearchRepository = creditCardOwnershipSearchRepository;
    }

    @Override
    public CreditCardOwnershipDTO save(CreditCardOwnershipDTO creditCardOwnershipDTO) {
        log.debug("Request to save CreditCardOwnership : {}", creditCardOwnershipDTO);
        CreditCardOwnership creditCardOwnership = creditCardOwnershipMapper.toEntity(creditCardOwnershipDTO);
        creditCardOwnership = creditCardOwnershipRepository.save(creditCardOwnership);
        CreditCardOwnershipDTO result = creditCardOwnershipMapper.toDto(creditCardOwnership);
        creditCardOwnershipSearchRepository.save(creditCardOwnership);
        return result;
    }

    @Override
    public Optional<CreditCardOwnershipDTO> partialUpdate(CreditCardOwnershipDTO creditCardOwnershipDTO) {
        log.debug("Request to partially update CreditCardOwnership : {}", creditCardOwnershipDTO);

        return creditCardOwnershipRepository
            .findById(creditCardOwnershipDTO.getId())
            .map(existingCreditCardOwnership -> {
                creditCardOwnershipMapper.partialUpdate(existingCreditCardOwnership, creditCardOwnershipDTO);

                return existingCreditCardOwnership;
            })
            .map(creditCardOwnershipRepository::save)
            .map(savedCreditCardOwnership -> {
                creditCardOwnershipSearchRepository.save(savedCreditCardOwnership);

                return savedCreditCardOwnership;
            })
            .map(creditCardOwnershipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardOwnershipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreditCardOwnerships");
        return creditCardOwnershipRepository.findAll(pageable).map(creditCardOwnershipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardOwnershipDTO> findOne(Long id) {
        log.debug("Request to get CreditCardOwnership : {}", id);
        return creditCardOwnershipRepository.findById(id).map(creditCardOwnershipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreditCardOwnership : {}", id);
        creditCardOwnershipRepository.deleteById(id);
        creditCardOwnershipSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardOwnershipDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CreditCardOwnerships for query {}", query);
        return creditCardOwnershipSearchRepository.search(query, pageable).map(creditCardOwnershipMapper::toDto);
    }
}
