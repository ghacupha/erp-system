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

import io.github.erp.domain.CrbComplaintStatusType;
import io.github.erp.repository.CrbComplaintStatusTypeRepository;
import io.github.erp.repository.search.CrbComplaintStatusTypeSearchRepository;
import io.github.erp.service.CrbComplaintStatusTypeService;
import io.github.erp.service.dto.CrbComplaintStatusTypeDTO;
import io.github.erp.service.mapper.CrbComplaintStatusTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbComplaintStatusType}.
 */
@Service
@Transactional
public class CrbComplaintStatusTypeServiceImpl implements CrbComplaintStatusTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbComplaintStatusTypeServiceImpl.class);

    private final CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository;

    private final CrbComplaintStatusTypeMapper crbComplaintStatusTypeMapper;

    private final CrbComplaintStatusTypeSearchRepository crbComplaintStatusTypeSearchRepository;

    public CrbComplaintStatusTypeServiceImpl(
        CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository,
        CrbComplaintStatusTypeMapper crbComplaintStatusTypeMapper,
        CrbComplaintStatusTypeSearchRepository crbComplaintStatusTypeSearchRepository
    ) {
        this.crbComplaintStatusTypeRepository = crbComplaintStatusTypeRepository;
        this.crbComplaintStatusTypeMapper = crbComplaintStatusTypeMapper;
        this.crbComplaintStatusTypeSearchRepository = crbComplaintStatusTypeSearchRepository;
    }

    @Override
    public CrbComplaintStatusTypeDTO save(CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO) {
        log.debug("Request to save CrbComplaintStatusType : {}", crbComplaintStatusTypeDTO);
        CrbComplaintStatusType crbComplaintStatusType = crbComplaintStatusTypeMapper.toEntity(crbComplaintStatusTypeDTO);
        crbComplaintStatusType = crbComplaintStatusTypeRepository.save(crbComplaintStatusType);
        CrbComplaintStatusTypeDTO result = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);
        crbComplaintStatusTypeSearchRepository.save(crbComplaintStatusType);
        return result;
    }

    @Override
    public Optional<CrbComplaintStatusTypeDTO> partialUpdate(CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO) {
        log.debug("Request to partially update CrbComplaintStatusType : {}", crbComplaintStatusTypeDTO);

        return crbComplaintStatusTypeRepository
            .findById(crbComplaintStatusTypeDTO.getId())
            .map(existingCrbComplaintStatusType -> {
                crbComplaintStatusTypeMapper.partialUpdate(existingCrbComplaintStatusType, crbComplaintStatusTypeDTO);

                return existingCrbComplaintStatusType;
            })
            .map(crbComplaintStatusTypeRepository::save)
            .map(savedCrbComplaintStatusType -> {
                crbComplaintStatusTypeSearchRepository.save(savedCrbComplaintStatusType);

                return savedCrbComplaintStatusType;
            })
            .map(crbComplaintStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbComplaintStatusTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbComplaintStatusTypes");
        return crbComplaintStatusTypeRepository.findAll(pageable).map(crbComplaintStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbComplaintStatusTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbComplaintStatusType : {}", id);
        return crbComplaintStatusTypeRepository.findById(id).map(crbComplaintStatusTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbComplaintStatusType : {}", id);
        crbComplaintStatusTypeRepository.deleteById(id);
        crbComplaintStatusTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbComplaintStatusTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbComplaintStatusTypes for query {}", query);
        return crbComplaintStatusTypeSearchRepository.search(query, pageable).map(crbComplaintStatusTypeMapper::toDto);
    }
}
