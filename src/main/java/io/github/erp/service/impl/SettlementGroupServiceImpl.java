package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.SettlementGroup;
import io.github.erp.repository.SettlementGroupRepository;
import io.github.erp.repository.search.SettlementGroupSearchRepository;
import io.github.erp.service.SettlementGroupService;
import io.github.erp.service.dto.SettlementGroupDTO;
import io.github.erp.service.mapper.SettlementGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SettlementGroup}.
 */
@Service
@Transactional
public class SettlementGroupServiceImpl implements SettlementGroupService {

    private final Logger log = LoggerFactory.getLogger(SettlementGroupServiceImpl.class);

    private final SettlementGroupRepository settlementGroupRepository;

    private final SettlementGroupMapper settlementGroupMapper;

    private final SettlementGroupSearchRepository settlementGroupSearchRepository;

    public SettlementGroupServiceImpl(
        SettlementGroupRepository settlementGroupRepository,
        SettlementGroupMapper settlementGroupMapper,
        SettlementGroupSearchRepository settlementGroupSearchRepository
    ) {
        this.settlementGroupRepository = settlementGroupRepository;
        this.settlementGroupMapper = settlementGroupMapper;
        this.settlementGroupSearchRepository = settlementGroupSearchRepository;
    }

    @Override
    public SettlementGroupDTO save(SettlementGroupDTO settlementGroupDTO) {
        log.debug("Request to save SettlementGroup : {}", settlementGroupDTO);
        validateCircularReference(settlementGroupDTO);
        SettlementGroup settlementGroup = settlementGroupMapper.toEntity(settlementGroupDTO);
        settlementGroup = settlementGroupRepository.save(settlementGroup);
        SettlementGroupDTO result = settlementGroupMapper.toDto(settlementGroup);
        settlementGroupSearchRepository.save(settlementGroup);
        return result;
    }

    @Override
    public SettlementGroupDTO update(SettlementGroupDTO settlementGroupDTO) {
        log.debug("Request to update SettlementGroup : {}", settlementGroupDTO);
        validateCircularReference(settlementGroupDTO);
        SettlementGroup settlementGroup = settlementGroupMapper.toEntity(settlementGroupDTO);
        settlementGroup = settlementGroupRepository.save(settlementGroup);
        SettlementGroupDTO result = settlementGroupMapper.toDto(settlementGroup);
        settlementGroupSearchRepository.save(settlementGroup);
        return result;
    }

    @Override
    public Optional<SettlementGroupDTO> partialUpdate(SettlementGroupDTO settlementGroupDTO) {
        log.debug("Request to partially update SettlementGroup : {}", settlementGroupDTO);

        if (settlementGroupDTO.getParentGroup() != null) {
            validateCircularReference(settlementGroupDTO);
        }

        return settlementGroupRepository
            .findById(settlementGroupDTO.getId())
            .map(existingSettlementGroup -> {
                settlementGroupMapper.partialUpdate(existingSettlementGroup, settlementGroupDTO);
                return existingSettlementGroup;
            })
            .map(settlementGroupRepository::save)
            .map(savedSettlementGroup -> {
                settlementGroupSearchRepository.save(savedSettlementGroup);
                return savedSettlementGroup;
            })
            .map(settlementGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SettlementGroups");
        return settlementGroupRepository.findAll(pageable).map(settlementGroupMapper::toDto);
    }

    public Page<SettlementGroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return settlementGroupRepository.findAllWithEagerRelationships(pageable).map(settlementGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SettlementGroupDTO> findOne(Long id) {
        log.debug("Request to get SettlementGroup : {}", id);
        return settlementGroupRepository.findOneWithEagerRelationships(id).map(settlementGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SettlementGroup : {}", id);
        settlementGroupRepository.deleteById(id);
        settlementGroupSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SettlementGroups for query {}", query);
        return settlementGroupSearchRepository.search(query, pageable).map(settlementGroupMapper::toDto);
    }

    private void validateCircularReference(SettlementGroupDTO settlementGroupDTO) {
        if (settlementGroupDTO.getParentGroup() == null) {
            return;
        }

        Long currentId = settlementGroupDTO.getId();
        Long parentId = settlementGroupDTO.getParentGroup().getId();
        
        if (currentId != null && currentId.equals(parentId)) {
            throw new IllegalArgumentException("Settlement group cannot be its own parent");
        }

        if (parentId == null) {
            return;
        }

        int depth = 0;
        Long checkId = parentId;
        
        while (checkId != null && depth < 10) {
            if (currentId != null && currentId.equals(checkId)) {
                throw new IllegalArgumentException("Circular reference detected in settlement group hierarchy");
            }
            
            Optional<SettlementGroup> parent = settlementGroupRepository.findById(checkId);
            if (parent.isPresent() && parent.get().getParentGroup() != null) {
                checkId = parent.get().getParentGroup().getId();
                depth++;
            } else {
                break;
            }
        }
        
        if (depth >= 10) {
            throw new IllegalArgumentException("Settlement group hierarchy too deep (maximum 10 levels allowed)");
        }
    }
}
