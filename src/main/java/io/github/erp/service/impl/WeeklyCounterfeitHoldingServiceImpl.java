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

import io.github.erp.domain.WeeklyCounterfeitHolding;
import io.github.erp.repository.WeeklyCounterfeitHoldingRepository;
import io.github.erp.repository.search.WeeklyCounterfeitHoldingSearchRepository;
import io.github.erp.service.WeeklyCounterfeitHoldingService;
import io.github.erp.service.dto.WeeklyCounterfeitHoldingDTO;
import io.github.erp.service.mapper.WeeklyCounterfeitHoldingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WeeklyCounterfeitHolding}.
 */
@Service
@Transactional
public class WeeklyCounterfeitHoldingServiceImpl implements WeeklyCounterfeitHoldingService {

    private final Logger log = LoggerFactory.getLogger(WeeklyCounterfeitHoldingServiceImpl.class);

    private final WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository;

    private final WeeklyCounterfeitHoldingMapper weeklyCounterfeitHoldingMapper;

    private final WeeklyCounterfeitHoldingSearchRepository weeklyCounterfeitHoldingSearchRepository;

    public WeeklyCounterfeitHoldingServiceImpl(
        WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository,
        WeeklyCounterfeitHoldingMapper weeklyCounterfeitHoldingMapper,
        WeeklyCounterfeitHoldingSearchRepository weeklyCounterfeitHoldingSearchRepository
    ) {
        this.weeklyCounterfeitHoldingRepository = weeklyCounterfeitHoldingRepository;
        this.weeklyCounterfeitHoldingMapper = weeklyCounterfeitHoldingMapper;
        this.weeklyCounterfeitHoldingSearchRepository = weeklyCounterfeitHoldingSearchRepository;
    }

    @Override
    public WeeklyCounterfeitHoldingDTO save(WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO) {
        log.debug("Request to save WeeklyCounterfeitHolding : {}", weeklyCounterfeitHoldingDTO);
        WeeklyCounterfeitHolding weeklyCounterfeitHolding = weeklyCounterfeitHoldingMapper.toEntity(weeklyCounterfeitHoldingDTO);
        weeklyCounterfeitHolding = weeklyCounterfeitHoldingRepository.save(weeklyCounterfeitHolding);
        WeeklyCounterfeitHoldingDTO result = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);
        weeklyCounterfeitHoldingSearchRepository.save(weeklyCounterfeitHolding);
        return result;
    }

    @Override
    public Optional<WeeklyCounterfeitHoldingDTO> partialUpdate(WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO) {
        log.debug("Request to partially update WeeklyCounterfeitHolding : {}", weeklyCounterfeitHoldingDTO);

        return weeklyCounterfeitHoldingRepository
            .findById(weeklyCounterfeitHoldingDTO.getId())
            .map(existingWeeklyCounterfeitHolding -> {
                weeklyCounterfeitHoldingMapper.partialUpdate(existingWeeklyCounterfeitHolding, weeklyCounterfeitHoldingDTO);

                return existingWeeklyCounterfeitHolding;
            })
            .map(weeklyCounterfeitHoldingRepository::save)
            .map(savedWeeklyCounterfeitHolding -> {
                weeklyCounterfeitHoldingSearchRepository.save(savedWeeklyCounterfeitHolding);

                return savedWeeklyCounterfeitHolding;
            })
            .map(weeklyCounterfeitHoldingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeeklyCounterfeitHoldingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeeklyCounterfeitHoldings");
        return weeklyCounterfeitHoldingRepository.findAll(pageable).map(weeklyCounterfeitHoldingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeeklyCounterfeitHoldingDTO> findOne(Long id) {
        log.debug("Request to get WeeklyCounterfeitHolding : {}", id);
        return weeklyCounterfeitHoldingRepository.findById(id).map(weeklyCounterfeitHoldingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WeeklyCounterfeitHolding : {}", id);
        weeklyCounterfeitHoldingRepository.deleteById(id);
        weeklyCounterfeitHoldingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeeklyCounterfeitHoldingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WeeklyCounterfeitHoldings for query {}", query);
        return weeklyCounterfeitHoldingSearchRepository.search(query, pageable).map(weeklyCounterfeitHoldingMapper::toDto);
    }
}
