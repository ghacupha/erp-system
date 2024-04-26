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

import io.github.erp.domain.CounterPartyDealType;
import io.github.erp.repository.CounterPartyDealTypeRepository;
import io.github.erp.repository.search.CounterPartyDealTypeSearchRepository;
import io.github.erp.service.CounterPartyDealTypeService;
import io.github.erp.service.dto.CounterPartyDealTypeDTO;
import io.github.erp.service.mapper.CounterPartyDealTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CounterPartyDealType}.
 */
@Service
@Transactional
public class CounterPartyDealTypeServiceImpl implements CounterPartyDealTypeService {

    private final Logger log = LoggerFactory.getLogger(CounterPartyDealTypeServiceImpl.class);

    private final CounterPartyDealTypeRepository counterPartyDealTypeRepository;

    private final CounterPartyDealTypeMapper counterPartyDealTypeMapper;

    private final CounterPartyDealTypeSearchRepository counterPartyDealTypeSearchRepository;

    public CounterPartyDealTypeServiceImpl(
        CounterPartyDealTypeRepository counterPartyDealTypeRepository,
        CounterPartyDealTypeMapper counterPartyDealTypeMapper,
        CounterPartyDealTypeSearchRepository counterPartyDealTypeSearchRepository
    ) {
        this.counterPartyDealTypeRepository = counterPartyDealTypeRepository;
        this.counterPartyDealTypeMapper = counterPartyDealTypeMapper;
        this.counterPartyDealTypeSearchRepository = counterPartyDealTypeSearchRepository;
    }

    @Override
    public CounterPartyDealTypeDTO save(CounterPartyDealTypeDTO counterPartyDealTypeDTO) {
        log.debug("Request to save CounterPartyDealType : {}", counterPartyDealTypeDTO);
        CounterPartyDealType counterPartyDealType = counterPartyDealTypeMapper.toEntity(counterPartyDealTypeDTO);
        counterPartyDealType = counterPartyDealTypeRepository.save(counterPartyDealType);
        CounterPartyDealTypeDTO result = counterPartyDealTypeMapper.toDto(counterPartyDealType);
        counterPartyDealTypeSearchRepository.save(counterPartyDealType);
        return result;
    }

    @Override
    public Optional<CounterPartyDealTypeDTO> partialUpdate(CounterPartyDealTypeDTO counterPartyDealTypeDTO) {
        log.debug("Request to partially update CounterPartyDealType : {}", counterPartyDealTypeDTO);

        return counterPartyDealTypeRepository
            .findById(counterPartyDealTypeDTO.getId())
            .map(existingCounterPartyDealType -> {
                counterPartyDealTypeMapper.partialUpdate(existingCounterPartyDealType, counterPartyDealTypeDTO);

                return existingCounterPartyDealType;
            })
            .map(counterPartyDealTypeRepository::save)
            .map(savedCounterPartyDealType -> {
                counterPartyDealTypeSearchRepository.save(savedCounterPartyDealType);

                return savedCounterPartyDealType;
            })
            .map(counterPartyDealTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterPartyDealTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CounterPartyDealTypes");
        return counterPartyDealTypeRepository.findAll(pageable).map(counterPartyDealTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CounterPartyDealTypeDTO> findOne(Long id) {
        log.debug("Request to get CounterPartyDealType : {}", id);
        return counterPartyDealTypeRepository.findById(id).map(counterPartyDealTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CounterPartyDealType : {}", id);
        counterPartyDealTypeRepository.deleteById(id);
        counterPartyDealTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterPartyDealTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CounterPartyDealTypes for query {}", query);
        return counterPartyDealTypeSearchRepository.search(query, pageable).map(counterPartyDealTypeMapper::toDto);
    }
}
