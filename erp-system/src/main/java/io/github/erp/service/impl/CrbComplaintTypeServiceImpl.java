package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.CrbComplaintType;
import io.github.erp.repository.CrbComplaintTypeRepository;
import io.github.erp.repository.search.CrbComplaintTypeSearchRepository;
import io.github.erp.service.CrbComplaintTypeService;
import io.github.erp.service.dto.CrbComplaintTypeDTO;
import io.github.erp.service.mapper.CrbComplaintTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbComplaintType}.
 */
@Service
@Transactional
public class CrbComplaintTypeServiceImpl implements CrbComplaintTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbComplaintTypeServiceImpl.class);

    private final CrbComplaintTypeRepository crbComplaintTypeRepository;

    private final CrbComplaintTypeMapper crbComplaintTypeMapper;

    private final CrbComplaintTypeSearchRepository crbComplaintTypeSearchRepository;

    public CrbComplaintTypeServiceImpl(
        CrbComplaintTypeRepository crbComplaintTypeRepository,
        CrbComplaintTypeMapper crbComplaintTypeMapper,
        CrbComplaintTypeSearchRepository crbComplaintTypeSearchRepository
    ) {
        this.crbComplaintTypeRepository = crbComplaintTypeRepository;
        this.crbComplaintTypeMapper = crbComplaintTypeMapper;
        this.crbComplaintTypeSearchRepository = crbComplaintTypeSearchRepository;
    }

    @Override
    public CrbComplaintTypeDTO save(CrbComplaintTypeDTO crbComplaintTypeDTO) {
        log.debug("Request to save CrbComplaintType : {}", crbComplaintTypeDTO);
        CrbComplaintType crbComplaintType = crbComplaintTypeMapper.toEntity(crbComplaintTypeDTO);
        crbComplaintType = crbComplaintTypeRepository.save(crbComplaintType);
        CrbComplaintTypeDTO result = crbComplaintTypeMapper.toDto(crbComplaintType);
        crbComplaintTypeSearchRepository.save(crbComplaintType);
        return result;
    }

    @Override
    public Optional<CrbComplaintTypeDTO> partialUpdate(CrbComplaintTypeDTO crbComplaintTypeDTO) {
        log.debug("Request to partially update CrbComplaintType : {}", crbComplaintTypeDTO);

        return crbComplaintTypeRepository
            .findById(crbComplaintTypeDTO.getId())
            .map(existingCrbComplaintType -> {
                crbComplaintTypeMapper.partialUpdate(existingCrbComplaintType, crbComplaintTypeDTO);

                return existingCrbComplaintType;
            })
            .map(crbComplaintTypeRepository::save)
            .map(savedCrbComplaintType -> {
                crbComplaintTypeSearchRepository.save(savedCrbComplaintType);

                return savedCrbComplaintType;
            })
            .map(crbComplaintTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbComplaintTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbComplaintTypes");
        return crbComplaintTypeRepository.findAll(pageable).map(crbComplaintTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbComplaintTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbComplaintType : {}", id);
        return crbComplaintTypeRepository.findById(id).map(crbComplaintTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbComplaintType : {}", id);
        crbComplaintTypeRepository.deleteById(id);
        crbComplaintTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbComplaintTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbComplaintTypes for query {}", query);
        return crbComplaintTypeSearchRepository.search(query, pageable).map(crbComplaintTypeMapper::toDto);
    }
}
