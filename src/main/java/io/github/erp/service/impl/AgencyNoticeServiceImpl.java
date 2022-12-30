package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 7 (Caleb Series) Server ver 0.3.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.AgencyNotice;
import io.github.erp.repository.AgencyNoticeRepository;
import io.github.erp.repository.search.AgencyNoticeSearchRepository;
import io.github.erp.service.AgencyNoticeService;
import io.github.erp.service.dto.AgencyNoticeDTO;
import io.github.erp.service.mapper.AgencyNoticeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgencyNotice}.
 */
@Service
@Transactional
public class AgencyNoticeServiceImpl implements AgencyNoticeService {

    private final Logger log = LoggerFactory.getLogger(AgencyNoticeServiceImpl.class);

    private final AgencyNoticeRepository agencyNoticeRepository;

    private final AgencyNoticeMapper agencyNoticeMapper;

    private final AgencyNoticeSearchRepository agencyNoticeSearchRepository;

    public AgencyNoticeServiceImpl(
        AgencyNoticeRepository agencyNoticeRepository,
        AgencyNoticeMapper agencyNoticeMapper,
        AgencyNoticeSearchRepository agencyNoticeSearchRepository
    ) {
        this.agencyNoticeRepository = agencyNoticeRepository;
        this.agencyNoticeMapper = agencyNoticeMapper;
        this.agencyNoticeSearchRepository = agencyNoticeSearchRepository;
    }

    @Override
    public AgencyNoticeDTO save(AgencyNoticeDTO agencyNoticeDTO) {
        log.debug("Request to save AgencyNotice : {}", agencyNoticeDTO);
        AgencyNotice agencyNotice = agencyNoticeMapper.toEntity(agencyNoticeDTO);
        agencyNotice = agencyNoticeRepository.save(agencyNotice);
        AgencyNoticeDTO result = agencyNoticeMapper.toDto(agencyNotice);
        agencyNoticeSearchRepository.save(agencyNotice);
        return result;
    }

    @Override
    public Optional<AgencyNoticeDTO> partialUpdate(AgencyNoticeDTO agencyNoticeDTO) {
        log.debug("Request to partially update AgencyNotice : {}", agencyNoticeDTO);

        return agencyNoticeRepository
            .findById(agencyNoticeDTO.getId())
            .map(existingAgencyNotice -> {
                agencyNoticeMapper.partialUpdate(existingAgencyNotice, agencyNoticeDTO);

                return existingAgencyNotice;
            })
            .map(agencyNoticeRepository::save)
            .map(savedAgencyNotice -> {
                agencyNoticeSearchRepository.save(savedAgencyNotice);

                return savedAgencyNotice;
            })
            .map(agencyNoticeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyNoticeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgencyNotices");
        return agencyNoticeRepository.findAll(pageable).map(agencyNoticeMapper::toDto);
    }

    public Page<AgencyNoticeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agencyNoticeRepository.findAllWithEagerRelationships(pageable).map(agencyNoticeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgencyNoticeDTO> findOne(Long id) {
        log.debug("Request to get AgencyNotice : {}", id);
        return agencyNoticeRepository.findOneWithEagerRelationships(id).map(agencyNoticeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgencyNotice : {}", id);
        agencyNoticeRepository.deleteById(id);
        agencyNoticeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyNoticeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgencyNotices for query {}", query);
        return agencyNoticeSearchRepository.search(query, pageable).map(agencyNoticeMapper::toDto);
    }
}
