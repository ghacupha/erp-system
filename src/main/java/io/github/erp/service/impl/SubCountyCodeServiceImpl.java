package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.SubCountyCode;
import io.github.erp.repository.SubCountyCodeRepository;
import io.github.erp.repository.search.SubCountyCodeSearchRepository;
import io.github.erp.service.SubCountyCodeService;
import io.github.erp.service.dto.SubCountyCodeDTO;
import io.github.erp.service.mapper.SubCountyCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubCountyCode}.
 */
@Service
@Transactional
public class SubCountyCodeServiceImpl implements SubCountyCodeService {

    private final Logger log = LoggerFactory.getLogger(SubCountyCodeServiceImpl.class);

    private final SubCountyCodeRepository subCountyCodeRepository;

    private final SubCountyCodeMapper subCountyCodeMapper;

    private final SubCountyCodeSearchRepository subCountyCodeSearchRepository;

    public SubCountyCodeServiceImpl(
        SubCountyCodeRepository subCountyCodeRepository,
        SubCountyCodeMapper subCountyCodeMapper,
        SubCountyCodeSearchRepository subCountyCodeSearchRepository
    ) {
        this.subCountyCodeRepository = subCountyCodeRepository;
        this.subCountyCodeMapper = subCountyCodeMapper;
        this.subCountyCodeSearchRepository = subCountyCodeSearchRepository;
    }

    @Override
    public SubCountyCodeDTO save(SubCountyCodeDTO subCountyCodeDTO) {
        log.debug("Request to save SubCountyCode : {}", subCountyCodeDTO);
        SubCountyCode subCountyCode = subCountyCodeMapper.toEntity(subCountyCodeDTO);
        subCountyCode = subCountyCodeRepository.save(subCountyCode);
        SubCountyCodeDTO result = subCountyCodeMapper.toDto(subCountyCode);
        subCountyCodeSearchRepository.save(subCountyCode);
        return result;
    }

    @Override
    public Optional<SubCountyCodeDTO> partialUpdate(SubCountyCodeDTO subCountyCodeDTO) {
        log.debug("Request to partially update SubCountyCode : {}", subCountyCodeDTO);

        return subCountyCodeRepository
            .findById(subCountyCodeDTO.getId())
            .map(existingSubCountyCode -> {
                subCountyCodeMapper.partialUpdate(existingSubCountyCode, subCountyCodeDTO);

                return existingSubCountyCode;
            })
            .map(subCountyCodeRepository::save)
            .map(savedSubCountyCode -> {
                subCountyCodeSearchRepository.save(savedSubCountyCode);

                return savedSubCountyCode;
            })
            .map(subCountyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCountyCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubCountyCodes");
        return subCountyCodeRepository.findAll(pageable).map(subCountyCodeMapper::toDto);
    }

    public Page<SubCountyCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return subCountyCodeRepository.findAllWithEagerRelationships(pageable).map(subCountyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubCountyCodeDTO> findOne(Long id) {
        log.debug("Request to get SubCountyCode : {}", id);
        return subCountyCodeRepository.findOneWithEagerRelationships(id).map(subCountyCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubCountyCode : {}", id);
        subCountyCodeRepository.deleteById(id);
        subCountyCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCountyCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SubCountyCodes for query {}", query);
        return subCountyCodeSearchRepository.search(query, pageable).map(subCountyCodeMapper::toDto);
    }
}
