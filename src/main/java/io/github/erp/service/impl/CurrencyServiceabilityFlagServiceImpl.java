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
