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

import io.github.erp.domain.DepreciationJob;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.repository.search.DepreciationJobSearchRepository;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.mapper.DepreciationJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationJob}.
 */
@Service
@Transactional
public class DepreciationJobServiceImpl implements DepreciationJobService {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobServiceImpl.class);

    private final DepreciationJobRepository depreciationJobRepository;

    private final DepreciationJobMapper depreciationJobMapper;

    private final DepreciationJobSearchRepository depreciationJobSearchRepository;

    public DepreciationJobServiceImpl(
        DepreciationJobRepository depreciationJobRepository,
        DepreciationJobMapper depreciationJobMapper,
        DepreciationJobSearchRepository depreciationJobSearchRepository
    ) {
        this.depreciationJobRepository = depreciationJobRepository;
        this.depreciationJobMapper = depreciationJobMapper;
        this.depreciationJobSearchRepository = depreciationJobSearchRepository;
    }

    @Override
    public DepreciationJobDTO save(DepreciationJobDTO depreciationJobDTO) {
        log.debug("Request to save DepreciationJob : {}", depreciationJobDTO);
        DepreciationJob depreciationJob = depreciationJobMapper.toEntity(depreciationJobDTO);
        depreciationJob = depreciationJobRepository.save(depreciationJob);
        DepreciationJobDTO result = depreciationJobMapper.toDto(depreciationJob);
        depreciationJobSearchRepository.save(depreciationJob);
        return result;
    }

    @Override
    public Optional<DepreciationJobDTO> partialUpdate(DepreciationJobDTO depreciationJobDTO) {
        log.debug("Request to partially update DepreciationJob : {}", depreciationJobDTO);

        return depreciationJobRepository
            .findById(depreciationJobDTO.getId())
            .map(existingDepreciationJob -> {
                depreciationJobMapper.partialUpdate(existingDepreciationJob, depreciationJobDTO);

                return existingDepreciationJob;
            })
            .map(depreciationJobRepository::save)
            .map(savedDepreciationJob -> {
                depreciationJobSearchRepository.save(savedDepreciationJob);

                return savedDepreciationJob;
            })
            .map(depreciationJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationJobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationJobs");
        return depreciationJobRepository.findAll(pageable).map(depreciationJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationJobDTO> findOne(Long id) {
        log.debug("Request to get DepreciationJob : {}", id);
        return depreciationJobRepository.findById(id).map(depreciationJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationJob : {}", id);
        depreciationJobRepository.deleteById(id);
        depreciationJobSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationJobDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationJobs for query {}", query);
        return depreciationJobSearchRepository.search(query, pageable).map(depreciationJobMapper::toDto);
    }
}
