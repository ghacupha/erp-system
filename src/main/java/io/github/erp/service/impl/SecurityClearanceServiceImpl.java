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

import io.github.erp.domain.SecurityClearance;
import io.github.erp.repository.SecurityClearanceRepository;
import io.github.erp.repository.search.SecurityClearanceSearchRepository;
import io.github.erp.service.SecurityClearanceService;
import io.github.erp.service.dto.SecurityClearanceDTO;
import io.github.erp.service.mapper.SecurityClearanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SecurityClearance}.
 */
@Service
@Transactional
public class SecurityClearanceServiceImpl implements SecurityClearanceService {

    private final Logger log = LoggerFactory.getLogger(SecurityClearanceServiceImpl.class);

    private final SecurityClearanceRepository securityClearanceRepository;

    private final SecurityClearanceMapper securityClearanceMapper;

    private final SecurityClearanceSearchRepository securityClearanceSearchRepository;

    public SecurityClearanceServiceImpl(
        SecurityClearanceRepository securityClearanceRepository,
        SecurityClearanceMapper securityClearanceMapper,
        SecurityClearanceSearchRepository securityClearanceSearchRepository
    ) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.securityClearanceMapper = securityClearanceMapper;
        this.securityClearanceSearchRepository = securityClearanceSearchRepository;
    }

    @Override
    public SecurityClearanceDTO save(SecurityClearanceDTO securityClearanceDTO) {
        log.debug("Request to save SecurityClearance : {}", securityClearanceDTO);
        SecurityClearance securityClearance = securityClearanceMapper.toEntity(securityClearanceDTO);
        securityClearance = securityClearanceRepository.save(securityClearance);
        SecurityClearanceDTO result = securityClearanceMapper.toDto(securityClearance);
        securityClearanceSearchRepository.save(securityClearance);
        return result;
    }

    @Override
    public Optional<SecurityClearanceDTO> partialUpdate(SecurityClearanceDTO securityClearanceDTO) {
        log.debug("Request to partially update SecurityClearance : {}", securityClearanceDTO);

        return securityClearanceRepository
            .findById(securityClearanceDTO.getId())
            .map(existingSecurityClearance -> {
                securityClearanceMapper.partialUpdate(existingSecurityClearance, securityClearanceDTO);

                return existingSecurityClearance;
            })
            .map(securityClearanceRepository::save)
            .map(savedSecurityClearance -> {
                securityClearanceSearchRepository.save(savedSecurityClearance);

                return savedSecurityClearance;
            })
            .map(securityClearanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityClearanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityClearances");
        return securityClearanceRepository.findAll(pageable).map(securityClearanceMapper::toDto);
    }

    public Page<SecurityClearanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return securityClearanceRepository.findAllWithEagerRelationships(pageable).map(securityClearanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityClearanceDTO> findOne(Long id) {
        log.debug("Request to get SecurityClearance : {}", id);
        return securityClearanceRepository.findOneWithEagerRelationships(id).map(securityClearanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SecurityClearance : {}", id);
        securityClearanceRepository.deleteById(id);
        securityClearanceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityClearanceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityClearances for query {}", query);
        return securityClearanceSearchRepository.search(query, pageable).map(securityClearanceMapper::toDto);
    }
}
