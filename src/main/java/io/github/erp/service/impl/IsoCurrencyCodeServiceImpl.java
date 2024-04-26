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

import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.IsoCurrencyCodeRepository;
import io.github.erp.repository.search.IsoCurrencyCodeSearchRepository;
import io.github.erp.service.IsoCurrencyCodeService;
import io.github.erp.service.dto.IsoCurrencyCodeDTO;
import io.github.erp.service.mapper.IsoCurrencyCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsoCurrencyCode}.
 */
@Service
@Transactional
public class IsoCurrencyCodeServiceImpl implements IsoCurrencyCodeService {

    private final Logger log = LoggerFactory.getLogger(IsoCurrencyCodeServiceImpl.class);

    private final IsoCurrencyCodeRepository isoCurrencyCodeRepository;

    private final IsoCurrencyCodeMapper isoCurrencyCodeMapper;

    private final IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository;

    public IsoCurrencyCodeServiceImpl(
        IsoCurrencyCodeRepository isoCurrencyCodeRepository,
        IsoCurrencyCodeMapper isoCurrencyCodeMapper,
        IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository
    ) {
        this.isoCurrencyCodeRepository = isoCurrencyCodeRepository;
        this.isoCurrencyCodeMapper = isoCurrencyCodeMapper;
        this.isoCurrencyCodeSearchRepository = isoCurrencyCodeSearchRepository;
    }

    @Override
    public IsoCurrencyCodeDTO save(IsoCurrencyCodeDTO isoCurrencyCodeDTO) {
        log.debug("Request to save IsoCurrencyCode : {}", isoCurrencyCodeDTO);
        IsoCurrencyCode isoCurrencyCode = isoCurrencyCodeMapper.toEntity(isoCurrencyCodeDTO);
        isoCurrencyCode = isoCurrencyCodeRepository.save(isoCurrencyCode);
        IsoCurrencyCodeDTO result = isoCurrencyCodeMapper.toDto(isoCurrencyCode);
        isoCurrencyCodeSearchRepository.save(isoCurrencyCode);
        return result;
    }

    @Override
    public Optional<IsoCurrencyCodeDTO> partialUpdate(IsoCurrencyCodeDTO isoCurrencyCodeDTO) {
        log.debug("Request to partially update IsoCurrencyCode : {}", isoCurrencyCodeDTO);

        return isoCurrencyCodeRepository
            .findById(isoCurrencyCodeDTO.getId())
            .map(existingIsoCurrencyCode -> {
                isoCurrencyCodeMapper.partialUpdate(existingIsoCurrencyCode, isoCurrencyCodeDTO);

                return existingIsoCurrencyCode;
            })
            .map(isoCurrencyCodeRepository::save)
            .map(savedIsoCurrencyCode -> {
                isoCurrencyCodeSearchRepository.save(savedIsoCurrencyCode);

                return savedIsoCurrencyCode;
            })
            .map(isoCurrencyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsoCurrencyCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IsoCurrencyCodes");
        return isoCurrencyCodeRepository.findAll(pageable).map(isoCurrencyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsoCurrencyCodeDTO> findOne(Long id) {
        log.debug("Request to get IsoCurrencyCode : {}", id);
        return isoCurrencyCodeRepository.findById(id).map(isoCurrencyCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IsoCurrencyCode : {}", id);
        isoCurrencyCodeRepository.deleteById(id);
        isoCurrencyCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsoCurrencyCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IsoCurrencyCodes for query {}", query);
        return isoCurrencyCodeSearchRepository.search(query, pageable).map(isoCurrencyCodeMapper::toDto);
    }
}
