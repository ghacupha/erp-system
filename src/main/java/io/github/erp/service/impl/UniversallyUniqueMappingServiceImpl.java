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

import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.service.UniversallyUniqueMappingService;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import io.github.erp.service.mapper.UniversallyUniqueMappingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UniversallyUniqueMapping}.
 */
@Service
@Transactional
public class UniversallyUniqueMappingServiceImpl implements UniversallyUniqueMappingService {

    private final Logger log = LoggerFactory.getLogger(UniversallyUniqueMappingServiceImpl.class);

    private final UniversallyUniqueMappingRepository universallyUniqueMappingRepository;

    private final UniversallyUniqueMappingMapper universallyUniqueMappingMapper;

    private final UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository;

    public UniversallyUniqueMappingServiceImpl(
        UniversallyUniqueMappingRepository universallyUniqueMappingRepository,
        UniversallyUniqueMappingMapper universallyUniqueMappingMapper,
        UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository
    ) {
        this.universallyUniqueMappingRepository = universallyUniqueMappingRepository;
        this.universallyUniqueMappingMapper = universallyUniqueMappingMapper;
        this.universallyUniqueMappingSearchRepository = universallyUniqueMappingSearchRepository;
    }

    @Override
    public UniversallyUniqueMappingDTO save(UniversallyUniqueMappingDTO universallyUniqueMappingDTO) {
        log.debug("Request to save UniversallyUniqueMapping : {}", universallyUniqueMappingDTO);
        UniversallyUniqueMapping universallyUniqueMapping = universallyUniqueMappingMapper.toEntity(universallyUniqueMappingDTO);
        universallyUniqueMapping = universallyUniqueMappingRepository.save(universallyUniqueMapping);
        UniversallyUniqueMappingDTO result = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);
        universallyUniqueMappingSearchRepository.save(universallyUniqueMapping);
        return result;
    }

    @Override
    public Optional<UniversallyUniqueMappingDTO> partialUpdate(UniversallyUniqueMappingDTO universallyUniqueMappingDTO) {
        log.debug("Request to partially update UniversallyUniqueMapping : {}", universallyUniqueMappingDTO);

        return universallyUniqueMappingRepository
            .findById(universallyUniqueMappingDTO.getId())
            .map(existingUniversallyUniqueMapping -> {
                universallyUniqueMappingMapper.partialUpdate(existingUniversallyUniqueMapping, universallyUniqueMappingDTO);

                return existingUniversallyUniqueMapping;
            })
            .map(universallyUniqueMappingRepository::save)
            .map(savedUniversallyUniqueMapping -> {
                universallyUniqueMappingSearchRepository.save(savedUniversallyUniqueMapping);

                return savedUniversallyUniqueMapping;
            })
            .map(universallyUniqueMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversallyUniqueMappingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniversallyUniqueMappings");
        return universallyUniqueMappingRepository.findAll(pageable).map(universallyUniqueMappingMapper::toDto);
    }

    public Page<UniversallyUniqueMappingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return universallyUniqueMappingRepository.findAllWithEagerRelationships(pageable).map(universallyUniqueMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversallyUniqueMappingDTO> findOne(Long id) {
        log.debug("Request to get UniversallyUniqueMapping : {}", id);
        return universallyUniqueMappingRepository.findOneWithEagerRelationships(id).map(universallyUniqueMappingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UniversallyUniqueMapping : {}", id);
        universallyUniqueMappingRepository.deleteById(id);
        universallyUniqueMappingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversallyUniqueMappingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UniversallyUniqueMappings for query {}", query);
        return universallyUniqueMappingSearchRepository.search(query, pageable).map(universallyUniqueMappingMapper::toDto);
    }
}
