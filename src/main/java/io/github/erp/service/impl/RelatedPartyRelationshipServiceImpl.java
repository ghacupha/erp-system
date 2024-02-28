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

import io.github.erp.domain.RelatedPartyRelationship;
import io.github.erp.repository.RelatedPartyRelationshipRepository;
import io.github.erp.repository.search.RelatedPartyRelationshipSearchRepository;
import io.github.erp.service.RelatedPartyRelationshipService;
import io.github.erp.service.dto.RelatedPartyRelationshipDTO;
import io.github.erp.service.mapper.RelatedPartyRelationshipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RelatedPartyRelationship}.
 */
@Service
@Transactional
public class RelatedPartyRelationshipServiceImpl implements RelatedPartyRelationshipService {

    private final Logger log = LoggerFactory.getLogger(RelatedPartyRelationshipServiceImpl.class);

    private final RelatedPartyRelationshipRepository relatedPartyRelationshipRepository;

    private final RelatedPartyRelationshipMapper relatedPartyRelationshipMapper;

    private final RelatedPartyRelationshipSearchRepository relatedPartyRelationshipSearchRepository;

    public RelatedPartyRelationshipServiceImpl(
        RelatedPartyRelationshipRepository relatedPartyRelationshipRepository,
        RelatedPartyRelationshipMapper relatedPartyRelationshipMapper,
        RelatedPartyRelationshipSearchRepository relatedPartyRelationshipSearchRepository
    ) {
        this.relatedPartyRelationshipRepository = relatedPartyRelationshipRepository;
        this.relatedPartyRelationshipMapper = relatedPartyRelationshipMapper;
        this.relatedPartyRelationshipSearchRepository = relatedPartyRelationshipSearchRepository;
    }

    @Override
    public RelatedPartyRelationshipDTO save(RelatedPartyRelationshipDTO relatedPartyRelationshipDTO) {
        log.debug("Request to save RelatedPartyRelationship : {}", relatedPartyRelationshipDTO);
        RelatedPartyRelationship relatedPartyRelationship = relatedPartyRelationshipMapper.toEntity(relatedPartyRelationshipDTO);
        relatedPartyRelationship = relatedPartyRelationshipRepository.save(relatedPartyRelationship);
        RelatedPartyRelationshipDTO result = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);
        relatedPartyRelationshipSearchRepository.save(relatedPartyRelationship);
        return result;
    }

    @Override
    public Optional<RelatedPartyRelationshipDTO> partialUpdate(RelatedPartyRelationshipDTO relatedPartyRelationshipDTO) {
        log.debug("Request to partially update RelatedPartyRelationship : {}", relatedPartyRelationshipDTO);

        return relatedPartyRelationshipRepository
            .findById(relatedPartyRelationshipDTO.getId())
            .map(existingRelatedPartyRelationship -> {
                relatedPartyRelationshipMapper.partialUpdate(existingRelatedPartyRelationship, relatedPartyRelationshipDTO);

                return existingRelatedPartyRelationship;
            })
            .map(relatedPartyRelationshipRepository::save)
            .map(savedRelatedPartyRelationship -> {
                relatedPartyRelationshipSearchRepository.save(savedRelatedPartyRelationship);

                return savedRelatedPartyRelationship;
            })
            .map(relatedPartyRelationshipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RelatedPartyRelationshipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RelatedPartyRelationships");
        return relatedPartyRelationshipRepository.findAll(pageable).map(relatedPartyRelationshipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RelatedPartyRelationshipDTO> findOne(Long id) {
        log.debug("Request to get RelatedPartyRelationship : {}", id);
        return relatedPartyRelationshipRepository.findById(id).map(relatedPartyRelationshipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RelatedPartyRelationship : {}", id);
        relatedPartyRelationshipRepository.deleteById(id);
        relatedPartyRelationshipSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RelatedPartyRelationshipDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RelatedPartyRelationships for query {}", query);
        return relatedPartyRelationshipSearchRepository.search(query, pageable).map(relatedPartyRelationshipMapper::toDto);
    }
}
