package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.domain.FraudType;
import io.github.erp.repository.FraudTypeRepository;
import io.github.erp.repository.search.FraudTypeSearchRepository;
import io.github.erp.service.FraudTypeService;
import io.github.erp.service.dto.FraudTypeDTO;
import io.github.erp.service.mapper.FraudTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FraudType}.
 */
@Service
@Transactional
public class FraudTypeServiceImpl implements FraudTypeService {

    private final Logger log = LoggerFactory.getLogger(FraudTypeServiceImpl.class);

    private final FraudTypeRepository fraudTypeRepository;

    private final FraudTypeMapper fraudTypeMapper;

    private final FraudTypeSearchRepository fraudTypeSearchRepository;

    public FraudTypeServiceImpl(
        FraudTypeRepository fraudTypeRepository,
        FraudTypeMapper fraudTypeMapper,
        FraudTypeSearchRepository fraudTypeSearchRepository
    ) {
        this.fraudTypeRepository = fraudTypeRepository;
        this.fraudTypeMapper = fraudTypeMapper;
        this.fraudTypeSearchRepository = fraudTypeSearchRepository;
    }

    @Override
    public FraudTypeDTO save(FraudTypeDTO fraudTypeDTO) {
        log.debug("Request to save FraudType : {}", fraudTypeDTO);
        FraudType fraudType = fraudTypeMapper.toEntity(fraudTypeDTO);
        fraudType = fraudTypeRepository.save(fraudType);
        FraudTypeDTO result = fraudTypeMapper.toDto(fraudType);
        fraudTypeSearchRepository.save(fraudType);
        return result;
    }

    @Override
    public Optional<FraudTypeDTO> partialUpdate(FraudTypeDTO fraudTypeDTO) {
        log.debug("Request to partially update FraudType : {}", fraudTypeDTO);

        return fraudTypeRepository
            .findById(fraudTypeDTO.getId())
            .map(existingFraudType -> {
                fraudTypeMapper.partialUpdate(existingFraudType, fraudTypeDTO);

                return existingFraudType;
            })
            .map(fraudTypeRepository::save)
            .map(savedFraudType -> {
                fraudTypeSearchRepository.save(savedFraudType);

                return savedFraudType;
            })
            .map(fraudTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FraudTypes");
        return fraudTypeRepository.findAll(pageable).map(fraudTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraudTypeDTO> findOne(Long id) {
        log.debug("Request to get FraudType : {}", id);
        return fraudTypeRepository.findById(id).map(fraudTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FraudType : {}", id);
        fraudTypeRepository.deleteById(id);
        fraudTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FraudTypes for query {}", query);
        return fraudTypeSearchRepository.search(query, pageable).map(fraudTypeMapper::toDto);
    }
}
