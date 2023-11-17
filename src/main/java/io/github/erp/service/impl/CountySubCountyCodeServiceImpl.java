package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
