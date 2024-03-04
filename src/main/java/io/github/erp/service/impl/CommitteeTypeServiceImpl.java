package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.domain.CommitteeType;
import io.github.erp.repository.CommitteeTypeRepository;
import io.github.erp.repository.search.CommitteeTypeSearchRepository;
import io.github.erp.service.CommitteeTypeService;
import io.github.erp.service.dto.CommitteeTypeDTO;
import io.github.erp.service.mapper.CommitteeTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommitteeType}.
 */
@Service
@Transactional
public class CommitteeTypeServiceImpl implements CommitteeTypeService {

    private final Logger log = LoggerFactory.getLogger(CommitteeTypeServiceImpl.class);

    private final CommitteeTypeRepository committeeTypeRepository;

    private final CommitteeTypeMapper committeeTypeMapper;

    private final CommitteeTypeSearchRepository committeeTypeSearchRepository;

    public CommitteeTypeServiceImpl(
        CommitteeTypeRepository committeeTypeRepository,
        CommitteeTypeMapper committeeTypeMapper,
        CommitteeTypeSearchRepository committeeTypeSearchRepository
    ) {
        this.committeeTypeRepository = committeeTypeRepository;
        this.committeeTypeMapper = committeeTypeMapper;
        this.committeeTypeSearchRepository = committeeTypeSearchRepository;
    }

    @Override
    public CommitteeTypeDTO save(CommitteeTypeDTO committeeTypeDTO) {
        log.debug("Request to save CommitteeType : {}", committeeTypeDTO);
        CommitteeType committeeType = committeeTypeMapper.toEntity(committeeTypeDTO);
        committeeType = committeeTypeRepository.save(committeeType);
        CommitteeTypeDTO result = committeeTypeMapper.toDto(committeeType);
        committeeTypeSearchRepository.save(committeeType);
        return result;
    }

    @Override
    public Optional<CommitteeTypeDTO> partialUpdate(CommitteeTypeDTO committeeTypeDTO) {
        log.debug("Request to partially update CommitteeType : {}", committeeTypeDTO);

        return committeeTypeRepository
            .findById(committeeTypeDTO.getId())
            .map(existingCommitteeType -> {
                committeeTypeMapper.partialUpdate(existingCommitteeType, committeeTypeDTO);

                return existingCommitteeType;
            })
            .map(committeeTypeRepository::save)
            .map(savedCommitteeType -> {
                committeeTypeSearchRepository.save(savedCommitteeType);

                return savedCommitteeType;
            })
            .map(committeeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommitteeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommitteeTypes");
        return committeeTypeRepository.findAll(pageable).map(committeeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommitteeTypeDTO> findOne(Long id) {
        log.debug("Request to get CommitteeType : {}", id);
        return committeeTypeRepository.findById(id).map(committeeTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommitteeType : {}", id);
        committeeTypeRepository.deleteById(id);
        committeeTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommitteeTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CommitteeTypes for query {}", query);
        return committeeTypeSearchRepository.search(query, pageable).map(committeeTypeMapper::toDto);
    }
}
