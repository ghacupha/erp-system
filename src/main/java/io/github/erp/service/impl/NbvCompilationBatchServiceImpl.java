package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.domain.NbvCompilationBatch;
import io.github.erp.repository.NbvCompilationBatchRepository;
import io.github.erp.repository.search.NbvCompilationBatchSearchRepository;
import io.github.erp.service.NbvCompilationBatchService;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import io.github.erp.service.mapper.NbvCompilationBatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NbvCompilationBatch}.
 */
@Service
@Transactional
public class NbvCompilationBatchServiceImpl implements NbvCompilationBatchService {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationBatchServiceImpl.class);

    private final NbvCompilationBatchRepository nbvCompilationBatchRepository;

    private final NbvCompilationBatchMapper nbvCompilationBatchMapper;

    private final NbvCompilationBatchSearchRepository nbvCompilationBatchSearchRepository;

    public NbvCompilationBatchServiceImpl(
        NbvCompilationBatchRepository nbvCompilationBatchRepository,
        NbvCompilationBatchMapper nbvCompilationBatchMapper,
        NbvCompilationBatchSearchRepository nbvCompilationBatchSearchRepository
    ) {
        this.nbvCompilationBatchRepository = nbvCompilationBatchRepository;
        this.nbvCompilationBatchMapper = nbvCompilationBatchMapper;
        this.nbvCompilationBatchSearchRepository = nbvCompilationBatchSearchRepository;
    }

    @Override
    public NbvCompilationBatchDTO save(NbvCompilationBatchDTO nbvCompilationBatchDTO) {
        log.debug("Request to save NbvCompilationBatch : {}", nbvCompilationBatchDTO);
        NbvCompilationBatch nbvCompilationBatch = nbvCompilationBatchMapper.toEntity(nbvCompilationBatchDTO);
        nbvCompilationBatch = nbvCompilationBatchRepository.save(nbvCompilationBatch);
        NbvCompilationBatchDTO result = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);
        nbvCompilationBatchSearchRepository.save(nbvCompilationBatch);
        return result;
    }

    @Override
    public Optional<NbvCompilationBatchDTO> partialUpdate(NbvCompilationBatchDTO nbvCompilationBatchDTO) {
        log.debug("Request to partially update NbvCompilationBatch : {}", nbvCompilationBatchDTO);

        return nbvCompilationBatchRepository
            .findById(nbvCompilationBatchDTO.getId())
            .map(existingNbvCompilationBatch -> {
                nbvCompilationBatchMapper.partialUpdate(existingNbvCompilationBatch, nbvCompilationBatchDTO);

                return existingNbvCompilationBatch;
            })
            .map(nbvCompilationBatchRepository::save)
            .map(savedNbvCompilationBatch -> {
                nbvCompilationBatchSearchRepository.save(savedNbvCompilationBatch);

                return savedNbvCompilationBatch;
            })
            .map(nbvCompilationBatchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NbvCompilationBatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NbvCompilationBatches");
        return nbvCompilationBatchRepository.findAll(pageable).map(nbvCompilationBatchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NbvCompilationBatchDTO> findOne(Long id) {
        log.debug("Request to get NbvCompilationBatch : {}", id);
        return nbvCompilationBatchRepository.findById(id).map(nbvCompilationBatchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NbvCompilationBatch : {}", id);
        nbvCompilationBatchRepository.deleteById(id);
        nbvCompilationBatchSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NbvCompilationBatchDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NbvCompilationBatches for query {}", query);
        return nbvCompilationBatchSearchRepository.search(query, pageable).map(nbvCompilationBatchMapper::toDto);
    }
}
