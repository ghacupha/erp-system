package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
