package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

import io.github.erp.domain.InstitutionStatusType;
import io.github.erp.repository.InstitutionStatusTypeRepository;
import io.github.erp.repository.search.InstitutionStatusTypeSearchRepository;
import io.github.erp.service.InstitutionStatusTypeService;
import io.github.erp.service.dto.InstitutionStatusTypeDTO;
import io.github.erp.service.mapper.InstitutionStatusTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InstitutionStatusType}.
 */
@Service
@Transactional
public class InstitutionStatusTypeServiceImpl implements InstitutionStatusTypeService {

    private final Logger log = LoggerFactory.getLogger(InstitutionStatusTypeServiceImpl.class);

    private final InstitutionStatusTypeRepository institutionStatusTypeRepository;

    private final InstitutionStatusTypeMapper institutionStatusTypeMapper;

    private final InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository;

    public InstitutionStatusTypeServiceImpl(
        InstitutionStatusTypeRepository institutionStatusTypeRepository,
        InstitutionStatusTypeMapper institutionStatusTypeMapper,
        InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository
    ) {
        this.institutionStatusTypeRepository = institutionStatusTypeRepository;
        this.institutionStatusTypeMapper = institutionStatusTypeMapper;
        this.institutionStatusTypeSearchRepository = institutionStatusTypeSearchRepository;
    }

    @Override
    public InstitutionStatusTypeDTO save(InstitutionStatusTypeDTO institutionStatusTypeDTO) {
        log.debug("Request to save InstitutionStatusType : {}", institutionStatusTypeDTO);
        InstitutionStatusType institutionStatusType = institutionStatusTypeMapper.toEntity(institutionStatusTypeDTO);
        institutionStatusType = institutionStatusTypeRepository.save(institutionStatusType);
        InstitutionStatusTypeDTO result = institutionStatusTypeMapper.toDto(institutionStatusType);
        institutionStatusTypeSearchRepository.save(institutionStatusType);
        return result;
    }

    @Override
    public Optional<InstitutionStatusTypeDTO> partialUpdate(InstitutionStatusTypeDTO institutionStatusTypeDTO) {
        log.debug("Request to partially update InstitutionStatusType : {}", institutionStatusTypeDTO);

        return institutionStatusTypeRepository
            .findById(institutionStatusTypeDTO.getId())
            .map(existingInstitutionStatusType -> {
                institutionStatusTypeMapper.partialUpdate(existingInstitutionStatusType, institutionStatusTypeDTO);

                return existingInstitutionStatusType;
            })
            .map(institutionStatusTypeRepository::save)
            .map(savedInstitutionStatusType -> {
                institutionStatusTypeSearchRepository.save(savedInstitutionStatusType);

                return savedInstitutionStatusType;
            })
            .map(institutionStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionStatusTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InstitutionStatusTypes");
        return institutionStatusTypeRepository.findAll(pageable).map(institutionStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstitutionStatusTypeDTO> findOne(Long id) {
        log.debug("Request to get InstitutionStatusType : {}", id);
        return institutionStatusTypeRepository.findById(id).map(institutionStatusTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InstitutionStatusType : {}", id);
        institutionStatusTypeRepository.deleteById(id);
        institutionStatusTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionStatusTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InstitutionStatusTypes for query {}", query);
        return institutionStatusTypeSearchRepository.search(query, pageable).map(institutionStatusTypeMapper::toDto);
    }
}
