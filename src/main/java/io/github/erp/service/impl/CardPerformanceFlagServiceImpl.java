package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.CardPerformanceFlag;
import io.github.erp.repository.CardPerformanceFlagRepository;
import io.github.erp.repository.search.CardPerformanceFlagSearchRepository;
import io.github.erp.service.CardPerformanceFlagService;
import io.github.erp.service.dto.CardPerformanceFlagDTO;
import io.github.erp.service.mapper.CardPerformanceFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardPerformanceFlag}.
 */
@Service
@Transactional
public class CardPerformanceFlagServiceImpl implements CardPerformanceFlagService {

    private final Logger log = LoggerFactory.getLogger(CardPerformanceFlagServiceImpl.class);

    private final CardPerformanceFlagRepository cardPerformanceFlagRepository;

    private final CardPerformanceFlagMapper cardPerformanceFlagMapper;

    private final CardPerformanceFlagSearchRepository cardPerformanceFlagSearchRepository;

    public CardPerformanceFlagServiceImpl(
        CardPerformanceFlagRepository cardPerformanceFlagRepository,
        CardPerformanceFlagMapper cardPerformanceFlagMapper,
        CardPerformanceFlagSearchRepository cardPerformanceFlagSearchRepository
    ) {
        this.cardPerformanceFlagRepository = cardPerformanceFlagRepository;
        this.cardPerformanceFlagMapper = cardPerformanceFlagMapper;
        this.cardPerformanceFlagSearchRepository = cardPerformanceFlagSearchRepository;
    }

    @Override
    public CardPerformanceFlagDTO save(CardPerformanceFlagDTO cardPerformanceFlagDTO) {
        log.debug("Request to save CardPerformanceFlag : {}", cardPerformanceFlagDTO);
        CardPerformanceFlag cardPerformanceFlag = cardPerformanceFlagMapper.toEntity(cardPerformanceFlagDTO);
        cardPerformanceFlag = cardPerformanceFlagRepository.save(cardPerformanceFlag);
        CardPerformanceFlagDTO result = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);
        cardPerformanceFlagSearchRepository.save(cardPerformanceFlag);
        return result;
    }

    @Override
    public Optional<CardPerformanceFlagDTO> partialUpdate(CardPerformanceFlagDTO cardPerformanceFlagDTO) {
        log.debug("Request to partially update CardPerformanceFlag : {}", cardPerformanceFlagDTO);

        return cardPerformanceFlagRepository
            .findById(cardPerformanceFlagDTO.getId())
            .map(existingCardPerformanceFlag -> {
                cardPerformanceFlagMapper.partialUpdate(existingCardPerformanceFlag, cardPerformanceFlagDTO);

                return existingCardPerformanceFlag;
            })
            .map(cardPerformanceFlagRepository::save)
            .map(savedCardPerformanceFlag -> {
                cardPerformanceFlagSearchRepository.save(savedCardPerformanceFlag);

                return savedCardPerformanceFlag;
            })
            .map(cardPerformanceFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardPerformanceFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardPerformanceFlags");
        return cardPerformanceFlagRepository.findAll(pageable).map(cardPerformanceFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardPerformanceFlagDTO> findOne(Long id) {
        log.debug("Request to get CardPerformanceFlag : {}", id);
        return cardPerformanceFlagRepository.findById(id).map(cardPerformanceFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardPerformanceFlag : {}", id);
        cardPerformanceFlagRepository.deleteById(id);
        cardPerformanceFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardPerformanceFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardPerformanceFlags for query {}", query);
        return cardPerformanceFlagSearchRepository.search(query, pageable).map(cardPerformanceFlagMapper::toDto);
    }
}
