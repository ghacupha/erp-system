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
