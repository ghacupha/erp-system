package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
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
