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

import io.github.erp.domain.AgriculturalEnterpriseActivityType;
import io.github.erp.repository.AgriculturalEnterpriseActivityTypeRepository;
import io.github.erp.repository.search.AgriculturalEnterpriseActivityTypeSearchRepository;
import io.github.erp.service.AgriculturalEnterpriseActivityTypeService;
import io.github.erp.service.dto.AgriculturalEnterpriseActivityTypeDTO;
import io.github.erp.service.mapper.AgriculturalEnterpriseActivityTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgriculturalEnterpriseActivityType}.
 */
@Service
@Transactional
public class AgriculturalEnterpriseActivityTypeServiceImpl implements AgriculturalEnterpriseActivityTypeService {

    private final Logger log = LoggerFactory.getLogger(AgriculturalEnterpriseActivityTypeServiceImpl.class);

    private final AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository;

    private final AgriculturalEnterpriseActivityTypeMapper agriculturalEnterpriseActivityTypeMapper;

    private final AgriculturalEnterpriseActivityTypeSearchRepository agriculturalEnterpriseActivityTypeSearchRepository;

    public AgriculturalEnterpriseActivityTypeServiceImpl(
        AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository,
        AgriculturalEnterpriseActivityTypeMapper agriculturalEnterpriseActivityTypeMapper,
        AgriculturalEnterpriseActivityTypeSearchRepository agriculturalEnterpriseActivityTypeSearchRepository
    ) {
        this.agriculturalEnterpriseActivityTypeRepository = agriculturalEnterpriseActivityTypeRepository;
        this.agriculturalEnterpriseActivityTypeMapper = agriculturalEnterpriseActivityTypeMapper;
        this.agriculturalEnterpriseActivityTypeSearchRepository = agriculturalEnterpriseActivityTypeSearchRepository;
    }

    @Override
    public AgriculturalEnterpriseActivityTypeDTO save(AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO) {
        log.debug("Request to save AgriculturalEnterpriseActivityType : {}", agriculturalEnterpriseActivityTypeDTO);
        AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeMapper.toEntity(
            agriculturalEnterpriseActivityTypeDTO
        );
        agriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeRepository.save(agriculturalEnterpriseActivityType);
        AgriculturalEnterpriseActivityTypeDTO result = agriculturalEnterpriseActivityTypeMapper.toDto(agriculturalEnterpriseActivityType);
        agriculturalEnterpriseActivityTypeSearchRepository.save(agriculturalEnterpriseActivityType);
        return result;
    }

    @Override
    public Optional<AgriculturalEnterpriseActivityTypeDTO> partialUpdate(
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO
    ) {
        log.debug("Request to partially update AgriculturalEnterpriseActivityType : {}", agriculturalEnterpriseActivityTypeDTO);

        return agriculturalEnterpriseActivityTypeRepository
            .findById(agriculturalEnterpriseActivityTypeDTO.getId())
            .map(existingAgriculturalEnterpriseActivityType -> {
                agriculturalEnterpriseActivityTypeMapper.partialUpdate(
                    existingAgriculturalEnterpriseActivityType,
                    agriculturalEnterpriseActivityTypeDTO
                );

                return existingAgriculturalEnterpriseActivityType;
            })
            .map(agriculturalEnterpriseActivityTypeRepository::save)
            .map(savedAgriculturalEnterpriseActivityType -> {
                agriculturalEnterpriseActivityTypeSearchRepository.save(savedAgriculturalEnterpriseActivityType);

                return savedAgriculturalEnterpriseActivityType;
            })
            .map(agriculturalEnterpriseActivityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgriculturalEnterpriseActivityTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgriculturalEnterpriseActivityTypes");
        return agriculturalEnterpriseActivityTypeRepository.findAll(pageable).map(agriculturalEnterpriseActivityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgriculturalEnterpriseActivityTypeDTO> findOne(Long id) {
        log.debug("Request to get AgriculturalEnterpriseActivityType : {}", id);
        return agriculturalEnterpriseActivityTypeRepository.findById(id).map(agriculturalEnterpriseActivityTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgriculturalEnterpriseActivityType : {}", id);
        agriculturalEnterpriseActivityTypeRepository.deleteById(id);
        agriculturalEnterpriseActivityTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgriculturalEnterpriseActivityTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgriculturalEnterpriseActivityTypes for query {}", query);
        return agriculturalEnterpriseActivityTypeSearchRepository
            .search(query, pageable)
            .map(agriculturalEnterpriseActivityTypeMapper::toDto);
    }
}
