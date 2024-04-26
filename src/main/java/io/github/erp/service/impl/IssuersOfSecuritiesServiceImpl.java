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

import io.github.erp.domain.IssuersOfSecurities;
import io.github.erp.repository.IssuersOfSecuritiesRepository;
import io.github.erp.repository.search.IssuersOfSecuritiesSearchRepository;
import io.github.erp.service.IssuersOfSecuritiesService;
import io.github.erp.service.dto.IssuersOfSecuritiesDTO;
import io.github.erp.service.mapper.IssuersOfSecuritiesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IssuersOfSecurities}.
 */
@Service
@Transactional
public class IssuersOfSecuritiesServiceImpl implements IssuersOfSecuritiesService {

    private final Logger log = LoggerFactory.getLogger(IssuersOfSecuritiesServiceImpl.class);

    private final IssuersOfSecuritiesRepository issuersOfSecuritiesRepository;

    private final IssuersOfSecuritiesMapper issuersOfSecuritiesMapper;

    private final IssuersOfSecuritiesSearchRepository issuersOfSecuritiesSearchRepository;

    public IssuersOfSecuritiesServiceImpl(
        IssuersOfSecuritiesRepository issuersOfSecuritiesRepository,
        IssuersOfSecuritiesMapper issuersOfSecuritiesMapper,
        IssuersOfSecuritiesSearchRepository issuersOfSecuritiesSearchRepository
    ) {
        this.issuersOfSecuritiesRepository = issuersOfSecuritiesRepository;
        this.issuersOfSecuritiesMapper = issuersOfSecuritiesMapper;
        this.issuersOfSecuritiesSearchRepository = issuersOfSecuritiesSearchRepository;
    }

    @Override
    public IssuersOfSecuritiesDTO save(IssuersOfSecuritiesDTO issuersOfSecuritiesDTO) {
        log.debug("Request to save IssuersOfSecurities : {}", issuersOfSecuritiesDTO);
        IssuersOfSecurities issuersOfSecurities = issuersOfSecuritiesMapper.toEntity(issuersOfSecuritiesDTO);
        issuersOfSecurities = issuersOfSecuritiesRepository.save(issuersOfSecurities);
        IssuersOfSecuritiesDTO result = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);
        issuersOfSecuritiesSearchRepository.save(issuersOfSecurities);
        return result;
    }

    @Override
    public Optional<IssuersOfSecuritiesDTO> partialUpdate(IssuersOfSecuritiesDTO issuersOfSecuritiesDTO) {
        log.debug("Request to partially update IssuersOfSecurities : {}", issuersOfSecuritiesDTO);

        return issuersOfSecuritiesRepository
            .findById(issuersOfSecuritiesDTO.getId())
            .map(existingIssuersOfSecurities -> {
                issuersOfSecuritiesMapper.partialUpdate(existingIssuersOfSecurities, issuersOfSecuritiesDTO);

                return existingIssuersOfSecurities;
            })
            .map(issuersOfSecuritiesRepository::save)
            .map(savedIssuersOfSecurities -> {
                issuersOfSecuritiesSearchRepository.save(savedIssuersOfSecurities);

                return savedIssuersOfSecurities;
            })
            .map(issuersOfSecuritiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssuersOfSecuritiesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IssuersOfSecurities");
        return issuersOfSecuritiesRepository.findAll(pageable).map(issuersOfSecuritiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IssuersOfSecuritiesDTO> findOne(Long id) {
        log.debug("Request to get IssuersOfSecurities : {}", id);
        return issuersOfSecuritiesRepository.findById(id).map(issuersOfSecuritiesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IssuersOfSecurities : {}", id);
        issuersOfSecuritiesRepository.deleteById(id);
        issuersOfSecuritiesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssuersOfSecuritiesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IssuersOfSecurities for query {}", query);
        return issuersOfSecuritiesSearchRepository.search(query, pageable).map(issuersOfSecuritiesMapper::toDto);
    }
}
