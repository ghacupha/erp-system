package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.CrbRecordFileType;
import io.github.erp.repository.CrbRecordFileTypeRepository;
import io.github.erp.repository.search.CrbRecordFileTypeSearchRepository;
import io.github.erp.service.CrbRecordFileTypeService;
import io.github.erp.service.dto.CrbRecordFileTypeDTO;
import io.github.erp.service.mapper.CrbRecordFileTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbRecordFileType}.
 */
@Service
@Transactional
public class CrbRecordFileTypeServiceImpl implements CrbRecordFileTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbRecordFileTypeServiceImpl.class);

    private final CrbRecordFileTypeRepository crbRecordFileTypeRepository;

    private final CrbRecordFileTypeMapper crbRecordFileTypeMapper;

    private final CrbRecordFileTypeSearchRepository crbRecordFileTypeSearchRepository;

    public CrbRecordFileTypeServiceImpl(
        CrbRecordFileTypeRepository crbRecordFileTypeRepository,
        CrbRecordFileTypeMapper crbRecordFileTypeMapper,
        CrbRecordFileTypeSearchRepository crbRecordFileTypeSearchRepository
    ) {
        this.crbRecordFileTypeRepository = crbRecordFileTypeRepository;
        this.crbRecordFileTypeMapper = crbRecordFileTypeMapper;
        this.crbRecordFileTypeSearchRepository = crbRecordFileTypeSearchRepository;
    }

    @Override
    public CrbRecordFileTypeDTO save(CrbRecordFileTypeDTO crbRecordFileTypeDTO) {
        log.debug("Request to save CrbRecordFileType : {}", crbRecordFileTypeDTO);
        CrbRecordFileType crbRecordFileType = crbRecordFileTypeMapper.toEntity(crbRecordFileTypeDTO);
        crbRecordFileType = crbRecordFileTypeRepository.save(crbRecordFileType);
        CrbRecordFileTypeDTO result = crbRecordFileTypeMapper.toDto(crbRecordFileType);
        crbRecordFileTypeSearchRepository.save(crbRecordFileType);
        return result;
    }

    @Override
    public Optional<CrbRecordFileTypeDTO> partialUpdate(CrbRecordFileTypeDTO crbRecordFileTypeDTO) {
        log.debug("Request to partially update CrbRecordFileType : {}", crbRecordFileTypeDTO);

        return crbRecordFileTypeRepository
            .findById(crbRecordFileTypeDTO.getId())
            .map(existingCrbRecordFileType -> {
                crbRecordFileTypeMapper.partialUpdate(existingCrbRecordFileType, crbRecordFileTypeDTO);

                return existingCrbRecordFileType;
            })
            .map(crbRecordFileTypeRepository::save)
            .map(savedCrbRecordFileType -> {
                crbRecordFileTypeSearchRepository.save(savedCrbRecordFileType);

                return savedCrbRecordFileType;
            })
            .map(crbRecordFileTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbRecordFileTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbRecordFileTypes");
        return crbRecordFileTypeRepository.findAll(pageable).map(crbRecordFileTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbRecordFileTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbRecordFileType : {}", id);
        return crbRecordFileTypeRepository.findById(id).map(crbRecordFileTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbRecordFileType : {}", id);
        crbRecordFileTypeRepository.deleteById(id);
        crbRecordFileTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbRecordFileTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbRecordFileTypes for query {}", query);
        return crbRecordFileTypeSearchRepository.search(query, pageable).map(crbRecordFileTypeMapper::toDto);
    }
}
