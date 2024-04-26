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

import io.github.erp.domain.SourceRemittancePurposeType;
import io.github.erp.repository.SourceRemittancePurposeTypeRepository;
import io.github.erp.repository.search.SourceRemittancePurposeTypeSearchRepository;
import io.github.erp.service.SourceRemittancePurposeTypeService;
import io.github.erp.service.dto.SourceRemittancePurposeTypeDTO;
import io.github.erp.service.mapper.SourceRemittancePurposeTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SourceRemittancePurposeType}.
 */
@Service
@Transactional
public class SourceRemittancePurposeTypeServiceImpl implements SourceRemittancePurposeTypeService {

    private final Logger log = LoggerFactory.getLogger(SourceRemittancePurposeTypeServiceImpl.class);

    private final SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository;

    private final SourceRemittancePurposeTypeMapper sourceRemittancePurposeTypeMapper;

    private final SourceRemittancePurposeTypeSearchRepository sourceRemittancePurposeTypeSearchRepository;

    public SourceRemittancePurposeTypeServiceImpl(
        SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository,
        SourceRemittancePurposeTypeMapper sourceRemittancePurposeTypeMapper,
        SourceRemittancePurposeTypeSearchRepository sourceRemittancePurposeTypeSearchRepository
    ) {
        this.sourceRemittancePurposeTypeRepository = sourceRemittancePurposeTypeRepository;
        this.sourceRemittancePurposeTypeMapper = sourceRemittancePurposeTypeMapper;
        this.sourceRemittancePurposeTypeSearchRepository = sourceRemittancePurposeTypeSearchRepository;
    }

    @Override
    public SourceRemittancePurposeTypeDTO save(SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO) {
        log.debug("Request to save SourceRemittancePurposeType : {}", sourceRemittancePurposeTypeDTO);
        SourceRemittancePurposeType sourceRemittancePurposeType = sourceRemittancePurposeTypeMapper.toEntity(
            sourceRemittancePurposeTypeDTO
        );
        sourceRemittancePurposeType = sourceRemittancePurposeTypeRepository.save(sourceRemittancePurposeType);
        SourceRemittancePurposeTypeDTO result = sourceRemittancePurposeTypeMapper.toDto(sourceRemittancePurposeType);
        sourceRemittancePurposeTypeSearchRepository.save(sourceRemittancePurposeType);
        return result;
    }

    @Override
    public Optional<SourceRemittancePurposeTypeDTO> partialUpdate(SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO) {
        log.debug("Request to partially update SourceRemittancePurposeType : {}", sourceRemittancePurposeTypeDTO);

        return sourceRemittancePurposeTypeRepository
            .findById(sourceRemittancePurposeTypeDTO.getId())
            .map(existingSourceRemittancePurposeType -> {
                sourceRemittancePurposeTypeMapper.partialUpdate(existingSourceRemittancePurposeType, sourceRemittancePurposeTypeDTO);

                return existingSourceRemittancePurposeType;
            })
            .map(sourceRemittancePurposeTypeRepository::save)
            .map(savedSourceRemittancePurposeType -> {
                sourceRemittancePurposeTypeSearchRepository.save(savedSourceRemittancePurposeType);

                return savedSourceRemittancePurposeType;
            })
            .map(sourceRemittancePurposeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourceRemittancePurposeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SourceRemittancePurposeTypes");
        return sourceRemittancePurposeTypeRepository.findAll(pageable).map(sourceRemittancePurposeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourceRemittancePurposeTypeDTO> findOne(Long id) {
        log.debug("Request to get SourceRemittancePurposeType : {}", id);
        return sourceRemittancePurposeTypeRepository.findById(id).map(sourceRemittancePurposeTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SourceRemittancePurposeType : {}", id);
        sourceRemittancePurposeTypeRepository.deleteById(id);
        sourceRemittancePurposeTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourceRemittancePurposeTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SourceRemittancePurposeTypes for query {}", query);
        return sourceRemittancePurposeTypeSearchRepository.search(query, pageable).map(sourceRemittancePurposeTypeMapper::toDto);
    }
}
