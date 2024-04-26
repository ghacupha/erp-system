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

import io.github.erp.domain.InstitutionCode;
import io.github.erp.repository.InstitutionCodeRepository;
import io.github.erp.repository.search.InstitutionCodeSearchRepository;
import io.github.erp.service.InstitutionCodeService;
import io.github.erp.service.dto.InstitutionCodeDTO;
import io.github.erp.service.mapper.InstitutionCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InstitutionCode}.
 */
@Service
@Transactional
public class InstitutionCodeServiceImpl implements InstitutionCodeService {

    private final Logger log = LoggerFactory.getLogger(InstitutionCodeServiceImpl.class);

    private final InstitutionCodeRepository institutionCodeRepository;

    private final InstitutionCodeMapper institutionCodeMapper;

    private final InstitutionCodeSearchRepository institutionCodeSearchRepository;

    public InstitutionCodeServiceImpl(
        InstitutionCodeRepository institutionCodeRepository,
        InstitutionCodeMapper institutionCodeMapper,
        InstitutionCodeSearchRepository institutionCodeSearchRepository
    ) {
        this.institutionCodeRepository = institutionCodeRepository;
        this.institutionCodeMapper = institutionCodeMapper;
        this.institutionCodeSearchRepository = institutionCodeSearchRepository;
    }

    @Override
    public InstitutionCodeDTO save(InstitutionCodeDTO institutionCodeDTO) {
        log.debug("Request to save InstitutionCode : {}", institutionCodeDTO);
        InstitutionCode institutionCode = institutionCodeMapper.toEntity(institutionCodeDTO);
        institutionCode = institutionCodeRepository.save(institutionCode);
        InstitutionCodeDTO result = institutionCodeMapper.toDto(institutionCode);
        institutionCodeSearchRepository.save(institutionCode);
        return result;
    }

    @Override
    public Optional<InstitutionCodeDTO> partialUpdate(InstitutionCodeDTO institutionCodeDTO) {
        log.debug("Request to partially update InstitutionCode : {}", institutionCodeDTO);

        return institutionCodeRepository
            .findById(institutionCodeDTO.getId())
            .map(existingInstitutionCode -> {
                institutionCodeMapper.partialUpdate(existingInstitutionCode, institutionCodeDTO);

                return existingInstitutionCode;
            })
            .map(institutionCodeRepository::save)
            .map(savedInstitutionCode -> {
                institutionCodeSearchRepository.save(savedInstitutionCode);

                return savedInstitutionCode;
            })
            .map(institutionCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InstitutionCodes");
        return institutionCodeRepository.findAll(pageable).map(institutionCodeMapper::toDto);
    }

    public Page<InstitutionCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return institutionCodeRepository.findAllWithEagerRelationships(pageable).map(institutionCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstitutionCodeDTO> findOne(Long id) {
        log.debug("Request to get InstitutionCode : {}", id);
        return institutionCodeRepository.findOneWithEagerRelationships(id).map(institutionCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InstitutionCode : {}", id);
        institutionCodeRepository.deleteById(id);
        institutionCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InstitutionCodes for query {}", query);
        return institutionCodeSearchRepository.search(query, pageable).map(institutionCodeMapper::toDto);
    }
}
