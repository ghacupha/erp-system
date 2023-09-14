package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.repository.WorkInProgressRegistrationRepository;
import io.github.erp.repository.search.WorkInProgressRegistrationSearchRepository;
import io.github.erp.service.WorkInProgressRegistrationService;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import io.github.erp.service.mapper.WorkInProgressRegistrationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkInProgressRegistration}.
 */
@Service
@Transactional
public class WorkInProgressRegistrationServiceImpl implements WorkInProgressRegistrationService {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressRegistrationServiceImpl.class);

    private final WorkInProgressRegistrationRepository workInProgressRegistrationRepository;

    private final WorkInProgressRegistrationMapper workInProgressRegistrationMapper;

    private final WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository;

    public WorkInProgressRegistrationServiceImpl(
        WorkInProgressRegistrationRepository workInProgressRegistrationRepository,
        WorkInProgressRegistrationMapper workInProgressRegistrationMapper,
        WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository
    ) {
        this.workInProgressRegistrationRepository = workInProgressRegistrationRepository;
        this.workInProgressRegistrationMapper = workInProgressRegistrationMapper;
        this.workInProgressRegistrationSearchRepository = workInProgressRegistrationSearchRepository;
    }

    @Override
    public WorkInProgressRegistrationDTO save(WorkInProgressRegistrationDTO workInProgressRegistrationDTO) {
        log.debug("Request to save WorkInProgressRegistration : {}", workInProgressRegistrationDTO);
        WorkInProgressRegistration workInProgressRegistration = workInProgressRegistrationMapper.toEntity(workInProgressRegistrationDTO);
        workInProgressRegistration = workInProgressRegistrationRepository.save(workInProgressRegistration);
        WorkInProgressRegistrationDTO result = workInProgressRegistrationMapper.toDto(workInProgressRegistration);
        workInProgressRegistrationSearchRepository.save(workInProgressRegistration);
        return result;
    }

    @Override
    public Optional<WorkInProgressRegistrationDTO> partialUpdate(WorkInProgressRegistrationDTO workInProgressRegistrationDTO) {
        log.debug("Request to partially update WorkInProgressRegistration : {}", workInProgressRegistrationDTO);

        return workInProgressRegistrationRepository
            .findById(workInProgressRegistrationDTO.getId())
            .map(existingWorkInProgressRegistration -> {
                workInProgressRegistrationMapper.partialUpdate(existingWorkInProgressRegistration, workInProgressRegistrationDTO);

                return existingWorkInProgressRegistration;
            })
            .map(workInProgressRegistrationRepository::save)
            .map(savedWorkInProgressRegistration -> {
                workInProgressRegistrationSearchRepository.save(savedWorkInProgressRegistration);

                return savedWorkInProgressRegistration;
            })
            .map(workInProgressRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkInProgressRegistrations");
        return workInProgressRegistrationRepository.findAll(pageable).map(workInProgressRegistrationMapper::toDto);
    }

    public Page<WorkInProgressRegistrationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workInProgressRegistrationRepository.findAllWithEagerRelationships(pageable).map(workInProgressRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkInProgressRegistrationDTO> findOne(Long id) {
        log.debug("Request to get WorkInProgressRegistration : {}", id);
        return workInProgressRegistrationRepository.findOneWithEagerRelationships(id).map(workInProgressRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkInProgressRegistration : {}", id);
        workInProgressRegistrationRepository.deleteById(id);
        workInProgressRegistrationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkInProgressRegistrations for query {}", query);
        return workInProgressRegistrationSearchRepository.search(query, pageable).map(workInProgressRegistrationMapper::toDto);
    }
}
