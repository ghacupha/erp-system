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

import io.github.erp.domain.CounterpartyType;
import io.github.erp.repository.CounterpartyTypeRepository;
import io.github.erp.repository.search.CounterpartyTypeSearchRepository;
import io.github.erp.service.CounterpartyTypeService;
import io.github.erp.service.dto.CounterpartyTypeDTO;
import io.github.erp.service.mapper.CounterpartyTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CounterpartyType}.
 */
@Service
@Transactional
public class CounterpartyTypeServiceImpl implements CounterpartyTypeService {

    private final Logger log = LoggerFactory.getLogger(CounterpartyTypeServiceImpl.class);

    private final CounterpartyTypeRepository counterpartyTypeRepository;

    private final CounterpartyTypeMapper counterpartyTypeMapper;

    private final CounterpartyTypeSearchRepository counterpartyTypeSearchRepository;

    public CounterpartyTypeServiceImpl(
        CounterpartyTypeRepository counterpartyTypeRepository,
        CounterpartyTypeMapper counterpartyTypeMapper,
        CounterpartyTypeSearchRepository counterpartyTypeSearchRepository
    ) {
        this.counterpartyTypeRepository = counterpartyTypeRepository;
        this.counterpartyTypeMapper = counterpartyTypeMapper;
        this.counterpartyTypeSearchRepository = counterpartyTypeSearchRepository;
    }

    @Override
    public CounterpartyTypeDTO save(CounterpartyTypeDTO counterpartyTypeDTO) {
        log.debug("Request to save CounterpartyType : {}", counterpartyTypeDTO);
        CounterpartyType counterpartyType = counterpartyTypeMapper.toEntity(counterpartyTypeDTO);
        counterpartyType = counterpartyTypeRepository.save(counterpartyType);
        CounterpartyTypeDTO result = counterpartyTypeMapper.toDto(counterpartyType);
        counterpartyTypeSearchRepository.save(counterpartyType);
        return result;
    }

    @Override
    public Optional<CounterpartyTypeDTO> partialUpdate(CounterpartyTypeDTO counterpartyTypeDTO) {
        log.debug("Request to partially update CounterpartyType : {}", counterpartyTypeDTO);

        return counterpartyTypeRepository
            .findById(counterpartyTypeDTO.getId())
            .map(existingCounterpartyType -> {
                counterpartyTypeMapper.partialUpdate(existingCounterpartyType, counterpartyTypeDTO);

                return existingCounterpartyType;
            })
            .map(counterpartyTypeRepository::save)
            .map(savedCounterpartyType -> {
                counterpartyTypeSearchRepository.save(savedCounterpartyType);

                return savedCounterpartyType;
            })
            .map(counterpartyTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterpartyTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CounterpartyTypes");
        return counterpartyTypeRepository.findAll(pageable).map(counterpartyTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CounterpartyTypeDTO> findOne(Long id) {
        log.debug("Request to get CounterpartyType : {}", id);
        return counterpartyTypeRepository.findById(id).map(counterpartyTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CounterpartyType : {}", id);
        counterpartyTypeRepository.deleteById(id);
        counterpartyTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterpartyTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CounterpartyTypes for query {}", query);
        return counterpartyTypeSearchRepository.search(query, pageable).map(counterpartyTypeMapper::toDto);
    }
}
