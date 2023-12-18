package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.domain.WeeklyCashHolding;
import io.github.erp.repository.WeeklyCashHoldingRepository;
import io.github.erp.repository.search.WeeklyCashHoldingSearchRepository;
import io.github.erp.service.WeeklyCashHoldingService;
import io.github.erp.service.dto.WeeklyCashHoldingDTO;
import io.github.erp.service.mapper.WeeklyCashHoldingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WeeklyCashHolding}.
 */
@Service
@Transactional
public class WeeklyCashHoldingServiceImpl implements WeeklyCashHoldingService {

    private final Logger log = LoggerFactory.getLogger(WeeklyCashHoldingServiceImpl.class);

    private final WeeklyCashHoldingRepository weeklyCashHoldingRepository;

    private final WeeklyCashHoldingMapper weeklyCashHoldingMapper;

    private final WeeklyCashHoldingSearchRepository weeklyCashHoldingSearchRepository;

    public WeeklyCashHoldingServiceImpl(
        WeeklyCashHoldingRepository weeklyCashHoldingRepository,
        WeeklyCashHoldingMapper weeklyCashHoldingMapper,
        WeeklyCashHoldingSearchRepository weeklyCashHoldingSearchRepository
    ) {
        this.weeklyCashHoldingRepository = weeklyCashHoldingRepository;
        this.weeklyCashHoldingMapper = weeklyCashHoldingMapper;
        this.weeklyCashHoldingSearchRepository = weeklyCashHoldingSearchRepository;
    }

    @Override
    public WeeklyCashHoldingDTO save(WeeklyCashHoldingDTO weeklyCashHoldingDTO) {
        log.debug("Request to save WeeklyCashHolding : {}", weeklyCashHoldingDTO);
        WeeklyCashHolding weeklyCashHolding = weeklyCashHoldingMapper.toEntity(weeklyCashHoldingDTO);
        weeklyCashHolding = weeklyCashHoldingRepository.save(weeklyCashHolding);
        WeeklyCashHoldingDTO result = weeklyCashHoldingMapper.toDto(weeklyCashHolding);
        weeklyCashHoldingSearchRepository.save(weeklyCashHolding);
        return result;
    }

    @Override
    public Optional<WeeklyCashHoldingDTO> partialUpdate(WeeklyCashHoldingDTO weeklyCashHoldingDTO) {
        log.debug("Request to partially update WeeklyCashHolding : {}", weeklyCashHoldingDTO);

        return weeklyCashHoldingRepository
            .findById(weeklyCashHoldingDTO.getId())
            .map(existingWeeklyCashHolding -> {
                weeklyCashHoldingMapper.partialUpdate(existingWeeklyCashHolding, weeklyCashHoldingDTO);

                return existingWeeklyCashHolding;
            })
            .map(weeklyCashHoldingRepository::save)
            .map(savedWeeklyCashHolding -> {
                weeklyCashHoldingSearchRepository.save(savedWeeklyCashHolding);

                return savedWeeklyCashHolding;
            })
            .map(weeklyCashHoldingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeeklyCashHoldingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeeklyCashHoldings");
        return weeklyCashHoldingRepository.findAll(pageable).map(weeklyCashHoldingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeeklyCashHoldingDTO> findOne(Long id) {
        log.debug("Request to get WeeklyCashHolding : {}", id);
        return weeklyCashHoldingRepository.findById(id).map(weeklyCashHoldingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WeeklyCashHolding : {}", id);
        weeklyCashHoldingRepository.deleteById(id);
        weeklyCashHoldingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeeklyCashHoldingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WeeklyCashHoldings for query {}", query);
        return weeklyCashHoldingSearchRepository.search(query, pageable).map(weeklyCashHoldingMapper::toDto);
    }
}
