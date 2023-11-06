package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
