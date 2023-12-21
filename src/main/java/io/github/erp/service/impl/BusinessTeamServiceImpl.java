package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.BusinessTeam;
import io.github.erp.repository.BusinessTeamRepository;
import io.github.erp.repository.search.BusinessTeamSearchRepository;
import io.github.erp.service.BusinessTeamService;
import io.github.erp.service.dto.BusinessTeamDTO;
import io.github.erp.service.mapper.BusinessTeamMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusinessTeam}.
 */
@Service
@Transactional
public class BusinessTeamServiceImpl implements BusinessTeamService {

    private final Logger log = LoggerFactory.getLogger(BusinessTeamServiceImpl.class);

    private final BusinessTeamRepository businessTeamRepository;

    private final BusinessTeamMapper businessTeamMapper;

    private final BusinessTeamSearchRepository businessTeamSearchRepository;

    public BusinessTeamServiceImpl(
        BusinessTeamRepository businessTeamRepository,
        BusinessTeamMapper businessTeamMapper,
        BusinessTeamSearchRepository businessTeamSearchRepository
    ) {
        this.businessTeamRepository = businessTeamRepository;
        this.businessTeamMapper = businessTeamMapper;
        this.businessTeamSearchRepository = businessTeamSearchRepository;
    }

    @Override
    public BusinessTeamDTO save(BusinessTeamDTO businessTeamDTO) {
        log.debug("Request to save BusinessTeam : {}", businessTeamDTO);
        BusinessTeam businessTeam = businessTeamMapper.toEntity(businessTeamDTO);
        businessTeam = businessTeamRepository.save(businessTeam);
        BusinessTeamDTO result = businessTeamMapper.toDto(businessTeam);
        businessTeamSearchRepository.save(businessTeam);
        return result;
    }

    @Override
    public Optional<BusinessTeamDTO> partialUpdate(BusinessTeamDTO businessTeamDTO) {
        log.debug("Request to partially update BusinessTeam : {}", businessTeamDTO);

        return businessTeamRepository
            .findById(businessTeamDTO.getId())
            .map(existingBusinessTeam -> {
                businessTeamMapper.partialUpdate(existingBusinessTeam, businessTeamDTO);

                return existingBusinessTeam;
            })
            .map(businessTeamRepository::save)
            .map(savedBusinessTeam -> {
                businessTeamSearchRepository.save(savedBusinessTeam);

                return savedBusinessTeam;
            })
            .map(businessTeamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessTeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessTeams");
        return businessTeamRepository.findAll(pageable).map(businessTeamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessTeamDTO> findOne(Long id) {
        log.debug("Request to get BusinessTeam : {}", id);
        return businessTeamRepository.findById(id).map(businessTeamMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessTeam : {}", id);
        businessTeamRepository.deleteById(id);
        businessTeamSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessTeamDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessTeams for query {}", query);
        return businessTeamSearchRepository.search(query, pageable).map(businessTeamMapper::toDto);
    }
}
