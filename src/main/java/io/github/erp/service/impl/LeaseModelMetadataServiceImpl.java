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

import io.github.erp.domain.LeaseModelMetadata;
import io.github.erp.repository.LeaseModelMetadataRepository;
import io.github.erp.repository.search.LeaseModelMetadataSearchRepository;
import io.github.erp.service.LeaseModelMetadataService;
import io.github.erp.service.dto.LeaseModelMetadataDTO;
import io.github.erp.service.mapper.LeaseModelMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseModelMetadata}.
 */
@Service
@Transactional
public class LeaseModelMetadataServiceImpl implements LeaseModelMetadataService {

    private final Logger log = LoggerFactory.getLogger(LeaseModelMetadataServiceImpl.class);

    private final LeaseModelMetadataRepository leaseModelMetadataRepository;

    private final LeaseModelMetadataMapper leaseModelMetadataMapper;

    private final LeaseModelMetadataSearchRepository leaseModelMetadataSearchRepository;

    public LeaseModelMetadataServiceImpl(
        LeaseModelMetadataRepository leaseModelMetadataRepository,
        LeaseModelMetadataMapper leaseModelMetadataMapper,
        LeaseModelMetadataSearchRepository leaseModelMetadataSearchRepository
    ) {
        this.leaseModelMetadataRepository = leaseModelMetadataRepository;
        this.leaseModelMetadataMapper = leaseModelMetadataMapper;
        this.leaseModelMetadataSearchRepository = leaseModelMetadataSearchRepository;
    }

    @Override
    public LeaseModelMetadataDTO save(LeaseModelMetadataDTO leaseModelMetadataDTO) {
        log.debug("Request to save LeaseModelMetadata : {}", leaseModelMetadataDTO);
        LeaseModelMetadata leaseModelMetadata = leaseModelMetadataMapper.toEntity(leaseModelMetadataDTO);
        leaseModelMetadata = leaseModelMetadataRepository.save(leaseModelMetadata);
        LeaseModelMetadataDTO result = leaseModelMetadataMapper.toDto(leaseModelMetadata);
        leaseModelMetadataSearchRepository.save(leaseModelMetadata);
        return result;
    }

    @Override
    public Optional<LeaseModelMetadataDTO> partialUpdate(LeaseModelMetadataDTO leaseModelMetadataDTO) {
        log.debug("Request to partially update LeaseModelMetadata : {}", leaseModelMetadataDTO);

        return leaseModelMetadataRepository
            .findById(leaseModelMetadataDTO.getId())
            .map(existingLeaseModelMetadata -> {
                leaseModelMetadataMapper.partialUpdate(existingLeaseModelMetadata, leaseModelMetadataDTO);

                return existingLeaseModelMetadata;
            })
            .map(leaseModelMetadataRepository::save)
            .map(savedLeaseModelMetadata -> {
                leaseModelMetadataSearchRepository.save(savedLeaseModelMetadata);

                return savedLeaseModelMetadata;
            })
            .map(leaseModelMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseModelMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseModelMetadata");
        return leaseModelMetadataRepository.findAll(pageable).map(leaseModelMetadataMapper::toDto);
    }

    public Page<LeaseModelMetadataDTO> findAllWithEagerRelationships(Pageable pageable) {
        return leaseModelMetadataRepository.findAllWithEagerRelationships(pageable).map(leaseModelMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseModelMetadataDTO> findOne(Long id) {
        log.debug("Request to get LeaseModelMetadata : {}", id);
        return leaseModelMetadataRepository.findOneWithEagerRelationships(id).map(leaseModelMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseModelMetadata : {}", id);
        leaseModelMetadataRepository.deleteById(id);
        leaseModelMetadataSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseModelMetadataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseModelMetadata for query {}", query);
        return leaseModelMetadataSearchRepository.search(query, pageable).map(leaseModelMetadataMapper::toDto);
    }
}
