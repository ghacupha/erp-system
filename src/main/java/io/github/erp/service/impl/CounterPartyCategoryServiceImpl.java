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

import io.github.erp.domain.CounterPartyCategory;
import io.github.erp.repository.CounterPartyCategoryRepository;
import io.github.erp.repository.search.CounterPartyCategorySearchRepository;
import io.github.erp.service.CounterPartyCategoryService;
import io.github.erp.service.dto.CounterPartyCategoryDTO;
import io.github.erp.service.mapper.CounterPartyCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CounterPartyCategory}.
 */
@Service
@Transactional
public class CounterPartyCategoryServiceImpl implements CounterPartyCategoryService {

    private final Logger log = LoggerFactory.getLogger(CounterPartyCategoryServiceImpl.class);

    private final CounterPartyCategoryRepository counterPartyCategoryRepository;

    private final CounterPartyCategoryMapper counterPartyCategoryMapper;

    private final CounterPartyCategorySearchRepository counterPartyCategorySearchRepository;

    public CounterPartyCategoryServiceImpl(
        CounterPartyCategoryRepository counterPartyCategoryRepository,
        CounterPartyCategoryMapper counterPartyCategoryMapper,
        CounterPartyCategorySearchRepository counterPartyCategorySearchRepository
    ) {
        this.counterPartyCategoryRepository = counterPartyCategoryRepository;
        this.counterPartyCategoryMapper = counterPartyCategoryMapper;
        this.counterPartyCategorySearchRepository = counterPartyCategorySearchRepository;
    }

    @Override
    public CounterPartyCategoryDTO save(CounterPartyCategoryDTO counterPartyCategoryDTO) {
        log.debug("Request to save CounterPartyCategory : {}", counterPartyCategoryDTO);
        CounterPartyCategory counterPartyCategory = counterPartyCategoryMapper.toEntity(counterPartyCategoryDTO);
        counterPartyCategory = counterPartyCategoryRepository.save(counterPartyCategory);
        CounterPartyCategoryDTO result = counterPartyCategoryMapper.toDto(counterPartyCategory);
        counterPartyCategorySearchRepository.save(counterPartyCategory);
        return result;
    }

    @Override
    public Optional<CounterPartyCategoryDTO> partialUpdate(CounterPartyCategoryDTO counterPartyCategoryDTO) {
        log.debug("Request to partially update CounterPartyCategory : {}", counterPartyCategoryDTO);

        return counterPartyCategoryRepository
            .findById(counterPartyCategoryDTO.getId())
            .map(existingCounterPartyCategory -> {
                counterPartyCategoryMapper.partialUpdate(existingCounterPartyCategory, counterPartyCategoryDTO);

                return existingCounterPartyCategory;
            })
            .map(counterPartyCategoryRepository::save)
            .map(savedCounterPartyCategory -> {
                counterPartyCategorySearchRepository.save(savedCounterPartyCategory);

                return savedCounterPartyCategory;
            })
            .map(counterPartyCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterPartyCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CounterPartyCategories");
        return counterPartyCategoryRepository.findAll(pageable).map(counterPartyCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CounterPartyCategoryDTO> findOne(Long id) {
        log.debug("Request to get CounterPartyCategory : {}", id);
        return counterPartyCategoryRepository.findById(id).map(counterPartyCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CounterPartyCategory : {}", id);
        counterPartyCategoryRepository.deleteById(id);
        counterPartyCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterPartyCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CounterPartyCategories for query {}", query);
        return counterPartyCategorySearchRepository.search(query, pageable).map(counterPartyCategoryMapper::toDto);
    }
}
