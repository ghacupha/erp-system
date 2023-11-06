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
