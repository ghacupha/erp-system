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
