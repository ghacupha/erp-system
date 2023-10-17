package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

import io.github.erp.domain.PartyRelationType;
import io.github.erp.repository.PartyRelationTypeRepository;
import io.github.erp.repository.search.PartyRelationTypeSearchRepository;
import io.github.erp.service.PartyRelationTypeService;
import io.github.erp.service.dto.PartyRelationTypeDTO;
import io.github.erp.service.mapper.PartyRelationTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PartyRelationType}.
 */
@Service
@Transactional
public class PartyRelationTypeServiceImpl implements PartyRelationTypeService {

    private final Logger log = LoggerFactory.getLogger(PartyRelationTypeServiceImpl.class);

    private final PartyRelationTypeRepository partyRelationTypeRepository;

    private final PartyRelationTypeMapper partyRelationTypeMapper;

    private final PartyRelationTypeSearchRepository partyRelationTypeSearchRepository;

    public PartyRelationTypeServiceImpl(
        PartyRelationTypeRepository partyRelationTypeRepository,
        PartyRelationTypeMapper partyRelationTypeMapper,
        PartyRelationTypeSearchRepository partyRelationTypeSearchRepository
    ) {
        this.partyRelationTypeRepository = partyRelationTypeRepository;
        this.partyRelationTypeMapper = partyRelationTypeMapper;
        this.partyRelationTypeSearchRepository = partyRelationTypeSearchRepository;
    }

    @Override
    public PartyRelationTypeDTO save(PartyRelationTypeDTO partyRelationTypeDTO) {
        log.debug("Request to save PartyRelationType : {}", partyRelationTypeDTO);
        PartyRelationType partyRelationType = partyRelationTypeMapper.toEntity(partyRelationTypeDTO);
        partyRelationType = partyRelationTypeRepository.save(partyRelationType);
        PartyRelationTypeDTO result = partyRelationTypeMapper.toDto(partyRelationType);
        partyRelationTypeSearchRepository.save(partyRelationType);
        return result;
    }

    @Override
    public Optional<PartyRelationTypeDTO> partialUpdate(PartyRelationTypeDTO partyRelationTypeDTO) {
        log.debug("Request to partially update PartyRelationType : {}", partyRelationTypeDTO);

        return partyRelationTypeRepository
            .findById(partyRelationTypeDTO.getId())
            .map(existingPartyRelationType -> {
                partyRelationTypeMapper.partialUpdate(existingPartyRelationType, partyRelationTypeDTO);

                return existingPartyRelationType;
            })
            .map(partyRelationTypeRepository::save)
            .map(savedPartyRelationType -> {
                partyRelationTypeSearchRepository.save(savedPartyRelationType);

                return savedPartyRelationType;
            })
            .map(partyRelationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartyRelationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PartyRelationTypes");
        return partyRelationTypeRepository.findAll(pageable).map(partyRelationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartyRelationTypeDTO> findOne(Long id) {
        log.debug("Request to get PartyRelationType : {}", id);
        return partyRelationTypeRepository.findById(id).map(partyRelationTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PartyRelationType : {}", id);
        partyRelationTypeRepository.deleteById(id);
        partyRelationTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartyRelationTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PartyRelationTypes for query {}", query);
        return partyRelationTypeSearchRepository.search(query, pageable).map(partyRelationTypeMapper::toDto);
    }
}
