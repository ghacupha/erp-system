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
