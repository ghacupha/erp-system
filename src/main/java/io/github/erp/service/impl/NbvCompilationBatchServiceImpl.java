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
