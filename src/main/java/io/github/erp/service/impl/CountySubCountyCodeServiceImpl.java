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

import io.github.erp.domain.CountySubCountyCode;
import io.github.erp.repository.CountySubCountyCodeRepository;
import io.github.erp.repository.search.CountySubCountyCodeSearchRepository;
import io.github.erp.service.CountySubCountyCodeService;
import io.github.erp.service.dto.CountySubCountyCodeDTO;
import io.github.erp.service.mapper.CountySubCountyCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountySubCountyCode}.
 */
@Service
@Transactional
public class CountySubCountyCodeServiceImpl implements CountySubCountyCodeService {

    private final Logger log = LoggerFactory.getLogger(CountySubCountyCodeServiceImpl.class);

    private final CountySubCountyCodeRepository countySubCountyCodeRepository;

    private final CountySubCountyCodeMapper countySubCountyCodeMapper;

    private final CountySubCountyCodeSearchRepository countySubCountyCodeSearchRepository;

    public CountySubCountyCodeServiceImpl(
        CountySubCountyCodeRepository countySubCountyCodeRepository,
        CountySubCountyCodeMapper countySubCountyCodeMapper,
        CountySubCountyCodeSearchRepository countySubCountyCodeSearchRepository
    ) {
        this.countySubCountyCodeRepository = countySubCountyCodeRepository;
        this.countySubCountyCodeMapper = countySubCountyCodeMapper;
        this.countySubCountyCodeSearchRepository = countySubCountyCodeSearchRepository;
    }

    @Override
    public CountySubCountyCodeDTO save(CountySubCountyCodeDTO countySubCountyCodeDTO) {
        log.debug("Request to save CountySubCountyCode : {}", countySubCountyCodeDTO);
        CountySubCountyCode countySubCountyCode = countySubCountyCodeMapper.toEntity(countySubCountyCodeDTO);
        countySubCountyCode = countySubCountyCodeRepository.save(countySubCountyCode);
        CountySubCountyCodeDTO result = countySubCountyCodeMapper.toDto(countySubCountyCode);
        countySubCountyCodeSearchRepository.save(countySubCountyCode);
        return result;
    }

    @Override
    public Optional<CountySubCountyCodeDTO> partialUpdate(CountySubCountyCodeDTO countySubCountyCodeDTO) {
        log.debug("Request to partially update CountySubCountyCode : {}", countySubCountyCodeDTO);

        return countySubCountyCodeRepository
            .findById(countySubCountyCodeDTO.getId())
            .map(existingCountySubCountyCode -> {
                countySubCountyCodeMapper.partialUpdate(existingCountySubCountyCode, countySubCountyCodeDTO);

                return existingCountySubCountyCode;
            })
            .map(countySubCountyCodeRepository::save)
            .map(savedCountySubCountyCode -> {
                countySubCountyCodeSearchRepository.save(savedCountySubCountyCode);

                return savedCountySubCountyCode;
            })
            .map(countySubCountyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountySubCountyCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CountySubCountyCodes");
        return countySubCountyCodeRepository.findAll(pageable).map(countySubCountyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CountySubCountyCodeDTO> findOne(Long id) {
        log.debug("Request to get CountySubCountyCode : {}", id);
        return countySubCountyCodeRepository.findById(id).map(countySubCountyCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CountySubCountyCode : {}", id);
        countySubCountyCodeRepository.deleteById(id);
        countySubCountyCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountySubCountyCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CountySubCountyCodes for query {}", query);
        return countySubCountyCodeSearchRepository.search(query, pageable).map(countySubCountyCodeMapper::toDto);
    }
}
