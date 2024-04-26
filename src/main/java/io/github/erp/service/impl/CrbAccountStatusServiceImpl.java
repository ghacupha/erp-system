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

import io.github.erp.domain.CrbAccountStatus;
import io.github.erp.repository.CrbAccountStatusRepository;
import io.github.erp.repository.search.CrbAccountStatusSearchRepository;
import io.github.erp.service.CrbAccountStatusService;
import io.github.erp.service.dto.CrbAccountStatusDTO;
import io.github.erp.service.mapper.CrbAccountStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAccountStatus}.
 */
@Service
@Transactional
public class CrbAccountStatusServiceImpl implements CrbAccountStatusService {

    private final Logger log = LoggerFactory.getLogger(CrbAccountStatusServiceImpl.class);

    private final CrbAccountStatusRepository crbAccountStatusRepository;

    private final CrbAccountStatusMapper crbAccountStatusMapper;

    private final CrbAccountStatusSearchRepository crbAccountStatusSearchRepository;

    public CrbAccountStatusServiceImpl(
        CrbAccountStatusRepository crbAccountStatusRepository,
        CrbAccountStatusMapper crbAccountStatusMapper,
        CrbAccountStatusSearchRepository crbAccountStatusSearchRepository
    ) {
        this.crbAccountStatusRepository = crbAccountStatusRepository;
        this.crbAccountStatusMapper = crbAccountStatusMapper;
        this.crbAccountStatusSearchRepository = crbAccountStatusSearchRepository;
    }

    @Override
    public CrbAccountStatusDTO save(CrbAccountStatusDTO crbAccountStatusDTO) {
        log.debug("Request to save CrbAccountStatus : {}", crbAccountStatusDTO);
        CrbAccountStatus crbAccountStatus = crbAccountStatusMapper.toEntity(crbAccountStatusDTO);
        crbAccountStatus = crbAccountStatusRepository.save(crbAccountStatus);
        CrbAccountStatusDTO result = crbAccountStatusMapper.toDto(crbAccountStatus);
        crbAccountStatusSearchRepository.save(crbAccountStatus);
        return result;
    }

    @Override
    public Optional<CrbAccountStatusDTO> partialUpdate(CrbAccountStatusDTO crbAccountStatusDTO) {
        log.debug("Request to partially update CrbAccountStatus : {}", crbAccountStatusDTO);

        return crbAccountStatusRepository
            .findById(crbAccountStatusDTO.getId())
            .map(existingCrbAccountStatus -> {
                crbAccountStatusMapper.partialUpdate(existingCrbAccountStatus, crbAccountStatusDTO);

                return existingCrbAccountStatus;
            })
            .map(crbAccountStatusRepository::save)
            .map(savedCrbAccountStatus -> {
                crbAccountStatusSearchRepository.save(savedCrbAccountStatus);

                return savedCrbAccountStatus;
            })
            .map(crbAccountStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAccountStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAccountStatuses");
        return crbAccountStatusRepository.findAll(pageable).map(crbAccountStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAccountStatusDTO> findOne(Long id) {
        log.debug("Request to get CrbAccountStatus : {}", id);
        return crbAccountStatusRepository.findById(id).map(crbAccountStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAccountStatus : {}", id);
        crbAccountStatusRepository.deleteById(id);
        crbAccountStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAccountStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAccountStatuses for query {}", query);
        return crbAccountStatusSearchRepository.search(query, pageable).map(crbAccountStatusMapper::toDto);
    }
}
