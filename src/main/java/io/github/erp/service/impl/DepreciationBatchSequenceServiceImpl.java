package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.search.DepreciationBatchSequenceSearchRepository;
import io.github.erp.service.DepreciationBatchSequenceService;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.mapper.DepreciationBatchSequenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationBatchSequence}.
 */
@Service
@Transactional
public class DepreciationBatchSequenceServiceImpl implements DepreciationBatchSequenceService {

    private final Logger log = LoggerFactory.getLogger(DepreciationBatchSequenceServiceImpl.class);

    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;

    private final DepreciationBatchSequenceMapper depreciationBatchSequenceMapper;

    private final DepreciationBatchSequenceSearchRepository depreciationBatchSequenceSearchRepository;

    public DepreciationBatchSequenceServiceImpl(
        DepreciationBatchSequenceRepository depreciationBatchSequenceRepository,
        DepreciationBatchSequenceMapper depreciationBatchSequenceMapper,
        DepreciationBatchSequenceSearchRepository depreciationBatchSequenceSearchRepository
    ) {
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationBatchSequenceMapper = depreciationBatchSequenceMapper;
        this.depreciationBatchSequenceSearchRepository = depreciationBatchSequenceSearchRepository;
    }

    @Override
    public DepreciationBatchSequenceDTO save(DepreciationBatchSequenceDTO depreciationBatchSequenceDTO) {
        log.debug("Request to save DepreciationBatchSequence : {}", depreciationBatchSequenceDTO);
        DepreciationBatchSequence depreciationBatchSequence = depreciationBatchSequenceMapper.toEntity(depreciationBatchSequenceDTO);
        depreciationBatchSequence = depreciationBatchSequenceRepository.save(depreciationBatchSequence);
        DepreciationBatchSequenceDTO result = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);
        depreciationBatchSequenceSearchRepository.save(depreciationBatchSequence);
        return result;
    }

    @Override
    public Optional<DepreciationBatchSequenceDTO> partialUpdate(DepreciationBatchSequenceDTO depreciationBatchSequenceDTO) {
        log.debug("Request to partially update DepreciationBatchSequence : {}", depreciationBatchSequenceDTO);

        return depreciationBatchSequenceRepository
            .findById(depreciationBatchSequenceDTO.getId())
            .map(existingDepreciationBatchSequence -> {
                depreciationBatchSequenceMapper.partialUpdate(existingDepreciationBatchSequence, depreciationBatchSequenceDTO);

                return existingDepreciationBatchSequence;
            })
            .map(depreciationBatchSequenceRepository::save)
            .map(savedDepreciationBatchSequence -> {
                depreciationBatchSequenceSearchRepository.save(savedDepreciationBatchSequence);

                return savedDepreciationBatchSequence;
            })
            .map(depreciationBatchSequenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationBatchSequenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationBatchSequences");
        return depreciationBatchSequenceRepository.findAll(pageable).map(depreciationBatchSequenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationBatchSequenceDTO> findOne(Long id) {
        log.debug("Request to get DepreciationBatchSequence : {}", id);
        return depreciationBatchSequenceRepository.findById(id).map(depreciationBatchSequenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationBatchSequence : {}", id);
        depreciationBatchSequenceRepository.deleteById(id);
        depreciationBatchSequenceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationBatchSequenceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationBatchSequences for query {}", query);
        return depreciationBatchSequenceSearchRepository.search(query, pageable).map(depreciationBatchSequenceMapper::toDto);
    }
}
