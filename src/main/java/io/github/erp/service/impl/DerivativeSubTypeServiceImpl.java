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

import io.github.erp.domain.DerivativeSubType;
import io.github.erp.repository.DerivativeSubTypeRepository;
import io.github.erp.repository.search.DerivativeSubTypeSearchRepository;
import io.github.erp.service.DerivativeSubTypeService;
import io.github.erp.service.dto.DerivativeSubTypeDTO;
import io.github.erp.service.mapper.DerivativeSubTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DerivativeSubType}.
 */
@Service
@Transactional
public class DerivativeSubTypeServiceImpl implements DerivativeSubTypeService {

    private final Logger log = LoggerFactory.getLogger(DerivativeSubTypeServiceImpl.class);

    private final DerivativeSubTypeRepository derivativeSubTypeRepository;

    private final DerivativeSubTypeMapper derivativeSubTypeMapper;

    private final DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository;

    public DerivativeSubTypeServiceImpl(
        DerivativeSubTypeRepository derivativeSubTypeRepository,
        DerivativeSubTypeMapper derivativeSubTypeMapper,
        DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository
    ) {
        this.derivativeSubTypeRepository = derivativeSubTypeRepository;
        this.derivativeSubTypeMapper = derivativeSubTypeMapper;
        this.derivativeSubTypeSearchRepository = derivativeSubTypeSearchRepository;
    }

    @Override
    public DerivativeSubTypeDTO save(DerivativeSubTypeDTO derivativeSubTypeDTO) {
        log.debug("Request to save DerivativeSubType : {}", derivativeSubTypeDTO);
        DerivativeSubType derivativeSubType = derivativeSubTypeMapper.toEntity(derivativeSubTypeDTO);
        derivativeSubType = derivativeSubTypeRepository.save(derivativeSubType);
        DerivativeSubTypeDTO result = derivativeSubTypeMapper.toDto(derivativeSubType);
        derivativeSubTypeSearchRepository.save(derivativeSubType);
        return result;
    }

    @Override
    public Optional<DerivativeSubTypeDTO> partialUpdate(DerivativeSubTypeDTO derivativeSubTypeDTO) {
        log.debug("Request to partially update DerivativeSubType : {}", derivativeSubTypeDTO);

        return derivativeSubTypeRepository
            .findById(derivativeSubTypeDTO.getId())
            .map(existingDerivativeSubType -> {
                derivativeSubTypeMapper.partialUpdate(existingDerivativeSubType, derivativeSubTypeDTO);

                return existingDerivativeSubType;
            })
            .map(derivativeSubTypeRepository::save)
            .map(savedDerivativeSubType -> {
                derivativeSubTypeSearchRepository.save(savedDerivativeSubType);

                return savedDerivativeSubType;
            })
            .map(derivativeSubTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DerivativeSubTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DerivativeSubTypes");
        return derivativeSubTypeRepository.findAll(pageable).map(derivativeSubTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DerivativeSubTypeDTO> findOne(Long id) {
        log.debug("Request to get DerivativeSubType : {}", id);
        return derivativeSubTypeRepository.findById(id).map(derivativeSubTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DerivativeSubType : {}", id);
        derivativeSubTypeRepository.deleteById(id);
        derivativeSubTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DerivativeSubTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DerivativeSubTypes for query {}", query);
        return derivativeSubTypeSearchRepository.search(query, pageable).map(derivativeSubTypeMapper::toDto);
    }
}
