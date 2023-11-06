package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
