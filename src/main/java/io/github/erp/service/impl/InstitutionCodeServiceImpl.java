package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
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
