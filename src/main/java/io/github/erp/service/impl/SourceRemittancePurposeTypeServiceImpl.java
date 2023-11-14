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
