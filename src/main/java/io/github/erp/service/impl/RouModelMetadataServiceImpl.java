package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.domain.RouModelMetadata;
import io.github.erp.repository.RouModelMetadataRepository;
import io.github.erp.repository.search.RouModelMetadataSearchRepository;
import io.github.erp.service.RouModelMetadataService;
import io.github.erp.service.dto.RouModelMetadataDTO;
import io.github.erp.service.mapper.RouModelMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouModelMetadata}.
 */
@Service
@Transactional
public class RouModelMetadataServiceImpl implements RouModelMetadataService {

    private final Logger log = LoggerFactory.getLogger(RouModelMetadataServiceImpl.class);

    private final RouModelMetadataRepository rouModelMetadataRepository;

    private final RouModelMetadataMapper rouModelMetadataMapper;

    private final RouModelMetadataSearchRepository rouModelMetadataSearchRepository;

    public RouModelMetadataServiceImpl(
        RouModelMetadataRepository rouModelMetadataRepository,
        RouModelMetadataMapper rouModelMetadataMapper,
        RouModelMetadataSearchRepository rouModelMetadataSearchRepository
    ) {
        this.rouModelMetadataRepository = rouModelMetadataRepository;
        this.rouModelMetadataMapper = rouModelMetadataMapper;
        this.rouModelMetadataSearchRepository = rouModelMetadataSearchRepository;
    }

    @Override
    public RouModelMetadataDTO save(RouModelMetadataDTO rouModelMetadataDTO) {
        log.debug("Request to save RouModelMetadata : {}", rouModelMetadataDTO);
        RouModelMetadata rouModelMetadata = rouModelMetadataMapper.toEntity(rouModelMetadataDTO);
        rouModelMetadata = rouModelMetadataRepository.save(rouModelMetadata);
        RouModelMetadataDTO result = rouModelMetadataMapper.toDto(rouModelMetadata);
        rouModelMetadataSearchRepository.save(rouModelMetadata);
        return result;
    }

    @Override
    public Optional<RouModelMetadataDTO> partialUpdate(RouModelMetadataDTO rouModelMetadataDTO) {
        log.debug("Request to partially update RouModelMetadata : {}", rouModelMetadataDTO);

        return rouModelMetadataRepository
            .findById(rouModelMetadataDTO.getId())
            .map(existingRouModelMetadata -> {
                rouModelMetadataMapper.partialUpdate(existingRouModelMetadata, rouModelMetadataDTO);

                return existingRouModelMetadata;
            })
            .map(rouModelMetadataRepository::save)
            .map(savedRouModelMetadata -> {
                rouModelMetadataSearchRepository.save(savedRouModelMetadata);

                return savedRouModelMetadata;
            })
            .map(rouModelMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouModelMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouModelMetadata");
        return rouModelMetadataRepository.findAll(pageable).map(rouModelMetadataMapper::toDto);
    }

    public Page<RouModelMetadataDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rouModelMetadataRepository.findAllWithEagerRelationships(pageable).map(rouModelMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouModelMetadataDTO> findOne(Long id) {
        log.debug("Request to get RouModelMetadata : {}", id);
        return rouModelMetadataRepository.findOneWithEagerRelationships(id).map(rouModelMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouModelMetadata : {}", id);
        rouModelMetadataRepository.deleteById(id);
        rouModelMetadataSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouModelMetadataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouModelMetadata for query {}", query);
        return rouModelMetadataSearchRepository.search(query, pageable).map(rouModelMetadataMapper::toDto);
    }
}
