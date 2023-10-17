package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.CurrencyAuthenticityFlag;
import io.github.erp.repository.CurrencyAuthenticityFlagRepository;
import io.github.erp.repository.search.CurrencyAuthenticityFlagSearchRepository;
import io.github.erp.service.CurrencyAuthenticityFlagService;
import io.github.erp.service.dto.CurrencyAuthenticityFlagDTO;
import io.github.erp.service.mapper.CurrencyAuthenticityFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CurrencyAuthenticityFlag}.
 */
@Service
@Transactional
public class CurrencyAuthenticityFlagServiceImpl implements CurrencyAuthenticityFlagService {

    private final Logger log = LoggerFactory.getLogger(CurrencyAuthenticityFlagServiceImpl.class);

    private final CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository;

    private final CurrencyAuthenticityFlagMapper currencyAuthenticityFlagMapper;

    private final CurrencyAuthenticityFlagSearchRepository currencyAuthenticityFlagSearchRepository;

    public CurrencyAuthenticityFlagServiceImpl(
        CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository,
        CurrencyAuthenticityFlagMapper currencyAuthenticityFlagMapper,
        CurrencyAuthenticityFlagSearchRepository currencyAuthenticityFlagSearchRepository
    ) {
        this.currencyAuthenticityFlagRepository = currencyAuthenticityFlagRepository;
        this.currencyAuthenticityFlagMapper = currencyAuthenticityFlagMapper;
        this.currencyAuthenticityFlagSearchRepository = currencyAuthenticityFlagSearchRepository;
    }

    @Override
    public CurrencyAuthenticityFlagDTO save(CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO) {
        log.debug("Request to save CurrencyAuthenticityFlag : {}", currencyAuthenticityFlagDTO);
        CurrencyAuthenticityFlag currencyAuthenticityFlag = currencyAuthenticityFlagMapper.toEntity(currencyAuthenticityFlagDTO);
        currencyAuthenticityFlag = currencyAuthenticityFlagRepository.save(currencyAuthenticityFlag);
        CurrencyAuthenticityFlagDTO result = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);
        currencyAuthenticityFlagSearchRepository.save(currencyAuthenticityFlag);
        return result;
    }

    @Override
    public Optional<CurrencyAuthenticityFlagDTO> partialUpdate(CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO) {
        log.debug("Request to partially update CurrencyAuthenticityFlag : {}", currencyAuthenticityFlagDTO);

        return currencyAuthenticityFlagRepository
            .findById(currencyAuthenticityFlagDTO.getId())
            .map(existingCurrencyAuthenticityFlag -> {
                currencyAuthenticityFlagMapper.partialUpdate(existingCurrencyAuthenticityFlag, currencyAuthenticityFlagDTO);

                return existingCurrencyAuthenticityFlag;
            })
            .map(currencyAuthenticityFlagRepository::save)
            .map(savedCurrencyAuthenticityFlag -> {
                currencyAuthenticityFlagSearchRepository.save(savedCurrencyAuthenticityFlag);

                return savedCurrencyAuthenticityFlag;
            })
            .map(currencyAuthenticityFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyAuthenticityFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyAuthenticityFlags");
        return currencyAuthenticityFlagRepository.findAll(pageable).map(currencyAuthenticityFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyAuthenticityFlagDTO> findOne(Long id) {
        log.debug("Request to get CurrencyAuthenticityFlag : {}", id);
        return currencyAuthenticityFlagRepository.findById(id).map(currencyAuthenticityFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CurrencyAuthenticityFlag : {}", id);
        currencyAuthenticityFlagRepository.deleteById(id);
        currencyAuthenticityFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyAuthenticityFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CurrencyAuthenticityFlags for query {}", query);
        return currencyAuthenticityFlagSearchRepository.search(query, pageable).map(currencyAuthenticityFlagMapper::toDto);
    }
}
