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

import io.github.erp.domain.RemittanceFlag;
import io.github.erp.repository.RemittanceFlagRepository;
import io.github.erp.repository.search.RemittanceFlagSearchRepository;
import io.github.erp.service.RemittanceFlagService;
import io.github.erp.service.dto.RemittanceFlagDTO;
import io.github.erp.service.mapper.RemittanceFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RemittanceFlag}.
 */
@Service
@Transactional
public class RemittanceFlagServiceImpl implements RemittanceFlagService {

    private final Logger log = LoggerFactory.getLogger(RemittanceFlagServiceImpl.class);

    private final RemittanceFlagRepository remittanceFlagRepository;

    private final RemittanceFlagMapper remittanceFlagMapper;

    private final RemittanceFlagSearchRepository remittanceFlagSearchRepository;

    public RemittanceFlagServiceImpl(
        RemittanceFlagRepository remittanceFlagRepository,
        RemittanceFlagMapper remittanceFlagMapper,
        RemittanceFlagSearchRepository remittanceFlagSearchRepository
    ) {
        this.remittanceFlagRepository = remittanceFlagRepository;
        this.remittanceFlagMapper = remittanceFlagMapper;
        this.remittanceFlagSearchRepository = remittanceFlagSearchRepository;
    }

    @Override
    public RemittanceFlagDTO save(RemittanceFlagDTO remittanceFlagDTO) {
        log.debug("Request to save RemittanceFlag : {}", remittanceFlagDTO);
        RemittanceFlag remittanceFlag = remittanceFlagMapper.toEntity(remittanceFlagDTO);
        remittanceFlag = remittanceFlagRepository.save(remittanceFlag);
        RemittanceFlagDTO result = remittanceFlagMapper.toDto(remittanceFlag);
        remittanceFlagSearchRepository.save(remittanceFlag);
        return result;
    }

    @Override
    public Optional<RemittanceFlagDTO> partialUpdate(RemittanceFlagDTO remittanceFlagDTO) {
        log.debug("Request to partially update RemittanceFlag : {}", remittanceFlagDTO);

        return remittanceFlagRepository
            .findById(remittanceFlagDTO.getId())
            .map(existingRemittanceFlag -> {
                remittanceFlagMapper.partialUpdate(existingRemittanceFlag, remittanceFlagDTO);

                return existingRemittanceFlag;
            })
            .map(remittanceFlagRepository::save)
            .map(savedRemittanceFlag -> {
                remittanceFlagSearchRepository.save(savedRemittanceFlag);

                return savedRemittanceFlag;
            })
            .map(remittanceFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RemittanceFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RemittanceFlags");
        return remittanceFlagRepository.findAll(pageable).map(remittanceFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RemittanceFlagDTO> findOne(Long id) {
        log.debug("Request to get RemittanceFlag : {}", id);
        return remittanceFlagRepository.findById(id).map(remittanceFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RemittanceFlag : {}", id);
        remittanceFlagRepository.deleteById(id);
        remittanceFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RemittanceFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RemittanceFlags for query {}", query);
        return remittanceFlagSearchRepository.search(query, pageable).map(remittanceFlagMapper::toDto);
    }
}
