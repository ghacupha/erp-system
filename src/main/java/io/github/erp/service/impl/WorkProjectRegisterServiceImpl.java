package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.repository.WorkProjectRegisterRepository;
import io.github.erp.repository.search.WorkProjectRegisterSearchRepository;
import io.github.erp.service.WorkProjectRegisterService;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
import io.github.erp.service.mapper.WorkProjectRegisterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkProjectRegister}.
 */
@Service
@Transactional
public class WorkProjectRegisterServiceImpl implements WorkProjectRegisterService {

    private final Logger log = LoggerFactory.getLogger(WorkProjectRegisterServiceImpl.class);

    private final WorkProjectRegisterRepository workProjectRegisterRepository;

    private final WorkProjectRegisterMapper workProjectRegisterMapper;

    private final WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository;

    public WorkProjectRegisterServiceImpl(
        WorkProjectRegisterRepository workProjectRegisterRepository,
        WorkProjectRegisterMapper workProjectRegisterMapper,
        WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository
    ) {
        this.workProjectRegisterRepository = workProjectRegisterRepository;
        this.workProjectRegisterMapper = workProjectRegisterMapper;
        this.workProjectRegisterSearchRepository = workProjectRegisterSearchRepository;
    }

    @Override
    public WorkProjectRegisterDTO save(WorkProjectRegisterDTO workProjectRegisterDTO) {
        log.debug("Request to save WorkProjectRegister : {}", workProjectRegisterDTO);
        WorkProjectRegister workProjectRegister = workProjectRegisterMapper.toEntity(workProjectRegisterDTO);
        workProjectRegister = workProjectRegisterRepository.save(workProjectRegister);
        WorkProjectRegisterDTO result = workProjectRegisterMapper.toDto(workProjectRegister);
        workProjectRegisterSearchRepository.save(workProjectRegister);
        return result;
    }

    @Override
    public Optional<WorkProjectRegisterDTO> partialUpdate(WorkProjectRegisterDTO workProjectRegisterDTO) {
        log.debug("Request to partially update WorkProjectRegister : {}", workProjectRegisterDTO);

        return workProjectRegisterRepository
            .findById(workProjectRegisterDTO.getId())
            .map(existingWorkProjectRegister -> {
                workProjectRegisterMapper.partialUpdate(existingWorkProjectRegister, workProjectRegisterDTO);

                return existingWorkProjectRegister;
            })
            .map(workProjectRegisterRepository::save)
            .map(savedWorkProjectRegister -> {
                workProjectRegisterSearchRepository.save(savedWorkProjectRegister);

                return savedWorkProjectRegister;
            })
            .map(workProjectRegisterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkProjectRegisterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkProjectRegisters");
        return workProjectRegisterRepository.findAll(pageable).map(workProjectRegisterMapper::toDto);
    }

    public Page<WorkProjectRegisterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workProjectRegisterRepository.findAllWithEagerRelationships(pageable).map(workProjectRegisterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkProjectRegisterDTO> findOne(Long id) {
        log.debug("Request to get WorkProjectRegister : {}", id);
        return workProjectRegisterRepository.findOneWithEagerRelationships(id).map(workProjectRegisterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkProjectRegister : {}", id);
        workProjectRegisterRepository.deleteById(id);
        workProjectRegisterSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkProjectRegisterDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkProjectRegisters for query {}", query);
        return workProjectRegisterSearchRepository.search(query, pageable).map(workProjectRegisterMapper::toDto);
    }
}
