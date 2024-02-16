package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.ExecutiveCategoryType;
import io.github.erp.repository.ExecutiveCategoryTypeRepository;
import io.github.erp.repository.search.ExecutiveCategoryTypeSearchRepository;
import io.github.erp.service.ExecutiveCategoryTypeService;
import io.github.erp.service.dto.ExecutiveCategoryTypeDTO;
import io.github.erp.service.mapper.ExecutiveCategoryTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExecutiveCategoryType}.
 */
@Service
@Transactional
public class ExecutiveCategoryTypeServiceImpl implements ExecutiveCategoryTypeService {

    private final Logger log = LoggerFactory.getLogger(ExecutiveCategoryTypeServiceImpl.class);

    private final ExecutiveCategoryTypeRepository executiveCategoryTypeRepository;

    private final ExecutiveCategoryTypeMapper executiveCategoryTypeMapper;

    private final ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository;

    public ExecutiveCategoryTypeServiceImpl(
        ExecutiveCategoryTypeRepository executiveCategoryTypeRepository,
        ExecutiveCategoryTypeMapper executiveCategoryTypeMapper,
        ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository
    ) {
        this.executiveCategoryTypeRepository = executiveCategoryTypeRepository;
        this.executiveCategoryTypeMapper = executiveCategoryTypeMapper;
        this.executiveCategoryTypeSearchRepository = executiveCategoryTypeSearchRepository;
    }

    @Override
    public ExecutiveCategoryTypeDTO save(ExecutiveCategoryTypeDTO executiveCategoryTypeDTO) {
        log.debug("Request to save ExecutiveCategoryType : {}", executiveCategoryTypeDTO);
        ExecutiveCategoryType executiveCategoryType = executiveCategoryTypeMapper.toEntity(executiveCategoryTypeDTO);
        executiveCategoryType = executiveCategoryTypeRepository.save(executiveCategoryType);
        ExecutiveCategoryTypeDTO result = executiveCategoryTypeMapper.toDto(executiveCategoryType);
        executiveCategoryTypeSearchRepository.save(executiveCategoryType);
        return result;
    }

    @Override
    public Optional<ExecutiveCategoryTypeDTO> partialUpdate(ExecutiveCategoryTypeDTO executiveCategoryTypeDTO) {
        log.debug("Request to partially update ExecutiveCategoryType : {}", executiveCategoryTypeDTO);

        return executiveCategoryTypeRepository
            .findById(executiveCategoryTypeDTO.getId())
            .map(existingExecutiveCategoryType -> {
                executiveCategoryTypeMapper.partialUpdate(existingExecutiveCategoryType, executiveCategoryTypeDTO);

                return existingExecutiveCategoryType;
            })
            .map(executiveCategoryTypeRepository::save)
            .map(savedExecutiveCategoryType -> {
                executiveCategoryTypeSearchRepository.save(savedExecutiveCategoryType);

                return savedExecutiveCategoryType;
            })
            .map(executiveCategoryTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExecutiveCategoryTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExecutiveCategoryTypes");
        return executiveCategoryTypeRepository.findAll(pageable).map(executiveCategoryTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExecutiveCategoryTypeDTO> findOne(Long id) {
        log.debug("Request to get ExecutiveCategoryType : {}", id);
        return executiveCategoryTypeRepository.findById(id).map(executiveCategoryTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExecutiveCategoryType : {}", id);
        executiveCategoryTypeRepository.deleteById(id);
        executiveCategoryTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExecutiveCategoryTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExecutiveCategoryTypes for query {}", query);
        return executiveCategoryTypeSearchRepository.search(query, pageable).map(executiveCategoryTypeMapper::toDto);
    }
}
