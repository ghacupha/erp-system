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

import io.github.erp.domain.GenderType;
import io.github.erp.repository.GenderTypeRepository;
import io.github.erp.repository.search.GenderTypeSearchRepository;
import io.github.erp.service.GenderTypeService;
import io.github.erp.service.dto.GenderTypeDTO;
import io.github.erp.service.mapper.GenderTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GenderType}.
 */
@Service
@Transactional
public class GenderTypeServiceImpl implements GenderTypeService {

    private final Logger log = LoggerFactory.getLogger(GenderTypeServiceImpl.class);

    private final GenderTypeRepository genderTypeRepository;

    private final GenderTypeMapper genderTypeMapper;

    private final GenderTypeSearchRepository genderTypeSearchRepository;

    public GenderTypeServiceImpl(
        GenderTypeRepository genderTypeRepository,
        GenderTypeMapper genderTypeMapper,
        GenderTypeSearchRepository genderTypeSearchRepository
    ) {
        this.genderTypeRepository = genderTypeRepository;
        this.genderTypeMapper = genderTypeMapper;
        this.genderTypeSearchRepository = genderTypeSearchRepository;
    }

    @Override
    public GenderTypeDTO save(GenderTypeDTO genderTypeDTO) {
        log.debug("Request to save GenderType : {}", genderTypeDTO);
        GenderType genderType = genderTypeMapper.toEntity(genderTypeDTO);
        genderType = genderTypeRepository.save(genderType);
        GenderTypeDTO result = genderTypeMapper.toDto(genderType);
        genderTypeSearchRepository.save(genderType);
        return result;
    }

    @Override
    public Optional<GenderTypeDTO> partialUpdate(GenderTypeDTO genderTypeDTO) {
        log.debug("Request to partially update GenderType : {}", genderTypeDTO);

        return genderTypeRepository
            .findById(genderTypeDTO.getId())
            .map(existingGenderType -> {
                genderTypeMapper.partialUpdate(existingGenderType, genderTypeDTO);

                return existingGenderType;
            })
            .map(genderTypeRepository::save)
            .map(savedGenderType -> {
                genderTypeSearchRepository.save(savedGenderType);

                return savedGenderType;
            })
            .map(genderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenderTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GenderTypes");
        return genderTypeRepository.findAll(pageable).map(genderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenderTypeDTO> findOne(Long id) {
        log.debug("Request to get GenderType : {}", id);
        return genderTypeRepository.findById(id).map(genderTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GenderType : {}", id);
        genderTypeRepository.deleteById(id);
        genderTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenderTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GenderTypes for query {}", query);
        return genderTypeSearchRepository.search(query, pageable).map(genderTypeMapper::toDto);
    }
}
