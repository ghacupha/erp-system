package io.github.erp.service.impl;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
