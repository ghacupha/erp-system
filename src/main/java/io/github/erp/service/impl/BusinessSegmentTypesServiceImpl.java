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

import io.github.erp.domain.BusinessSegmentTypes;
import io.github.erp.repository.BusinessSegmentTypesRepository;
import io.github.erp.repository.search.BusinessSegmentTypesSearchRepository;
import io.github.erp.service.BusinessSegmentTypesService;
import io.github.erp.service.dto.BusinessSegmentTypesDTO;
import io.github.erp.service.mapper.BusinessSegmentTypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusinessSegmentTypes}.
 */
@Service
@Transactional
public class BusinessSegmentTypesServiceImpl implements BusinessSegmentTypesService {

    private final Logger log = LoggerFactory.getLogger(BusinessSegmentTypesServiceImpl.class);

    private final BusinessSegmentTypesRepository businessSegmentTypesRepository;

    private final BusinessSegmentTypesMapper businessSegmentTypesMapper;

    private final BusinessSegmentTypesSearchRepository businessSegmentTypesSearchRepository;

    public BusinessSegmentTypesServiceImpl(
        BusinessSegmentTypesRepository businessSegmentTypesRepository,
        BusinessSegmentTypesMapper businessSegmentTypesMapper,
        BusinessSegmentTypesSearchRepository businessSegmentTypesSearchRepository
    ) {
        this.businessSegmentTypesRepository = businessSegmentTypesRepository;
        this.businessSegmentTypesMapper = businessSegmentTypesMapper;
        this.businessSegmentTypesSearchRepository = businessSegmentTypesSearchRepository;
    }

    @Override
    public BusinessSegmentTypesDTO save(BusinessSegmentTypesDTO businessSegmentTypesDTO) {
        log.debug("Request to save BusinessSegmentTypes : {}", businessSegmentTypesDTO);
        BusinessSegmentTypes businessSegmentTypes = businessSegmentTypesMapper.toEntity(businessSegmentTypesDTO);
        businessSegmentTypes = businessSegmentTypesRepository.save(businessSegmentTypes);
        BusinessSegmentTypesDTO result = businessSegmentTypesMapper.toDto(businessSegmentTypes);
        businessSegmentTypesSearchRepository.save(businessSegmentTypes);
        return result;
    }

    @Override
    public Optional<BusinessSegmentTypesDTO> partialUpdate(BusinessSegmentTypesDTO businessSegmentTypesDTO) {
        log.debug("Request to partially update BusinessSegmentTypes : {}", businessSegmentTypesDTO);

        return businessSegmentTypesRepository
            .findById(businessSegmentTypesDTO.getId())
            .map(existingBusinessSegmentTypes -> {
                businessSegmentTypesMapper.partialUpdate(existingBusinessSegmentTypes, businessSegmentTypesDTO);

                return existingBusinessSegmentTypes;
            })
            .map(businessSegmentTypesRepository::save)
            .map(savedBusinessSegmentTypes -> {
                businessSegmentTypesSearchRepository.save(savedBusinessSegmentTypes);

                return savedBusinessSegmentTypes;
            })
            .map(businessSegmentTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessSegmentTypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessSegmentTypes");
        return businessSegmentTypesRepository.findAll(pageable).map(businessSegmentTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessSegmentTypesDTO> findOne(Long id) {
        log.debug("Request to get BusinessSegmentTypes : {}", id);
        return businessSegmentTypesRepository.findById(id).map(businessSegmentTypesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessSegmentTypes : {}", id);
        businessSegmentTypesRepository.deleteById(id);
        businessSegmentTypesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessSegmentTypesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessSegmentTypes for query {}", query);
        return businessSegmentTypesSearchRepository.search(query, pageable).map(businessSegmentTypesMapper::toDto);
    }
}
