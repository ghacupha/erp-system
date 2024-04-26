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
