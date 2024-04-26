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

import io.github.erp.domain.CountyCode;
import io.github.erp.repository.CountyCodeRepository;
import io.github.erp.repository.search.CountyCodeSearchRepository;
import io.github.erp.service.CountyCodeService;
import io.github.erp.service.dto.CountyCodeDTO;
import io.github.erp.service.mapper.CountyCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountyCode}.
 */
@Service
@Transactional
public class CountyCodeServiceImpl implements CountyCodeService {

    private final Logger log = LoggerFactory.getLogger(CountyCodeServiceImpl.class);

    private final CountyCodeRepository countyCodeRepository;

    private final CountyCodeMapper countyCodeMapper;

    private final CountyCodeSearchRepository countyCodeSearchRepository;

    public CountyCodeServiceImpl(
        CountyCodeRepository countyCodeRepository,
        CountyCodeMapper countyCodeMapper,
        CountyCodeSearchRepository countyCodeSearchRepository
    ) {
        this.countyCodeRepository = countyCodeRepository;
        this.countyCodeMapper = countyCodeMapper;
        this.countyCodeSearchRepository = countyCodeSearchRepository;
    }

    @Override
    public CountyCodeDTO save(CountyCodeDTO countyCodeDTO) {
        log.debug("Request to save CountyCode : {}", countyCodeDTO);
        CountyCode countyCode = countyCodeMapper.toEntity(countyCodeDTO);
        countyCode = countyCodeRepository.save(countyCode);
        CountyCodeDTO result = countyCodeMapper.toDto(countyCode);
        countyCodeSearchRepository.save(countyCode);
        return result;
    }

    @Override
    public Optional<CountyCodeDTO> partialUpdate(CountyCodeDTO countyCodeDTO) {
        log.debug("Request to partially update CountyCode : {}", countyCodeDTO);

        return countyCodeRepository
            .findById(countyCodeDTO.getId())
            .map(existingCountyCode -> {
                countyCodeMapper.partialUpdate(existingCountyCode, countyCodeDTO);

                return existingCountyCode;
            })
            .map(countyCodeRepository::save)
            .map(savedCountyCode -> {
                countyCodeSearchRepository.save(savedCountyCode);

                return savedCountyCode;
            })
            .map(countyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountyCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CountyCodes");
        return countyCodeRepository.findAll(pageable).map(countyCodeMapper::toDto);
    }

    public Page<CountyCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return countyCodeRepository.findAllWithEagerRelationships(pageable).map(countyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CountyCodeDTO> findOne(Long id) {
        log.debug("Request to get CountyCode : {}", id);
        return countyCodeRepository.findOneWithEagerRelationships(id).map(countyCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CountyCode : {}", id);
        countyCodeRepository.deleteById(id);
        countyCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountyCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CountyCodes for query {}", query);
        return countyCodeSearchRepository.search(query, pageable).map(countyCodeMapper::toDto);
    }
}
