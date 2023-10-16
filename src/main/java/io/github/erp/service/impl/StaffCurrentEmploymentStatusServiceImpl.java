package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.StaffCurrentEmploymentStatus;
import io.github.erp.repository.StaffCurrentEmploymentStatusRepository;
import io.github.erp.repository.search.StaffCurrentEmploymentStatusSearchRepository;
import io.github.erp.service.StaffCurrentEmploymentStatusService;
import io.github.erp.service.dto.StaffCurrentEmploymentStatusDTO;
import io.github.erp.service.mapper.StaffCurrentEmploymentStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StaffCurrentEmploymentStatus}.
 */
@Service
@Transactional
public class StaffCurrentEmploymentStatusServiceImpl implements StaffCurrentEmploymentStatusService {

    private final Logger log = LoggerFactory.getLogger(StaffCurrentEmploymentStatusServiceImpl.class);

    private final StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository;

    private final StaffCurrentEmploymentStatusMapper staffCurrentEmploymentStatusMapper;

    private final StaffCurrentEmploymentStatusSearchRepository staffCurrentEmploymentStatusSearchRepository;

    public StaffCurrentEmploymentStatusServiceImpl(
        StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository,
        StaffCurrentEmploymentStatusMapper staffCurrentEmploymentStatusMapper,
        StaffCurrentEmploymentStatusSearchRepository staffCurrentEmploymentStatusSearchRepository
    ) {
        this.staffCurrentEmploymentStatusRepository = staffCurrentEmploymentStatusRepository;
        this.staffCurrentEmploymentStatusMapper = staffCurrentEmploymentStatusMapper;
        this.staffCurrentEmploymentStatusSearchRepository = staffCurrentEmploymentStatusSearchRepository;
    }

    @Override
    public StaffCurrentEmploymentStatusDTO save(StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO) {
        log.debug("Request to save StaffCurrentEmploymentStatus : {}", staffCurrentEmploymentStatusDTO);
        StaffCurrentEmploymentStatus staffCurrentEmploymentStatus = staffCurrentEmploymentStatusMapper.toEntity(
            staffCurrentEmploymentStatusDTO
        );
        staffCurrentEmploymentStatus = staffCurrentEmploymentStatusRepository.save(staffCurrentEmploymentStatus);
        StaffCurrentEmploymentStatusDTO result = staffCurrentEmploymentStatusMapper.toDto(staffCurrentEmploymentStatus);
        staffCurrentEmploymentStatusSearchRepository.save(staffCurrentEmploymentStatus);
        return result;
    }

    @Override
    public Optional<StaffCurrentEmploymentStatusDTO> partialUpdate(StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO) {
        log.debug("Request to partially update StaffCurrentEmploymentStatus : {}", staffCurrentEmploymentStatusDTO);

        return staffCurrentEmploymentStatusRepository
            .findById(staffCurrentEmploymentStatusDTO.getId())
            .map(existingStaffCurrentEmploymentStatus -> {
                staffCurrentEmploymentStatusMapper.partialUpdate(existingStaffCurrentEmploymentStatus, staffCurrentEmploymentStatusDTO);

                return existingStaffCurrentEmploymentStatus;
            })
            .map(staffCurrentEmploymentStatusRepository::save)
            .map(savedStaffCurrentEmploymentStatus -> {
                staffCurrentEmploymentStatusSearchRepository.save(savedStaffCurrentEmploymentStatus);

                return savedStaffCurrentEmploymentStatus;
            })
            .map(staffCurrentEmploymentStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffCurrentEmploymentStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StaffCurrentEmploymentStatuses");
        return staffCurrentEmploymentStatusRepository.findAll(pageable).map(staffCurrentEmploymentStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StaffCurrentEmploymentStatusDTO> findOne(Long id) {
        log.debug("Request to get StaffCurrentEmploymentStatus : {}", id);
        return staffCurrentEmploymentStatusRepository.findById(id).map(staffCurrentEmploymentStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaffCurrentEmploymentStatus : {}", id);
        staffCurrentEmploymentStatusRepository.deleteById(id);
        staffCurrentEmploymentStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffCurrentEmploymentStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StaffCurrentEmploymentStatuses for query {}", query);
        return staffCurrentEmploymentStatusSearchRepository.search(query, pageable).map(staffCurrentEmploymentStatusMapper::toDto);
    }
}
