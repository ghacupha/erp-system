package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import io.github.erp.domain.CrbAccountHolderType;
import io.github.erp.repository.CrbAccountHolderTypeRepository;
import io.github.erp.repository.search.CrbAccountHolderTypeSearchRepository;
import io.github.erp.service.CrbAccountHolderTypeService;
import io.github.erp.service.dto.CrbAccountHolderTypeDTO;
import io.github.erp.service.mapper.CrbAccountHolderTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAccountHolderType}.
 */
@Service
@Transactional
public class CrbAccountHolderTypeServiceImpl implements CrbAccountHolderTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbAccountHolderTypeServiceImpl.class);

    private final CrbAccountHolderTypeRepository crbAccountHolderTypeRepository;

    private final CrbAccountHolderTypeMapper crbAccountHolderTypeMapper;

    private final CrbAccountHolderTypeSearchRepository crbAccountHolderTypeSearchRepository;

    public CrbAccountHolderTypeServiceImpl(
        CrbAccountHolderTypeRepository crbAccountHolderTypeRepository,
        CrbAccountHolderTypeMapper crbAccountHolderTypeMapper,
        CrbAccountHolderTypeSearchRepository crbAccountHolderTypeSearchRepository
    ) {
        this.crbAccountHolderTypeRepository = crbAccountHolderTypeRepository;
        this.crbAccountHolderTypeMapper = crbAccountHolderTypeMapper;
        this.crbAccountHolderTypeSearchRepository = crbAccountHolderTypeSearchRepository;
    }

    @Override
    public CrbAccountHolderTypeDTO save(CrbAccountHolderTypeDTO crbAccountHolderTypeDTO) {
        log.debug("Request to save CrbAccountHolderType : {}", crbAccountHolderTypeDTO);
        CrbAccountHolderType crbAccountHolderType = crbAccountHolderTypeMapper.toEntity(crbAccountHolderTypeDTO);
        crbAccountHolderType = crbAccountHolderTypeRepository.save(crbAccountHolderType);
        CrbAccountHolderTypeDTO result = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);
        crbAccountHolderTypeSearchRepository.save(crbAccountHolderType);
        return result;
    }

    @Override
    public Optional<CrbAccountHolderTypeDTO> partialUpdate(CrbAccountHolderTypeDTO crbAccountHolderTypeDTO) {
        log.debug("Request to partially update CrbAccountHolderType : {}", crbAccountHolderTypeDTO);

        return crbAccountHolderTypeRepository
            .findById(crbAccountHolderTypeDTO.getId())
            .map(existingCrbAccountHolderType -> {
                crbAccountHolderTypeMapper.partialUpdate(existingCrbAccountHolderType, crbAccountHolderTypeDTO);

                return existingCrbAccountHolderType;
            })
            .map(crbAccountHolderTypeRepository::save)
            .map(savedCrbAccountHolderType -> {
                crbAccountHolderTypeSearchRepository.save(savedCrbAccountHolderType);

                return savedCrbAccountHolderType;
            })
            .map(crbAccountHolderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAccountHolderTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAccountHolderTypes");
        return crbAccountHolderTypeRepository.findAll(pageable).map(crbAccountHolderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAccountHolderTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbAccountHolderType : {}", id);
        return crbAccountHolderTypeRepository.findById(id).map(crbAccountHolderTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAccountHolderType : {}", id);
        crbAccountHolderTypeRepository.deleteById(id);
        crbAccountHolderTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAccountHolderTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAccountHolderTypes for query {}", query);
        return crbAccountHolderTypeSearchRepository.search(query, pageable).map(crbAccountHolderTypeMapper::toDto);
    }
}
