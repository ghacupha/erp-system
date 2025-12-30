package io.github.erp.service.impl;
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.LeaseTemplate;
import io.github.erp.repository.LeaseTemplateRepository;
import io.github.erp.repository.search.LeaseTemplateSearchRepository;
import io.github.erp.service.LeaseTemplateService;
import io.github.erp.service.dto.LeaseTemplateDTO;
import io.github.erp.service.mapper.LeaseTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseTemplate}.
 */
@Service
@Transactional
public class LeaseTemplateServiceImpl implements LeaseTemplateService {

    private final Logger log = LoggerFactory.getLogger(LeaseTemplateServiceImpl.class);

    private final LeaseTemplateRepository leaseTemplateRepository;

    private final LeaseTemplateMapper leaseTemplateMapper;

    private final LeaseTemplateSearchRepository leaseTemplateSearchRepository;

    public LeaseTemplateServiceImpl(
        LeaseTemplateRepository leaseTemplateRepository,
        LeaseTemplateMapper leaseTemplateMapper,
        LeaseTemplateSearchRepository leaseTemplateSearchRepository
    ) {
        this.leaseTemplateRepository = leaseTemplateRepository;
        this.leaseTemplateMapper = leaseTemplateMapper;
        this.leaseTemplateSearchRepository = leaseTemplateSearchRepository;
    }

    @Override
    public LeaseTemplateDTO save(LeaseTemplateDTO leaseTemplateDTO) {
        log.debug("Request to save LeaseTemplate : {}", leaseTemplateDTO);
        LeaseTemplate leaseTemplate = leaseTemplateMapper.toEntity(leaseTemplateDTO);
        leaseTemplate = leaseTemplateRepository.save(leaseTemplate);
        LeaseTemplateDTO result = leaseTemplateMapper.toDto(leaseTemplate);
        leaseTemplateSearchRepository.save(leaseTemplate);
        return result;
    }

    @Override
    public Optional<LeaseTemplateDTO> partialUpdate(LeaseTemplateDTO leaseTemplateDTO) {
        log.debug("Request to partially update LeaseTemplate : {}", leaseTemplateDTO);

        return leaseTemplateRepository
            .findById(leaseTemplateDTO.getId())
            .map(existingLeaseTemplate -> {
                leaseTemplateMapper.partialUpdate(existingLeaseTemplate, leaseTemplateDTO);

                return existingLeaseTemplate;
            })
            .map(leaseTemplateRepository::save)
            .map(savedLeaseTemplate -> {
                leaseTemplateSearchRepository.save(savedLeaseTemplate);

                return savedLeaseTemplate;
            })
            .map(leaseTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseTemplates");
        return leaseTemplateRepository.findAll(pageable).map(leaseTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseTemplateDTO> findOne(Long id) {
        log.debug("Request to get LeaseTemplate : {}", id);
        return leaseTemplateRepository.findById(id).map(leaseTemplateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseTemplate : {}", id);
        leaseTemplateRepository.deleteById(id);
        leaseTemplateSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseTemplates for query {}", query);
        return leaseTemplateSearchRepository.search(query, pageable).map(leaseTemplateMapper::toDto);
    }
}
