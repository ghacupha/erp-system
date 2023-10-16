package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
