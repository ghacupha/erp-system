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

import io.github.erp.domain.AcquiringIssuingFlag;
import io.github.erp.repository.AcquiringIssuingFlagRepository;
import io.github.erp.repository.search.AcquiringIssuingFlagSearchRepository;
import io.github.erp.service.AcquiringIssuingFlagService;
import io.github.erp.service.dto.AcquiringIssuingFlagDTO;
import io.github.erp.service.mapper.AcquiringIssuingFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AcquiringIssuingFlag}.
 */
@Service
@Transactional
public class AcquiringIssuingFlagServiceImpl implements AcquiringIssuingFlagService {

    private final Logger log = LoggerFactory.getLogger(AcquiringIssuingFlagServiceImpl.class);

    private final AcquiringIssuingFlagRepository acquiringIssuingFlagRepository;

    private final AcquiringIssuingFlagMapper acquiringIssuingFlagMapper;

    private final AcquiringIssuingFlagSearchRepository acquiringIssuingFlagSearchRepository;

    public AcquiringIssuingFlagServiceImpl(
        AcquiringIssuingFlagRepository acquiringIssuingFlagRepository,
        AcquiringIssuingFlagMapper acquiringIssuingFlagMapper,
        AcquiringIssuingFlagSearchRepository acquiringIssuingFlagSearchRepository
    ) {
        this.acquiringIssuingFlagRepository = acquiringIssuingFlagRepository;
        this.acquiringIssuingFlagMapper = acquiringIssuingFlagMapper;
        this.acquiringIssuingFlagSearchRepository = acquiringIssuingFlagSearchRepository;
    }

    @Override
    public AcquiringIssuingFlagDTO save(AcquiringIssuingFlagDTO acquiringIssuingFlagDTO) {
        log.debug("Request to save AcquiringIssuingFlag : {}", acquiringIssuingFlagDTO);
        AcquiringIssuingFlag acquiringIssuingFlag = acquiringIssuingFlagMapper.toEntity(acquiringIssuingFlagDTO);
        acquiringIssuingFlag = acquiringIssuingFlagRepository.save(acquiringIssuingFlag);
        AcquiringIssuingFlagDTO result = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);
        acquiringIssuingFlagSearchRepository.save(acquiringIssuingFlag);
        return result;
    }

    @Override
    public Optional<AcquiringIssuingFlagDTO> partialUpdate(AcquiringIssuingFlagDTO acquiringIssuingFlagDTO) {
        log.debug("Request to partially update AcquiringIssuingFlag : {}", acquiringIssuingFlagDTO);

        return acquiringIssuingFlagRepository
            .findById(acquiringIssuingFlagDTO.getId())
            .map(existingAcquiringIssuingFlag -> {
                acquiringIssuingFlagMapper.partialUpdate(existingAcquiringIssuingFlag, acquiringIssuingFlagDTO);

                return existingAcquiringIssuingFlag;
            })
            .map(acquiringIssuingFlagRepository::save)
            .map(savedAcquiringIssuingFlag -> {
                acquiringIssuingFlagSearchRepository.save(savedAcquiringIssuingFlag);

                return savedAcquiringIssuingFlag;
            })
            .map(acquiringIssuingFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcquiringIssuingFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AcquiringIssuingFlags");
        return acquiringIssuingFlagRepository.findAll(pageable).map(acquiringIssuingFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcquiringIssuingFlagDTO> findOne(Long id) {
        log.debug("Request to get AcquiringIssuingFlag : {}", id);
        return acquiringIssuingFlagRepository.findById(id).map(acquiringIssuingFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AcquiringIssuingFlag : {}", id);
        acquiringIssuingFlagRepository.deleteById(id);
        acquiringIssuingFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcquiringIssuingFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AcquiringIssuingFlags for query {}", query);
        return acquiringIssuingFlagSearchRepository.search(query, pageable).map(acquiringIssuingFlagMapper::toDto);
    }
}
