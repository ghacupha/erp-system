package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.repository.NetBookValueEntryRepository;
import io.github.erp.repository.search.NetBookValueEntrySearchRepository;
import io.github.erp.service.NetBookValueEntryService;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.mapper.NetBookValueEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NetBookValueEntry}.
 */
@Service
@Transactional
public class NetBookValueEntryServiceImpl implements NetBookValueEntryService {

    private final Logger log = LoggerFactory.getLogger(NetBookValueEntryServiceImpl.class);

    private final NetBookValueEntryRepository netBookValueEntryRepository;

    private final NetBookValueEntryMapper netBookValueEntryMapper;

    private final NetBookValueEntrySearchRepository netBookValueEntrySearchRepository;

    public NetBookValueEntryServiceImpl(
        NetBookValueEntryRepository netBookValueEntryRepository,
        NetBookValueEntryMapper netBookValueEntryMapper,
        NetBookValueEntrySearchRepository netBookValueEntrySearchRepository
    ) {
        this.netBookValueEntryRepository = netBookValueEntryRepository;
        this.netBookValueEntryMapper = netBookValueEntryMapper;
        this.netBookValueEntrySearchRepository = netBookValueEntrySearchRepository;
    }

    @Override
    public NetBookValueEntryDTO save(NetBookValueEntryDTO netBookValueEntryDTO) {
        log.debug("Request to save NetBookValueEntry : {}", netBookValueEntryDTO);
        NetBookValueEntry netBookValueEntry = netBookValueEntryMapper.toEntity(netBookValueEntryDTO);
        netBookValueEntry = netBookValueEntryRepository.save(netBookValueEntry);
        NetBookValueEntryDTO result = netBookValueEntryMapper.toDto(netBookValueEntry);
        netBookValueEntrySearchRepository.save(netBookValueEntry);
        return result;
    }

    @Override
    public Optional<NetBookValueEntryDTO> partialUpdate(NetBookValueEntryDTO netBookValueEntryDTO) {
        log.debug("Request to partially update NetBookValueEntry : {}", netBookValueEntryDTO);

        return netBookValueEntryRepository
            .findById(netBookValueEntryDTO.getId())
            .map(existingNetBookValueEntry -> {
                netBookValueEntryMapper.partialUpdate(existingNetBookValueEntry, netBookValueEntryDTO);

                return existingNetBookValueEntry;
            })
            .map(netBookValueEntryRepository::save)
            .map(savedNetBookValueEntry -> {
                netBookValueEntrySearchRepository.save(savedNetBookValueEntry);

                return savedNetBookValueEntry;
            })
            .map(netBookValueEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NetBookValueEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NetBookValueEntries");
        return netBookValueEntryRepository.findAll(pageable).map(netBookValueEntryMapper::toDto);
    }

    public Page<NetBookValueEntryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return netBookValueEntryRepository.findAllWithEagerRelationships(pageable).map(netBookValueEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NetBookValueEntryDTO> findOne(Long id) {
        log.debug("Request to get NetBookValueEntry : {}", id);
        return netBookValueEntryRepository.findOneWithEagerRelationships(id).map(netBookValueEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NetBookValueEntry : {}", id);
        netBookValueEntryRepository.deleteById(id);
        netBookValueEntrySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NetBookValueEntryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NetBookValueEntries for query {}", query);
        return netBookValueEntrySearchRepository.search(query, pageable).map(netBookValueEntryMapper::toDto);
    }
}
