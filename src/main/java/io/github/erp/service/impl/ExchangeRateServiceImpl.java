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

import io.github.erp.domain.ExchangeRate;
import io.github.erp.repository.ExchangeRateRepository;
import io.github.erp.repository.search.ExchangeRateSearchRepository;
import io.github.erp.service.ExchangeRateService;
import io.github.erp.service.dto.ExchangeRateDTO;
import io.github.erp.service.mapper.ExchangeRateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExchangeRate}.
 */
@Service
@Transactional
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Logger log = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateMapper exchangeRateMapper;

    private final ExchangeRateSearchRepository exchangeRateSearchRepository;

    public ExchangeRateServiceImpl(
        ExchangeRateRepository exchangeRateRepository,
        ExchangeRateMapper exchangeRateMapper,
        ExchangeRateSearchRepository exchangeRateSearchRepository
    ) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
        this.exchangeRateSearchRepository = exchangeRateSearchRepository;
    }

    @Override
    public ExchangeRateDTO save(ExchangeRateDTO exchangeRateDTO) {
        log.debug("Request to save ExchangeRate : {}", exchangeRateDTO);
        ExchangeRate exchangeRate = exchangeRateMapper.toEntity(exchangeRateDTO);
        exchangeRate = exchangeRateRepository.save(exchangeRate);
        ExchangeRateDTO result = exchangeRateMapper.toDto(exchangeRate);
        exchangeRateSearchRepository.save(exchangeRate);
        return result;
    }

    @Override
    public Optional<ExchangeRateDTO> partialUpdate(ExchangeRateDTO exchangeRateDTO) {
        log.debug("Request to partially update ExchangeRate : {}", exchangeRateDTO);

        return exchangeRateRepository
            .findById(exchangeRateDTO.getId())
            .map(existingExchangeRate -> {
                exchangeRateMapper.partialUpdate(existingExchangeRate, exchangeRateDTO);

                return existingExchangeRate;
            })
            .map(exchangeRateRepository::save)
            .map(savedExchangeRate -> {
                exchangeRateSearchRepository.save(savedExchangeRate);

                return savedExchangeRate;
            })
            .map(exchangeRateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeRateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExchangeRates");
        return exchangeRateRepository.findAll(pageable).map(exchangeRateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExchangeRateDTO> findOne(Long id) {
        log.debug("Request to get ExchangeRate : {}", id);
        return exchangeRateRepository.findById(id).map(exchangeRateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExchangeRate : {}", id);
        exchangeRateRepository.deleteById(id);
        exchangeRateSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeRateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExchangeRates for query {}", query);
        return exchangeRateSearchRepository.search(query, pageable).map(exchangeRateMapper::toDto);
    }
}
