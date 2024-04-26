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

import io.github.erp.domain.OutletType;
import io.github.erp.repository.OutletTypeRepository;
import io.github.erp.repository.search.OutletTypeSearchRepository;
import io.github.erp.service.OutletTypeService;
import io.github.erp.service.dto.OutletTypeDTO;
import io.github.erp.service.mapper.OutletTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OutletType}.
 */
@Service
@Transactional
public class OutletTypeServiceImpl implements OutletTypeService {

    private final Logger log = LoggerFactory.getLogger(OutletTypeServiceImpl.class);

    private final OutletTypeRepository outletTypeRepository;

    private final OutletTypeMapper outletTypeMapper;

    private final OutletTypeSearchRepository outletTypeSearchRepository;

    public OutletTypeServiceImpl(
        OutletTypeRepository outletTypeRepository,
        OutletTypeMapper outletTypeMapper,
        OutletTypeSearchRepository outletTypeSearchRepository
    ) {
        this.outletTypeRepository = outletTypeRepository;
        this.outletTypeMapper = outletTypeMapper;
        this.outletTypeSearchRepository = outletTypeSearchRepository;
    }

    @Override
    public OutletTypeDTO save(OutletTypeDTO outletTypeDTO) {
        log.debug("Request to save OutletType : {}", outletTypeDTO);
        OutletType outletType = outletTypeMapper.toEntity(outletTypeDTO);
        outletType = outletTypeRepository.save(outletType);
        OutletTypeDTO result = outletTypeMapper.toDto(outletType);
        outletTypeSearchRepository.save(outletType);
        return result;
    }

    @Override
    public Optional<OutletTypeDTO> partialUpdate(OutletTypeDTO outletTypeDTO) {
        log.debug("Request to partially update OutletType : {}", outletTypeDTO);

        return outletTypeRepository
            .findById(outletTypeDTO.getId())
            .map(existingOutletType -> {
                outletTypeMapper.partialUpdate(existingOutletType, outletTypeDTO);

                return existingOutletType;
            })
            .map(outletTypeRepository::save)
            .map(savedOutletType -> {
                outletTypeSearchRepository.save(savedOutletType);

                return savedOutletType;
            })
            .map(outletTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutletTypes");
        return outletTypeRepository.findAll(pageable).map(outletTypeMapper::toDto);
    }

    public Page<OutletTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return outletTypeRepository.findAllWithEagerRelationships(pageable).map(outletTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutletTypeDTO> findOne(Long id) {
        log.debug("Request to get OutletType : {}", id);
        return outletTypeRepository.findOneWithEagerRelationships(id).map(outletTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutletType : {}", id);
        outletTypeRepository.deleteById(id);
        outletTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OutletTypes for query {}", query);
        return outletTypeSearchRepository.search(query, pageable).map(outletTypeMapper::toDto);
    }
}
