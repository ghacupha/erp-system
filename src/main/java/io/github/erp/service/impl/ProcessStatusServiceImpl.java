package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 7 (Caleb Series) Server ver 0.3.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.ProcessStatus;
import io.github.erp.repository.ProcessStatusRepository;
import io.github.erp.repository.search.ProcessStatusSearchRepository;
import io.github.erp.service.ProcessStatusService;
import io.github.erp.service.dto.ProcessStatusDTO;
import io.github.erp.service.mapper.ProcessStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProcessStatus}.
 */
@Service
@Transactional
public class ProcessStatusServiceImpl implements ProcessStatusService {

    private final Logger log = LoggerFactory.getLogger(ProcessStatusServiceImpl.class);

    private final ProcessStatusRepository processStatusRepository;

    private final ProcessStatusMapper processStatusMapper;

    private final ProcessStatusSearchRepository processStatusSearchRepository;

    public ProcessStatusServiceImpl(
        ProcessStatusRepository processStatusRepository,
        ProcessStatusMapper processStatusMapper,
        ProcessStatusSearchRepository processStatusSearchRepository
    ) {
        this.processStatusRepository = processStatusRepository;
        this.processStatusMapper = processStatusMapper;
        this.processStatusSearchRepository = processStatusSearchRepository;
    }

    @Override
    public ProcessStatusDTO save(ProcessStatusDTO processStatusDTO) {
        log.debug("Request to save ProcessStatus : {}", processStatusDTO);
        ProcessStatus processStatus = processStatusMapper.toEntity(processStatusDTO);
        processStatus = processStatusRepository.save(processStatus);
        ProcessStatusDTO result = processStatusMapper.toDto(processStatus);
        processStatusSearchRepository.save(processStatus);
        return result;
    }

    @Override
    public Optional<ProcessStatusDTO> partialUpdate(ProcessStatusDTO processStatusDTO) {
        log.debug("Request to partially update ProcessStatus : {}", processStatusDTO);

        return processStatusRepository
            .findById(processStatusDTO.getId())
            .map(existingProcessStatus -> {
                processStatusMapper.partialUpdate(existingProcessStatus, processStatusDTO);

                return existingProcessStatus;
            })
            .map(processStatusRepository::save)
            .map(savedProcessStatus -> {
                processStatusSearchRepository.save(savedProcessStatus);

                return savedProcessStatus;
            })
            .map(processStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessStatuses");
        return processStatusRepository.findAll(pageable).map(processStatusMapper::toDto);
    }

    public Page<ProcessStatusDTO> findAllWithEagerRelationships(Pageable pageable) {
        return processStatusRepository.findAllWithEagerRelationships(pageable).map(processStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessStatusDTO> findOne(Long id) {
        log.debug("Request to get ProcessStatus : {}", id);
        return processStatusRepository.findOneWithEagerRelationships(id).map(processStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcessStatus : {}", id);
        processStatusRepository.deleteById(id);
        processStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProcessStatuses for query {}", query);
        return processStatusSearchRepository.search(query, pageable).map(processStatusMapper::toDto);
    }
}
