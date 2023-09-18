package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.domain.FxTransactionChannelType;
import io.github.erp.repository.FxTransactionChannelTypeRepository;
import io.github.erp.repository.search.FxTransactionChannelTypeSearchRepository;
import io.github.erp.service.FxTransactionChannelTypeService;
import io.github.erp.service.dto.FxTransactionChannelTypeDTO;
import io.github.erp.service.mapper.FxTransactionChannelTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxTransactionChannelType}.
 */
@Service
@Transactional
public class FxTransactionChannelTypeServiceImpl implements FxTransactionChannelTypeService {

    private final Logger log = LoggerFactory.getLogger(FxTransactionChannelTypeServiceImpl.class);

    private final FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository;

    private final FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper;

    private final FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository;

    public FxTransactionChannelTypeServiceImpl(
        FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository,
        FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper,
        FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository
    ) {
        this.fxTransactionChannelTypeRepository = fxTransactionChannelTypeRepository;
        this.fxTransactionChannelTypeMapper = fxTransactionChannelTypeMapper;
        this.fxTransactionChannelTypeSearchRepository = fxTransactionChannelTypeSearchRepository;
    }

    @Override
    public FxTransactionChannelTypeDTO save(FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO) {
        log.debug("Request to save FxTransactionChannelType : {}", fxTransactionChannelTypeDTO);
        FxTransactionChannelType fxTransactionChannelType = fxTransactionChannelTypeMapper.toEntity(fxTransactionChannelTypeDTO);
        fxTransactionChannelType = fxTransactionChannelTypeRepository.save(fxTransactionChannelType);
        FxTransactionChannelTypeDTO result = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);
        fxTransactionChannelTypeSearchRepository.save(fxTransactionChannelType);
        return result;
    }

    @Override
    public Optional<FxTransactionChannelTypeDTO> partialUpdate(FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO) {
        log.debug("Request to partially update FxTransactionChannelType : {}", fxTransactionChannelTypeDTO);

        return fxTransactionChannelTypeRepository
            .findById(fxTransactionChannelTypeDTO.getId())
            .map(existingFxTransactionChannelType -> {
                fxTransactionChannelTypeMapper.partialUpdate(existingFxTransactionChannelType, fxTransactionChannelTypeDTO);

                return existingFxTransactionChannelType;
            })
            .map(fxTransactionChannelTypeRepository::save)
            .map(savedFxTransactionChannelType -> {
                fxTransactionChannelTypeSearchRepository.save(savedFxTransactionChannelType);

                return savedFxTransactionChannelType;
            })
            .map(fxTransactionChannelTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionChannelTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxTransactionChannelTypes");
        return fxTransactionChannelTypeRepository.findAll(pageable).map(fxTransactionChannelTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxTransactionChannelTypeDTO> findOne(Long id) {
        log.debug("Request to get FxTransactionChannelType : {}", id);
        return fxTransactionChannelTypeRepository.findById(id).map(fxTransactionChannelTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxTransactionChannelType : {}", id);
        fxTransactionChannelTypeRepository.deleteById(id);
        fxTransactionChannelTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionChannelTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxTransactionChannelTypes for query {}", query);
        return fxTransactionChannelTypeSearchRepository.search(query, pageable).map(fxTransactionChannelTypeMapper::toDto);
    }
}
