package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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

import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.SettlementCurrencyRepository;
import io.github.erp.repository.search.SettlementCurrencySearchRepository;
import io.github.erp.service.SettlementCurrencyService;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.mapper.SettlementCurrencyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SettlementCurrency}.
 */
@Service
@Transactional
public class SettlementCurrencyServiceImpl implements SettlementCurrencyService {

    private final Logger log = LoggerFactory.getLogger(SettlementCurrencyServiceImpl.class);

    private final SettlementCurrencyRepository settlementCurrencyRepository;

    private final SettlementCurrencyMapper settlementCurrencyMapper;

    private final SettlementCurrencySearchRepository settlementCurrencySearchRepository;

    public SettlementCurrencyServiceImpl(
        SettlementCurrencyRepository settlementCurrencyRepository,
        SettlementCurrencyMapper settlementCurrencyMapper,
        SettlementCurrencySearchRepository settlementCurrencySearchRepository
    ) {
        this.settlementCurrencyRepository = settlementCurrencyRepository;
        this.settlementCurrencyMapper = settlementCurrencyMapper;
        this.settlementCurrencySearchRepository = settlementCurrencySearchRepository;
    }

    @Override
    public SettlementCurrencyDTO save(SettlementCurrencyDTO settlementCurrencyDTO) {
        log.debug("Request to save SettlementCurrency : {}", settlementCurrencyDTO);
        SettlementCurrency settlementCurrency = settlementCurrencyMapper.toEntity(settlementCurrencyDTO);
        settlementCurrency = settlementCurrencyRepository.save(settlementCurrency);
        SettlementCurrencyDTO result = settlementCurrencyMapper.toDto(settlementCurrency);
        settlementCurrencySearchRepository.save(settlementCurrency);
        return result;
    }

    @Override
    public Optional<SettlementCurrencyDTO> partialUpdate(SettlementCurrencyDTO settlementCurrencyDTO) {
        log.debug("Request to partially update SettlementCurrency : {}", settlementCurrencyDTO);

        return settlementCurrencyRepository
            .findById(settlementCurrencyDTO.getId())
            .map(existingSettlementCurrency -> {
                settlementCurrencyMapper.partialUpdate(existingSettlementCurrency, settlementCurrencyDTO);

                return existingSettlementCurrency;
            })
            .map(settlementCurrencyRepository::save)
            .map(savedSettlementCurrency -> {
                settlementCurrencySearchRepository.save(savedSettlementCurrency);

                return savedSettlementCurrency;
            })
            .map(settlementCurrencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementCurrencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SettlementCurrencies");
        return settlementCurrencyRepository.findAll(pageable).map(settlementCurrencyMapper::toDto);
    }

    public Page<SettlementCurrencyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return settlementCurrencyRepository.findAllWithEagerRelationships(pageable).map(settlementCurrencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SettlementCurrencyDTO> findOne(Long id) {
        log.debug("Request to get SettlementCurrency : {}", id);
        return settlementCurrencyRepository.findOneWithEagerRelationships(id).map(settlementCurrencyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SettlementCurrency : {}", id);
        settlementCurrencyRepository.deleteById(id);
        settlementCurrencySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementCurrencyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SettlementCurrencies for query {}", query);
        return settlementCurrencySearchRepository.search(query, pageable).map(settlementCurrencyMapper::toDto);
    }
}
