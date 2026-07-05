package io.github.erp.internal.service.assets;

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
import io.github.erp.domain.WorkInProgressOutstandingReportRequisition;
import io.github.erp.internal.repository.InternalWorkInProgressOutstandingReportRequisitionRepository;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.search.WorkInProgressOutstandingReportRequisitionSearchRepository;
import io.github.erp.service.dto.WorkInProgressOutstandingReportRequisitionDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportRequisitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link WorkInProgressOutstandingReportRequisition}.
 */
@Service
@Transactional
public class InternalWorkInProgressOutstandingReportRequisitionServiceImpl implements InternalWorkInProgressOutstandingReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(InternalWorkInProgressOutstandingReportRequisitionServiceImpl.class);

    private final InternalWorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository;

    private final WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper;

    private final WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository;

    private final InternalApplicationUserDetailService applicationUserDetailService;

    public InternalWorkInProgressOutstandingReportRequisitionServiceImpl(
        InternalWorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository,
        WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper,
        WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository,
        InternalApplicationUserDetailService applicationUserDetailService) {
        this.workInProgressOutstandingReportRequisitionRepository = workInProgressOutstandingReportRequisitionRepository;
        this.workInProgressOutstandingReportRequisitionMapper = workInProgressOutstandingReportRequisitionMapper;
        this.workInProgressOutstandingReportRequisitionSearchRepository = workInProgressOutstandingReportRequisitionSearchRepository;
        this.applicationUserDetailService = applicationUserDetailService;
    }

    @Override
    public WorkInProgressOutstandingReportRequisitionDTO save(
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    ) {
        log.debug("Request to save WorkInProgressOutstandingReportRequisition : {}", workInProgressOutstandingReportRequisitionDTO);

        applicationUserDetailService.getCurrentApplicationUser().ifPresent(appUser -> {
            if (workInProgressOutstandingReportRequisitionDTO.getId() == null) {
                workInProgressOutstandingReportRequisitionDTO.setRequestedBy(appUser);
            } else {
                workInProgressOutstandingReportRequisitionDTO.setLastAccessedBy(appUser);
            }
        });

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

        Optional<WorkInProgressOutstandingReportRequisitionDTO> one =
            workInProgressOutstandingReportRequisitionRepository
                .findById(id)
                .map(workInProgressOutstandingReportRequisitionMapper::toDto);

        // to update last-accessed-by
        save(one.get());

        return one;
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
