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

import io.github.erp.domain.LegalStatus;
import io.github.erp.repository.LegalStatusRepository;
import io.github.erp.repository.search.LegalStatusSearchRepository;
import io.github.erp.service.LegalStatusService;
import io.github.erp.service.dto.LegalStatusDTO;
import io.github.erp.service.mapper.LegalStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LegalStatus}.
 */
@Service
@Transactional
public class LegalStatusServiceImpl implements LegalStatusService {

    private final Logger log = LoggerFactory.getLogger(LegalStatusServiceImpl.class);

    private final LegalStatusRepository legalStatusRepository;

    private final LegalStatusMapper legalStatusMapper;

    private final LegalStatusSearchRepository legalStatusSearchRepository;

    public LegalStatusServiceImpl(
        LegalStatusRepository legalStatusRepository,
        LegalStatusMapper legalStatusMapper,
        LegalStatusSearchRepository legalStatusSearchRepository
    ) {
        this.legalStatusRepository = legalStatusRepository;
        this.legalStatusMapper = legalStatusMapper;
        this.legalStatusSearchRepository = legalStatusSearchRepository;
    }

    @Override
    public LegalStatusDTO save(LegalStatusDTO legalStatusDTO) {
        log.debug("Request to save LegalStatus : {}", legalStatusDTO);
        LegalStatus legalStatus = legalStatusMapper.toEntity(legalStatusDTO);
        legalStatus = legalStatusRepository.save(legalStatus);
        LegalStatusDTO result = legalStatusMapper.toDto(legalStatus);
        legalStatusSearchRepository.save(legalStatus);
        return result;
    }

    @Override
    public Optional<LegalStatusDTO> partialUpdate(LegalStatusDTO legalStatusDTO) {
        log.debug("Request to partially update LegalStatus : {}", legalStatusDTO);

        return legalStatusRepository
            .findById(legalStatusDTO.getId())
            .map(existingLegalStatus -> {
                legalStatusMapper.partialUpdate(existingLegalStatus, legalStatusDTO);

                return existingLegalStatus;
            })
            .map(legalStatusRepository::save)
            .map(savedLegalStatus -> {
                legalStatusSearchRepository.save(savedLegalStatus);

                return savedLegalStatus;
            })
            .map(legalStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LegalStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LegalStatuses");
        return legalStatusRepository.findAll(pageable).map(legalStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LegalStatusDTO> findOne(Long id) {
        log.debug("Request to get LegalStatus : {}", id);
        return legalStatusRepository.findById(id).map(legalStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LegalStatus : {}", id);
        legalStatusRepository.deleteById(id);
        legalStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LegalStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LegalStatuses for query {}", query);
        return legalStatusSearchRepository.search(query, pageable).map(legalStatusMapper::toDto);
    }
}
