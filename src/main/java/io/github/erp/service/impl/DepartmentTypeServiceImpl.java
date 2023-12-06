package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
