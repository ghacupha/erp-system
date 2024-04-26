package io.github.erp.internal.service;

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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.internal.repository.InternalUniversallyUniqueMappingRepository;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import io.github.erp.service.impl.UniversallyUniqueMappingServiceImpl;
import io.github.erp.service.mapper.UniversallyUniqueMappingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service("internalUniversallyUniqueMappingService")
@Transactional
public class InternalUniversallyUniqueMappingServiceImpl implements InternalUniversallyUniqueMappingService {

    private static final Logger log = LoggerFactory.getLogger(InternalUniversallyUniqueMappingService.class);

    private final InternalUniversallyUniqueMappingRepository internalUUMappingRepository;

    private final UniversallyUniqueMappingMapper universallyUniqueMappingMapper;

    private final UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository;

    public InternalUniversallyUniqueMappingServiceImpl(
        UniversallyUniqueMappingMapper universallyUniqueMappingMapper,
        UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository,
        InternalUniversallyUniqueMappingRepository internalUUMappingRepository) {
        this.internalUUMappingRepository = internalUUMappingRepository;
        this.universallyUniqueMappingMapper = universallyUniqueMappingMapper;
        this.universallyUniqueMappingSearchRepository = universallyUniqueMappingSearchRepository;
    }

    @Override
    public Optional<UniversallyUniqueMapping> getMapping(String universalKey) {
        AtomicReference<UniversallyUniqueMapping> val = new AtomicReference<>();
        internalUUMappingRepository.findByUniversalKeyEquals(universalKey).ifPresentOrElse(
            val::set,
            () -> {throw new ConfigurationMappingNotFoundException("Sorry, couldn't find mapping for " + universalKey + " Are sure you had that configured?"); }
        );
        return Optional.of(val.get());
    }

    @Override
    public UniversallyUniqueMappingDTO save(UniversallyUniqueMappingDTO universallyUniqueMappingDTO) {
        log.debug("Request to save UniversallyUniqueMapping : {}", universallyUniqueMappingDTO);
        UniversallyUniqueMapping universallyUniqueMapping = universallyUniqueMappingMapper.toEntity(universallyUniqueMappingDTO);
        universallyUniqueMapping = internalUUMappingRepository.save(universallyUniqueMapping);
        UniversallyUniqueMappingDTO result = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);
        universallyUniqueMappingSearchRepository.save(universallyUniqueMapping);
        return result;
    }

    @Override
    public Optional<UniversallyUniqueMappingDTO> partialUpdate(UniversallyUniqueMappingDTO universallyUniqueMappingDTO) {
        log.debug("Request to partially update UniversallyUniqueMapping : {}", universallyUniqueMappingDTO);

        return internalUUMappingRepository
            .findById(universallyUniqueMappingDTO.getId())
            .map(existingUniversallyUniqueMapping -> {
                universallyUniqueMappingMapper.partialUpdate(existingUniversallyUniqueMapping, universallyUniqueMappingDTO);

                return existingUniversallyUniqueMapping;
            })
            .map(internalUUMappingRepository::save)
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
        return internalUUMappingRepository.findAll(pageable).map(universallyUniqueMappingMapper::toDto);
    }

    public Page<UniversallyUniqueMappingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return internalUUMappingRepository.findAllWithEagerRelationships(pageable).map(universallyUniqueMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversallyUniqueMappingDTO> findOne(Long id) {
        log.debug("Request to get UniversallyUniqueMapping : {}", id);
        return internalUUMappingRepository.findOneWithEagerRelationships(id).map(universallyUniqueMappingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UniversallyUniqueMapping : {}", id);
        internalUUMappingRepository.deleteById(id);
        universallyUniqueMappingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversallyUniqueMappingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UniversallyUniqueMappings for query {}", query);
        return universallyUniqueMappingSearchRepository.search(query, pageable).map(universallyUniqueMappingMapper::toDto);
    }
}
