package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.domain.AgentBankingActivity;
import io.github.erp.repository.AgentBankingActivityRepository;
import io.github.erp.repository.search.AgentBankingActivitySearchRepository;
import io.github.erp.service.AgentBankingActivityService;
import io.github.erp.service.dto.AgentBankingActivityDTO;
import io.github.erp.service.mapper.AgentBankingActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgentBankingActivity}.
 */
@Service
@Transactional
public class AgentBankingActivityServiceImpl implements AgentBankingActivityService {

    private final Logger log = LoggerFactory.getLogger(AgentBankingActivityServiceImpl.class);

    private final AgentBankingActivityRepository agentBankingActivityRepository;

    private final AgentBankingActivityMapper agentBankingActivityMapper;

    private final AgentBankingActivitySearchRepository agentBankingActivitySearchRepository;

    public AgentBankingActivityServiceImpl(
        AgentBankingActivityRepository agentBankingActivityRepository,
        AgentBankingActivityMapper agentBankingActivityMapper,
        AgentBankingActivitySearchRepository agentBankingActivitySearchRepository
    ) {
        this.agentBankingActivityRepository = agentBankingActivityRepository;
        this.agentBankingActivityMapper = agentBankingActivityMapper;
        this.agentBankingActivitySearchRepository = agentBankingActivitySearchRepository;
    }

    @Override
    public AgentBankingActivityDTO save(AgentBankingActivityDTO agentBankingActivityDTO) {
        log.debug("Request to save AgentBankingActivity : {}", agentBankingActivityDTO);
        AgentBankingActivity agentBankingActivity = agentBankingActivityMapper.toEntity(agentBankingActivityDTO);
        agentBankingActivity = agentBankingActivityRepository.save(agentBankingActivity);
        AgentBankingActivityDTO result = agentBankingActivityMapper.toDto(agentBankingActivity);
        agentBankingActivitySearchRepository.save(agentBankingActivity);
        return result;
    }

    @Override
    public Optional<AgentBankingActivityDTO> partialUpdate(AgentBankingActivityDTO agentBankingActivityDTO) {
        log.debug("Request to partially update AgentBankingActivity : {}", agentBankingActivityDTO);

        return agentBankingActivityRepository
            .findById(agentBankingActivityDTO.getId())
            .map(existingAgentBankingActivity -> {
                agentBankingActivityMapper.partialUpdate(existingAgentBankingActivity, agentBankingActivityDTO);

                return existingAgentBankingActivity;
            })
            .map(agentBankingActivityRepository::save)
            .map(savedAgentBankingActivity -> {
                agentBankingActivitySearchRepository.save(savedAgentBankingActivity);

                return savedAgentBankingActivity;
            })
            .map(agentBankingActivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgentBankingActivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgentBankingActivities");
        return agentBankingActivityRepository.findAll(pageable).map(agentBankingActivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgentBankingActivityDTO> findOne(Long id) {
        log.debug("Request to get AgentBankingActivity : {}", id);
        return agentBankingActivityRepository.findById(id).map(agentBankingActivityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgentBankingActivity : {}", id);
        agentBankingActivityRepository.deleteById(id);
        agentBankingActivitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgentBankingActivityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgentBankingActivities for query {}", query);
        return agentBankingActivitySearchRepository.search(query, pageable).map(agentBankingActivityMapper::toDto);
    }
}
