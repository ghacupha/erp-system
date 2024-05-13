package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.StaffRoleType;
import io.github.erp.repository.StaffRoleTypeRepository;
import io.github.erp.repository.search.StaffRoleTypeSearchRepository;
import io.github.erp.service.StaffRoleTypeService;
import io.github.erp.service.dto.StaffRoleTypeDTO;
import io.github.erp.service.mapper.StaffRoleTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StaffRoleType}.
 */
@Service
@Transactional
public class StaffRoleTypeServiceImpl implements StaffRoleTypeService {

    private final Logger log = LoggerFactory.getLogger(StaffRoleTypeServiceImpl.class);

    private final StaffRoleTypeRepository staffRoleTypeRepository;

    private final StaffRoleTypeMapper staffRoleTypeMapper;

    private final StaffRoleTypeSearchRepository staffRoleTypeSearchRepository;

    public StaffRoleTypeServiceImpl(
        StaffRoleTypeRepository staffRoleTypeRepository,
        StaffRoleTypeMapper staffRoleTypeMapper,
        StaffRoleTypeSearchRepository staffRoleTypeSearchRepository
    ) {
        this.staffRoleTypeRepository = staffRoleTypeRepository;
        this.staffRoleTypeMapper = staffRoleTypeMapper;
        this.staffRoleTypeSearchRepository = staffRoleTypeSearchRepository;
    }

    @Override
    public StaffRoleTypeDTO save(StaffRoleTypeDTO staffRoleTypeDTO) {
        log.debug("Request to save StaffRoleType : {}", staffRoleTypeDTO);
        StaffRoleType staffRoleType = staffRoleTypeMapper.toEntity(staffRoleTypeDTO);
        staffRoleType = staffRoleTypeRepository.save(staffRoleType);
        StaffRoleTypeDTO result = staffRoleTypeMapper.toDto(staffRoleType);
        staffRoleTypeSearchRepository.save(staffRoleType);
        return result;
    }

    @Override
    public Optional<StaffRoleTypeDTO> partialUpdate(StaffRoleTypeDTO staffRoleTypeDTO) {
        log.debug("Request to partially update StaffRoleType : {}", staffRoleTypeDTO);

        return staffRoleTypeRepository
            .findById(staffRoleTypeDTO.getId())
            .map(existingStaffRoleType -> {
                staffRoleTypeMapper.partialUpdate(existingStaffRoleType, staffRoleTypeDTO);

                return existingStaffRoleType;
            })
            .map(staffRoleTypeRepository::save)
            .map(savedStaffRoleType -> {
                staffRoleTypeSearchRepository.save(savedStaffRoleType);

                return savedStaffRoleType;
            })
            .map(staffRoleTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffRoleTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StaffRoleTypes");
        return staffRoleTypeRepository.findAll(pageable).map(staffRoleTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StaffRoleTypeDTO> findOne(Long id) {
        log.debug("Request to get StaffRoleType : {}", id);
        return staffRoleTypeRepository.findById(id).map(staffRoleTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaffRoleType : {}", id);
        staffRoleTypeRepository.deleteById(id);
        staffRoleTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffRoleTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StaffRoleTypes for query {}", query);
        return staffRoleTypeSearchRepository.search(query, pageable).map(staffRoleTypeMapper::toDto);
    }
}
