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
