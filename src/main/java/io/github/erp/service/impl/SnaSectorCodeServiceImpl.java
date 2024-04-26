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

import io.github.erp.domain.SnaSectorCode;
import io.github.erp.repository.SnaSectorCodeRepository;
import io.github.erp.repository.search.SnaSectorCodeSearchRepository;
import io.github.erp.service.SnaSectorCodeService;
import io.github.erp.service.dto.SnaSectorCodeDTO;
import io.github.erp.service.mapper.SnaSectorCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SnaSectorCode}.
 */
@Service
@Transactional
public class SnaSectorCodeServiceImpl implements SnaSectorCodeService {

    private final Logger log = LoggerFactory.getLogger(SnaSectorCodeServiceImpl.class);

    private final SnaSectorCodeRepository snaSectorCodeRepository;

    private final SnaSectorCodeMapper snaSectorCodeMapper;

    private final SnaSectorCodeSearchRepository snaSectorCodeSearchRepository;

    public SnaSectorCodeServiceImpl(
        SnaSectorCodeRepository snaSectorCodeRepository,
        SnaSectorCodeMapper snaSectorCodeMapper,
        SnaSectorCodeSearchRepository snaSectorCodeSearchRepository
    ) {
        this.snaSectorCodeRepository = snaSectorCodeRepository;
        this.snaSectorCodeMapper = snaSectorCodeMapper;
        this.snaSectorCodeSearchRepository = snaSectorCodeSearchRepository;
    }

    @Override
    public SnaSectorCodeDTO save(SnaSectorCodeDTO snaSectorCodeDTO) {
        log.debug("Request to save SnaSectorCode : {}", snaSectorCodeDTO);
        SnaSectorCode snaSectorCode = snaSectorCodeMapper.toEntity(snaSectorCodeDTO);
        snaSectorCode = snaSectorCodeRepository.save(snaSectorCode);
        SnaSectorCodeDTO result = snaSectorCodeMapper.toDto(snaSectorCode);
        snaSectorCodeSearchRepository.save(snaSectorCode);
        return result;
    }

    @Override
    public Optional<SnaSectorCodeDTO> partialUpdate(SnaSectorCodeDTO snaSectorCodeDTO) {
        log.debug("Request to partially update SnaSectorCode : {}", snaSectorCodeDTO);

        return snaSectorCodeRepository
            .findById(snaSectorCodeDTO.getId())
            .map(existingSnaSectorCode -> {
                snaSectorCodeMapper.partialUpdate(existingSnaSectorCode, snaSectorCodeDTO);

                return existingSnaSectorCode;
            })
            .map(snaSectorCodeRepository::save)
            .map(savedSnaSectorCode -> {
                snaSectorCodeSearchRepository.save(savedSnaSectorCode);

                return savedSnaSectorCode;
            })
            .map(snaSectorCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SnaSectorCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SnaSectorCodes");
        return snaSectorCodeRepository.findAll(pageable).map(snaSectorCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SnaSectorCodeDTO> findOne(Long id) {
        log.debug("Request to get SnaSectorCode : {}", id);
        return snaSectorCodeRepository.findById(id).map(snaSectorCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SnaSectorCode : {}", id);
        snaSectorCodeRepository.deleteById(id);
        snaSectorCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SnaSectorCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SnaSectorCodes for query {}", query);
        return snaSectorCodeSearchRepository.search(query, pageable).map(snaSectorCodeMapper::toDto);
    }
}
