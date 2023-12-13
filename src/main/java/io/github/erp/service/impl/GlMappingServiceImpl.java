package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.domain.GlMapping;
import io.github.erp.repository.GlMappingRepository;
import io.github.erp.repository.search.GlMappingSearchRepository;
import io.github.erp.service.GlMappingService;
import io.github.erp.service.dto.GlMappingDTO;
import io.github.erp.service.mapper.GlMappingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GlMapping}.
 */
@Service
@Transactional
public class GlMappingServiceImpl implements GlMappingService {

    private final Logger log = LoggerFactory.getLogger(GlMappingServiceImpl.class);

    private final GlMappingRepository glMappingRepository;

    private final GlMappingMapper glMappingMapper;

    private final GlMappingSearchRepository glMappingSearchRepository;

    public GlMappingServiceImpl(
        GlMappingRepository glMappingRepository,
        GlMappingMapper glMappingMapper,
        GlMappingSearchRepository glMappingSearchRepository
    ) {
        this.glMappingRepository = glMappingRepository;
        this.glMappingMapper = glMappingMapper;
        this.glMappingSearchRepository = glMappingSearchRepository;
    }

    @Override
    public GlMappingDTO save(GlMappingDTO glMappingDTO) {
        log.debug("Request to save GlMapping : {}", glMappingDTO);
        GlMapping glMapping = glMappingMapper.toEntity(glMappingDTO);
        glMapping = glMappingRepository.save(glMapping);
        GlMappingDTO result = glMappingMapper.toDto(glMapping);
        glMappingSearchRepository.save(glMapping);
        return result;
    }

    @Override
    public Optional<GlMappingDTO> partialUpdate(GlMappingDTO glMappingDTO) {
        log.debug("Request to partially update GlMapping : {}", glMappingDTO);

        return glMappingRepository
            .findById(glMappingDTO.getId())
            .map(existingGlMapping -> {
                glMappingMapper.partialUpdate(existingGlMapping, glMappingDTO);

                return existingGlMapping;
            })
            .map(glMappingRepository::save)
            .map(savedGlMapping -> {
                glMappingSearchRepository.save(savedGlMapping);

                return savedGlMapping;
            })
            .map(glMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GlMappingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GlMappings");
        return glMappingRepository.findAll(pageable).map(glMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GlMappingDTO> findOne(Long id) {
        log.debug("Request to get GlMapping : {}", id);
        return glMappingRepository.findById(id).map(glMappingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GlMapping : {}", id);
        glMappingRepository.deleteById(id);
        glMappingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GlMappingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GlMappings for query {}", query);
        return glMappingSearchRepository.search(query, pageable).map(glMappingMapper::toDto);
    }
}
