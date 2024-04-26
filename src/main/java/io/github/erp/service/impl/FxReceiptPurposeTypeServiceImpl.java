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

import io.github.erp.domain.FxReceiptPurposeType;
import io.github.erp.repository.FxReceiptPurposeTypeRepository;
import io.github.erp.repository.search.FxReceiptPurposeTypeSearchRepository;
import io.github.erp.service.FxReceiptPurposeTypeService;
import io.github.erp.service.dto.FxReceiptPurposeTypeDTO;
import io.github.erp.service.mapper.FxReceiptPurposeTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxReceiptPurposeType}.
 */
@Service
@Transactional
public class FxReceiptPurposeTypeServiceImpl implements FxReceiptPurposeTypeService {

    private final Logger log = LoggerFactory.getLogger(FxReceiptPurposeTypeServiceImpl.class);

    private final FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository;

    private final FxReceiptPurposeTypeMapper fxReceiptPurposeTypeMapper;

    private final FxReceiptPurposeTypeSearchRepository fxReceiptPurposeTypeSearchRepository;

    public FxReceiptPurposeTypeServiceImpl(
        FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository,
        FxReceiptPurposeTypeMapper fxReceiptPurposeTypeMapper,
        FxReceiptPurposeTypeSearchRepository fxReceiptPurposeTypeSearchRepository
    ) {
        this.fxReceiptPurposeTypeRepository = fxReceiptPurposeTypeRepository;
        this.fxReceiptPurposeTypeMapper = fxReceiptPurposeTypeMapper;
        this.fxReceiptPurposeTypeSearchRepository = fxReceiptPurposeTypeSearchRepository;
    }

    @Override
    public FxReceiptPurposeTypeDTO save(FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO) {
        log.debug("Request to save FxReceiptPurposeType : {}", fxReceiptPurposeTypeDTO);
        FxReceiptPurposeType fxReceiptPurposeType = fxReceiptPurposeTypeMapper.toEntity(fxReceiptPurposeTypeDTO);
        fxReceiptPurposeType = fxReceiptPurposeTypeRepository.save(fxReceiptPurposeType);
        FxReceiptPurposeTypeDTO result = fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeType);
        fxReceiptPurposeTypeSearchRepository.save(fxReceiptPurposeType);
        return result;
    }

    @Override
    public Optional<FxReceiptPurposeTypeDTO> partialUpdate(FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO) {
        log.debug("Request to partially update FxReceiptPurposeType : {}", fxReceiptPurposeTypeDTO);

        return fxReceiptPurposeTypeRepository
            .findById(fxReceiptPurposeTypeDTO.getId())
            .map(existingFxReceiptPurposeType -> {
                fxReceiptPurposeTypeMapper.partialUpdate(existingFxReceiptPurposeType, fxReceiptPurposeTypeDTO);

                return existingFxReceiptPurposeType;
            })
            .map(fxReceiptPurposeTypeRepository::save)
            .map(savedFxReceiptPurposeType -> {
                fxReceiptPurposeTypeSearchRepository.save(savedFxReceiptPurposeType);

                return savedFxReceiptPurposeType;
            })
            .map(fxReceiptPurposeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxReceiptPurposeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxReceiptPurposeTypes");
        return fxReceiptPurposeTypeRepository.findAll(pageable).map(fxReceiptPurposeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxReceiptPurposeTypeDTO> findOne(Long id) {
        log.debug("Request to get FxReceiptPurposeType : {}", id);
        return fxReceiptPurposeTypeRepository.findById(id).map(fxReceiptPurposeTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxReceiptPurposeType : {}", id);
        fxReceiptPurposeTypeRepository.deleteById(id);
        fxReceiptPurposeTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxReceiptPurposeTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxReceiptPurposeTypes for query {}", query);
        return fxReceiptPurposeTypeSearchRepository.search(query, pageable).map(fxReceiptPurposeTypeMapper::toDto);
    }
}
