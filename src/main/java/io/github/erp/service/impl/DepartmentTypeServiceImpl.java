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

import io.github.erp.domain.DepartmentType;
import io.github.erp.repository.DepartmentTypeRepository;
import io.github.erp.repository.search.DepartmentTypeSearchRepository;
import io.github.erp.service.DepartmentTypeService;
import io.github.erp.service.dto.DepartmentTypeDTO;
import io.github.erp.service.mapper.DepartmentTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepartmentType}.
 */
@Service
@Transactional
public class DepartmentTypeServiceImpl implements DepartmentTypeService {

    private final Logger log = LoggerFactory.getLogger(DepartmentTypeServiceImpl.class);

    private final DepartmentTypeRepository departmentTypeRepository;

    private final DepartmentTypeMapper departmentTypeMapper;

    private final DepartmentTypeSearchRepository departmentTypeSearchRepository;

    public DepartmentTypeServiceImpl(
        DepartmentTypeRepository departmentTypeRepository,
        DepartmentTypeMapper departmentTypeMapper,
        DepartmentTypeSearchRepository departmentTypeSearchRepository
    ) {
        this.departmentTypeRepository = departmentTypeRepository;
        this.departmentTypeMapper = departmentTypeMapper;
        this.departmentTypeSearchRepository = departmentTypeSearchRepository;
    }

    @Override
    public DepartmentTypeDTO save(DepartmentTypeDTO departmentTypeDTO) {
        log.debug("Request to save DepartmentType : {}", departmentTypeDTO);
        DepartmentType departmentType = departmentTypeMapper.toEntity(departmentTypeDTO);
        departmentType = departmentTypeRepository.save(departmentType);
        DepartmentTypeDTO result = departmentTypeMapper.toDto(departmentType);
        departmentTypeSearchRepository.save(departmentType);
        return result;
    }

    @Override
    public Optional<DepartmentTypeDTO> partialUpdate(DepartmentTypeDTO departmentTypeDTO) {
        log.debug("Request to partially update DepartmentType : {}", departmentTypeDTO);

        return departmentTypeRepository
            .findById(departmentTypeDTO.getId())
            .map(existingDepartmentType -> {
                departmentTypeMapper.partialUpdate(existingDepartmentType, departmentTypeDTO);

                return existingDepartmentType;
            })
            .map(departmentTypeRepository::save)
            .map(savedDepartmentType -> {
                departmentTypeSearchRepository.save(savedDepartmentType);

                return savedDepartmentType;
            })
            .map(departmentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepartmentTypes");
        return departmentTypeRepository.findAll(pageable).map(departmentTypeMapper::toDto);
    }

    public Page<DepartmentTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return departmentTypeRepository.findAllWithEagerRelationships(pageable).map(departmentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentTypeDTO> findOne(Long id) {
        log.debug("Request to get DepartmentType : {}", id);
        return departmentTypeRepository.findOneWithEagerRelationships(id).map(departmentTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepartmentType : {}", id);
        departmentTypeRepository.deleteById(id);
        departmentTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepartmentTypes for query {}", query);
        return departmentTypeSearchRepository.search(query, pageable).map(departmentTypeMapper::toDto);
    }
}
