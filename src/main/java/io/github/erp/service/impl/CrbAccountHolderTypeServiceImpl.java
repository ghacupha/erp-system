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
