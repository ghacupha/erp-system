package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.repository.NbvCompilationJobRepository;
import io.github.erp.repository.search.NbvCompilationJobSearchRepository;
import io.github.erp.service.NbvCompilationJobService;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import io.github.erp.service.mapper.NbvCompilationJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NbvCompilationJob}.
 */
@Service
@Transactional
public class NbvCompilationJobServiceImpl implements NbvCompilationJobService {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationJobServiceImpl.class);

    private final NbvCompilationJobRepository nbvCompilationJobRepository;

    private final NbvCompilationJobMapper nbvCompilationJobMapper;

    private final NbvCompilationJobSearchRepository nbvCompilationJobSearchRepository;

    public NbvCompilationJobServiceImpl(
        NbvCompilationJobRepository nbvCompilationJobRepository,
        NbvCompilationJobMapper nbvCompilationJobMapper,
        NbvCompilationJobSearchRepository nbvCompilationJobSearchRepository
    ) {
        this.nbvCompilationJobRepository = nbvCompilationJobRepository;
        this.nbvCompilationJobMapper = nbvCompilationJobMapper;
        this.nbvCompilationJobSearchRepository = nbvCompilationJobSearchRepository;
    }

    @Override
    public NbvCompilationJobDTO save(NbvCompilationJobDTO nbvCompilationJobDTO) {
        log.debug("Request to save NbvCompilationJob : {}", nbvCompilationJobDTO);
        NbvCompilationJob nbvCompilationJob = nbvCompilationJobMapper.toEntity(nbvCompilationJobDTO);
        nbvCompilationJob = nbvCompilationJobRepository.save(nbvCompilationJob);
        NbvCompilationJobDTO result = nbvCompilationJobMapper.toDto(nbvCompilationJob);
        nbvCompilationJobSearchRepository.save(nbvCompilationJob);
        return result;
    }

    @Override
    public Optional<NbvCompilationJobDTO> partialUpdate(NbvCompilationJobDTO nbvCompilationJobDTO) {
        log.debug("Request to partially update NbvCompilationJob : {}", nbvCompilationJobDTO);

        return nbvCompilationJobRepository
            .findById(nbvCompilationJobDTO.getId())
            .map(existingNbvCompilationJob -> {
                nbvCompilationJobMapper.partialUpdate(existingNbvCompilationJob, nbvCompilationJobDTO);

                return existingNbvCompilationJob;
            })
            .map(nbvCompilationJobRepository::save)
            .map(savedNbvCompilationJob -> {
                nbvCompilationJobSearchRepository.save(savedNbvCompilationJob);

                return savedNbvCompilationJob;
            })
            .map(nbvCompilationJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NbvCompilationJobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NbvCompilationJobs");
        return nbvCompilationJobRepository.findAll(pageable).map(nbvCompilationJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NbvCompilationJobDTO> findOne(Long id) {
        log.debug("Request to get NbvCompilationJob : {}", id);
        return nbvCompilationJobRepository.findById(id).map(nbvCompilationJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NbvCompilationJob : {}", id);
        nbvCompilationJobRepository.deleteById(id);
        nbvCompilationJobSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NbvCompilationJobDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NbvCompilationJobs for query {}", query);
        return nbvCompilationJobSearchRepository.search(query, pageable).map(nbvCompilationJobMapper::toDto);
    }
}
