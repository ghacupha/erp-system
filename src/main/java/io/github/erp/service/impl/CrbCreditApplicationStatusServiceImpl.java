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

import io.github.erp.domain.CrbCreditApplicationStatus;
import io.github.erp.repository.CrbCreditApplicationStatusRepository;
import io.github.erp.repository.search.CrbCreditApplicationStatusSearchRepository;
import io.github.erp.service.CrbCreditApplicationStatusService;
import io.github.erp.service.dto.CrbCreditApplicationStatusDTO;
import io.github.erp.service.mapper.CrbCreditApplicationStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbCreditApplicationStatus}.
 */
@Service
@Transactional
public class CrbCreditApplicationStatusServiceImpl implements CrbCreditApplicationStatusService {

    private final Logger log = LoggerFactory.getLogger(CrbCreditApplicationStatusServiceImpl.class);

    private final CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository;

    private final CrbCreditApplicationStatusMapper crbCreditApplicationStatusMapper;

    private final CrbCreditApplicationStatusSearchRepository crbCreditApplicationStatusSearchRepository;

    public CrbCreditApplicationStatusServiceImpl(
        CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository,
        CrbCreditApplicationStatusMapper crbCreditApplicationStatusMapper,
        CrbCreditApplicationStatusSearchRepository crbCreditApplicationStatusSearchRepository
    ) {
        this.crbCreditApplicationStatusRepository = crbCreditApplicationStatusRepository;
        this.crbCreditApplicationStatusMapper = crbCreditApplicationStatusMapper;
        this.crbCreditApplicationStatusSearchRepository = crbCreditApplicationStatusSearchRepository;
    }

    @Override
    public CrbCreditApplicationStatusDTO save(CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO) {
        log.debug("Request to save CrbCreditApplicationStatus : {}", crbCreditApplicationStatusDTO);
        CrbCreditApplicationStatus crbCreditApplicationStatus = crbCreditApplicationStatusMapper.toEntity(crbCreditApplicationStatusDTO);
        crbCreditApplicationStatus = crbCreditApplicationStatusRepository.save(crbCreditApplicationStatus);
        CrbCreditApplicationStatusDTO result = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);
        crbCreditApplicationStatusSearchRepository.save(crbCreditApplicationStatus);
        return result;
    }

    @Override
    public Optional<CrbCreditApplicationStatusDTO> partialUpdate(CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO) {
        log.debug("Request to partially update CrbCreditApplicationStatus : {}", crbCreditApplicationStatusDTO);

        return crbCreditApplicationStatusRepository
            .findById(crbCreditApplicationStatusDTO.getId())
            .map(existingCrbCreditApplicationStatus -> {
                crbCreditApplicationStatusMapper.partialUpdate(existingCrbCreditApplicationStatus, crbCreditApplicationStatusDTO);

                return existingCrbCreditApplicationStatus;
            })
            .map(crbCreditApplicationStatusRepository::save)
            .map(savedCrbCreditApplicationStatus -> {
                crbCreditApplicationStatusSearchRepository.save(savedCrbCreditApplicationStatus);

                return savedCrbCreditApplicationStatus;
            })
            .map(crbCreditApplicationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbCreditApplicationStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbCreditApplicationStatuses");
        return crbCreditApplicationStatusRepository.findAll(pageable).map(crbCreditApplicationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbCreditApplicationStatusDTO> findOne(Long id) {
        log.debug("Request to get CrbCreditApplicationStatus : {}", id);
        return crbCreditApplicationStatusRepository.findById(id).map(crbCreditApplicationStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbCreditApplicationStatus : {}", id);
        crbCreditApplicationStatusRepository.deleteById(id);
        crbCreditApplicationStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbCreditApplicationStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbCreditApplicationStatuses for query {}", query);
        return crbCreditApplicationStatusSearchRepository.search(query, pageable).map(crbCreditApplicationStatusMapper::toDto);
    }
}
