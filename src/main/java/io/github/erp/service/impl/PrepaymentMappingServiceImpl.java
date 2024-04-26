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

import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.repository.PrepaymentMappingRepository;
import io.github.erp.repository.search.PrepaymentMappingSearchRepository;
import io.github.erp.service.PrepaymentMappingService;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import io.github.erp.service.mapper.PrepaymentMappingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentMapping}.
 */
@Service
@Transactional
public class PrepaymentMappingServiceImpl implements PrepaymentMappingService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentMappingServiceImpl.class);

    private final PrepaymentMappingRepository prepaymentMappingRepository;

    private final PrepaymentMappingMapper prepaymentMappingMapper;

    private final PrepaymentMappingSearchRepository prepaymentMappingSearchRepository;

    public PrepaymentMappingServiceImpl(
        PrepaymentMappingRepository prepaymentMappingRepository,
        PrepaymentMappingMapper prepaymentMappingMapper,
        PrepaymentMappingSearchRepository prepaymentMappingSearchRepository
    ) {
        this.prepaymentMappingRepository = prepaymentMappingRepository;
        this.prepaymentMappingMapper = prepaymentMappingMapper;
        this.prepaymentMappingSearchRepository = prepaymentMappingSearchRepository;
    }

    @Override
    public PrepaymentMappingDTO save(PrepaymentMappingDTO prepaymentMappingDTO) {
        log.debug("Request to save PrepaymentMapping : {}", prepaymentMappingDTO);
        PrepaymentMapping prepaymentMapping = prepaymentMappingMapper.toEntity(prepaymentMappingDTO);
        prepaymentMapping = prepaymentMappingRepository.save(prepaymentMapping);
        PrepaymentMappingDTO result = prepaymentMappingMapper.toDto(prepaymentMapping);
        prepaymentMappingSearchRepository.save(prepaymentMapping);
        return result;
    }

    @Override
    public Optional<PrepaymentMappingDTO> partialUpdate(PrepaymentMappingDTO prepaymentMappingDTO) {
        log.debug("Request to partially update PrepaymentMapping : {}", prepaymentMappingDTO);

        return prepaymentMappingRepository
            .findById(prepaymentMappingDTO.getId())
            .map(existingPrepaymentMapping -> {
                prepaymentMappingMapper.partialUpdate(existingPrepaymentMapping, prepaymentMappingDTO);

                return existingPrepaymentMapping;
            })
            .map(prepaymentMappingRepository::save)
            .map(savedPrepaymentMapping -> {
                prepaymentMappingSearchRepository.save(savedPrepaymentMapping);

                return savedPrepaymentMapping;
            })
            .map(prepaymentMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentMappingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentMappings");
        return prepaymentMappingRepository.findAll(pageable).map(prepaymentMappingMapper::toDto);
    }

    public Page<PrepaymentMappingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prepaymentMappingRepository.findAllWithEagerRelationships(pageable).map(prepaymentMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentMappingDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentMapping : {}", id);
        return prepaymentMappingRepository.findOneWithEagerRelationships(id).map(prepaymentMappingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentMapping : {}", id);
        prepaymentMappingRepository.deleteById(id);
        prepaymentMappingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentMappingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentMappings for query {}", query);
        return prepaymentMappingSearchRepository.search(query, pageable).map(prepaymentMappingMapper::toDto);
    }
}
