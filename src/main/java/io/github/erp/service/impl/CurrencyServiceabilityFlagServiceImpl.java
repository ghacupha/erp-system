package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import io.github.erp.domain.CurrencyServiceabilityFlag;
import io.github.erp.repository.CurrencyServiceabilityFlagRepository;
import io.github.erp.repository.search.CurrencyServiceabilityFlagSearchRepository;
import io.github.erp.service.CurrencyServiceabilityFlagService;
import io.github.erp.service.dto.CurrencyServiceabilityFlagDTO;
import io.github.erp.service.mapper.CurrencyServiceabilityFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CurrencyServiceabilityFlag}.
 */
@Service
@Transactional
public class CurrencyServiceabilityFlagServiceImpl implements CurrencyServiceabilityFlagService {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceabilityFlagServiceImpl.class);

    private final CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository;

    private final CurrencyServiceabilityFlagMapper currencyServiceabilityFlagMapper;

    private final CurrencyServiceabilityFlagSearchRepository currencyServiceabilityFlagSearchRepository;

    public CurrencyServiceabilityFlagServiceImpl(
        CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository,
        CurrencyServiceabilityFlagMapper currencyServiceabilityFlagMapper,
        CurrencyServiceabilityFlagSearchRepository currencyServiceabilityFlagSearchRepository
    ) {
        this.currencyServiceabilityFlagRepository = currencyServiceabilityFlagRepository;
        this.currencyServiceabilityFlagMapper = currencyServiceabilityFlagMapper;
        this.currencyServiceabilityFlagSearchRepository = currencyServiceabilityFlagSearchRepository;
    }

    @Override
    public CurrencyServiceabilityFlagDTO save(CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO) {
        log.debug("Request to save CurrencyServiceabilityFlag : {}", currencyServiceabilityFlagDTO);
        CurrencyServiceabilityFlag currencyServiceabilityFlag = currencyServiceabilityFlagMapper.toEntity(currencyServiceabilityFlagDTO);
        currencyServiceabilityFlag = currencyServiceabilityFlagRepository.save(currencyServiceabilityFlag);
        CurrencyServiceabilityFlagDTO result = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);
        currencyServiceabilityFlagSearchRepository.save(currencyServiceabilityFlag);
        return result;
    }

    @Override
    public Optional<CurrencyServiceabilityFlagDTO> partialUpdate(CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO) {
        log.debug("Request to partially update CurrencyServiceabilityFlag : {}", currencyServiceabilityFlagDTO);

        return currencyServiceabilityFlagRepository
            .findById(currencyServiceabilityFlagDTO.getId())
            .map(existingCurrencyServiceabilityFlag -> {
                currencyServiceabilityFlagMapper.partialUpdate(existingCurrencyServiceabilityFlag, currencyServiceabilityFlagDTO);

                return existingCurrencyServiceabilityFlag;
            })
            .map(currencyServiceabilityFlagRepository::save)
            .map(savedCurrencyServiceabilityFlag -> {
                currencyServiceabilityFlagSearchRepository.save(savedCurrencyServiceabilityFlag);

                return savedCurrencyServiceabilityFlag;
            })
            .map(currencyServiceabilityFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyServiceabilityFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyServiceabilityFlags");
        return currencyServiceabilityFlagRepository.findAll(pageable).map(currencyServiceabilityFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyServiceabilityFlagDTO> findOne(Long id) {
        log.debug("Request to get CurrencyServiceabilityFlag : {}", id);
        return currencyServiceabilityFlagRepository.findById(id).map(currencyServiceabilityFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CurrencyServiceabilityFlag : {}", id);
        currencyServiceabilityFlagRepository.deleteById(id);
        currencyServiceabilityFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyServiceabilityFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CurrencyServiceabilityFlags for query {}", query);
        return currencyServiceabilityFlagSearchRepository.search(query, pageable).map(currencyServiceabilityFlagMapper::toDto);
    }
}
