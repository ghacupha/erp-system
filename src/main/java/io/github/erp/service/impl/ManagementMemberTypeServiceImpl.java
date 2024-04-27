package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.domain.ManagementMemberType;
import io.github.erp.repository.ManagementMemberTypeRepository;
import io.github.erp.repository.search.ManagementMemberTypeSearchRepository;
import io.github.erp.service.ManagementMemberTypeService;
import io.github.erp.service.dto.ManagementMemberTypeDTO;
import io.github.erp.service.mapper.ManagementMemberTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ManagementMemberType}.
 */
@Service
@Transactional
public class ManagementMemberTypeServiceImpl implements ManagementMemberTypeService {

    private final Logger log = LoggerFactory.getLogger(ManagementMemberTypeServiceImpl.class);

    private final ManagementMemberTypeRepository managementMemberTypeRepository;

    private final ManagementMemberTypeMapper managementMemberTypeMapper;

    private final ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository;

    public ManagementMemberTypeServiceImpl(
        ManagementMemberTypeRepository managementMemberTypeRepository,
        ManagementMemberTypeMapper managementMemberTypeMapper,
        ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository
    ) {
        this.managementMemberTypeRepository = managementMemberTypeRepository;
        this.managementMemberTypeMapper = managementMemberTypeMapper;
        this.managementMemberTypeSearchRepository = managementMemberTypeSearchRepository;
    }

    @Override
    public ManagementMemberTypeDTO save(ManagementMemberTypeDTO managementMemberTypeDTO) {
        log.debug("Request to save ManagementMemberType : {}", managementMemberTypeDTO);
        ManagementMemberType managementMemberType = managementMemberTypeMapper.toEntity(managementMemberTypeDTO);
        managementMemberType = managementMemberTypeRepository.save(managementMemberType);
        ManagementMemberTypeDTO result = managementMemberTypeMapper.toDto(managementMemberType);
        managementMemberTypeSearchRepository.save(managementMemberType);
        return result;
    }

    @Override
    public Optional<ManagementMemberTypeDTO> partialUpdate(ManagementMemberTypeDTO managementMemberTypeDTO) {
        log.debug("Request to partially update ManagementMemberType : {}", managementMemberTypeDTO);

        return managementMemberTypeRepository
            .findById(managementMemberTypeDTO.getId())
            .map(existingManagementMemberType -> {
                managementMemberTypeMapper.partialUpdate(existingManagementMemberType, managementMemberTypeDTO);

                return existingManagementMemberType;
            })
            .map(managementMemberTypeRepository::save)
            .map(savedManagementMemberType -> {
                managementMemberTypeSearchRepository.save(savedManagementMemberType);

                return savedManagementMemberType;
            })
            .map(managementMemberTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagementMemberTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManagementMemberTypes");
        return managementMemberTypeRepository.findAll(pageable).map(managementMemberTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManagementMemberTypeDTO> findOne(Long id) {
        log.debug("Request to get ManagementMemberType : {}", id);
        return managementMemberTypeRepository.findById(id).map(managementMemberTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ManagementMemberType : {}", id);
        managementMemberTypeRepository.deleteById(id);
        managementMemberTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagementMemberTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ManagementMemberTypes for query {}", query);
        return managementMemberTypeSearchRepository.search(query, pageable).map(managementMemberTypeMapper::toDto);
    }
}
