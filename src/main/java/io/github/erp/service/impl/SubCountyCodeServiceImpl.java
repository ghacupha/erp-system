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
