package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.WorkInProgressOutstandingReportRequisition;
import io.github.erp.repository.WorkInProgressOutstandingReportRequisitionRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportRequisitionSearchRepository;
import io.github.erp.service.WorkInProgressOutstandingReportRequisitionService;
import io.github.erp.service.dto.WorkInProgressOutstandingReportRequisitionDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkInProgressOutstandingReportRequisition}.
 */
@Service
@Transactional
public class WorkInProgressOutstandingReportRequisitionServiceImpl implements WorkInProgressOutstandingReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOutstandingReportRequisitionServiceImpl.class);

    private final WorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository;

    private final WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper;

    private final WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository;

    public WorkInProgressOutstandingReportRequisitionServiceImpl(
        WorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository,
        WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper,
        WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository
    ) {
        this.workInProgressOutstandingReportRequisitionRepository = workInProgressOutstandingReportRequisitionRepository;
        this.workInProgressOutstandingReportRequisitionMapper = workInProgressOutstandingReportRequisitionMapper;
        this.workInProgressOutstandingReportRequisitionSearchRepository = workInProgressOutstandingReportRequisitionSearchRepository;
    }

    @Override
    public WorkInProgressOutstandingReportRequisitionDTO save(
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    ) {
        log.debug("Request to save WorkInProgressOutstandingReportRequisition : {}", workInProgressOutstandingReportRequisitionDTO);
        WorkInProgressOutstandingReportRequisition workInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisitionMapper.toEntity(
            workInProgressOutstandingReportRequisitionDTO
        );
        workInProgressOutstandingReportRequisition =
            workInProgressOutstandingReportRequisitionRepository.save(workInProgressOutstandingReportRequisition);
        WorkInProgressOutstandingReportRequisitionDTO result = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );
        workInProgressOutstandingReportRequisitionSearchRepository.save(workInProgressOutstandingReportRequisition);
        return result;
    }

    @Override
    public Optional<WorkInProgressOutstandingReportRequisitionDTO> partialUpdate(
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    ) {
        log.debug(
            "Request to partially update WorkInProgressOutstandingReportRequisition : {}",
            workInProgressOutstandingReportRequisitionDTO
        );

        return workInProgressOutstandingReportRequisitionRepository
            .findById(workInProgressOutstandingReportRequisitionDTO.getId())
            .map(existingWorkInProgressOutstandingReportRequisition -> {
                workInProgressOutstandingReportRequisitionMapper.partialUpdate(
                    existingWorkInProgressOutstandingReportRequisition,
                    workInProgressOutstandingReportRequisitionDTO
                );

                return existingWorkInProgressOutstandingReportRequisition;
            })
            .map(workInProgressOutstandingReportRequisitionRepository::save)
            .map(savedWorkInProgressOutstandingReportRequisition -> {
                workInProgressOutstandingReportRequisitionSearchRepository.save(savedWorkInProgressOutstandingReportRequisition);

                return savedWorkInProgressOutstandingReportRequisition;
            })
            .map(workInProgressOutstandingReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkInProgressOutstandingReportRequisitions");
        return workInProgressOutstandingReportRequisitionRepository
            .findAll(pageable)
            .map(workInProgressOutstandingReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkInProgressOutstandingReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get WorkInProgressOutstandingReportRequisition : {}", id);
        return workInProgressOutstandingReportRequisitionRepository
            .findById(id)
            .map(workInProgressOutstandingReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkInProgressOutstandingReportRequisition : {}", id);
        workInProgressOutstandingReportRequisitionRepository.deleteById(id);
        workInProgressOutstandingReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkInProgressOutstandingReportRequisitions for query {}", query);
        return workInProgressOutstandingReportRequisitionSearchRepository
            .search(query, pageable)
            .map(workInProgressOutstandingReportRequisitionMapper::toDto);
    }
}
