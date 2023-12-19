package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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

import io.github.erp.domain.TerminalTypes;
import io.github.erp.repository.TerminalTypesRepository;
import io.github.erp.repository.search.TerminalTypesSearchRepository;
import io.github.erp.service.TerminalTypesService;
import io.github.erp.service.dto.TerminalTypesDTO;
import io.github.erp.service.mapper.TerminalTypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TerminalTypes}.
 */
@Service
@Transactional
public class TerminalTypesServiceImpl implements TerminalTypesService {

    private final Logger log = LoggerFactory.getLogger(TerminalTypesServiceImpl.class);

    private final TerminalTypesRepository terminalTypesRepository;

    private final TerminalTypesMapper terminalTypesMapper;

    private final TerminalTypesSearchRepository terminalTypesSearchRepository;

    public TerminalTypesServiceImpl(
        TerminalTypesRepository terminalTypesRepository,
        TerminalTypesMapper terminalTypesMapper,
        TerminalTypesSearchRepository terminalTypesSearchRepository
    ) {
        this.terminalTypesRepository = terminalTypesRepository;
        this.terminalTypesMapper = terminalTypesMapper;
        this.terminalTypesSearchRepository = terminalTypesSearchRepository;
    }

    @Override
    public TerminalTypesDTO save(TerminalTypesDTO terminalTypesDTO) {
        log.debug("Request to save TerminalTypes : {}", terminalTypesDTO);
        TerminalTypes terminalTypes = terminalTypesMapper.toEntity(terminalTypesDTO);
        terminalTypes = terminalTypesRepository.save(terminalTypes);
        TerminalTypesDTO result = terminalTypesMapper.toDto(terminalTypes);
        terminalTypesSearchRepository.save(terminalTypes);
        return result;
    }

    @Override
    public Optional<TerminalTypesDTO> partialUpdate(TerminalTypesDTO terminalTypesDTO) {
        log.debug("Request to partially update TerminalTypes : {}", terminalTypesDTO);

        return terminalTypesRepository
            .findById(terminalTypesDTO.getId())
            .map(existingTerminalTypes -> {
                terminalTypesMapper.partialUpdate(existingTerminalTypes, terminalTypesDTO);

                return existingTerminalTypes;
            })
            .map(terminalTypesRepository::save)
            .map(savedTerminalTypes -> {
                terminalTypesSearchRepository.save(savedTerminalTypes);

                return savedTerminalTypes;
            })
            .map(terminalTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalTypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TerminalTypes");
        return terminalTypesRepository.findAll(pageable).map(terminalTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerminalTypesDTO> findOne(Long id) {
        log.debug("Request to get TerminalTypes : {}", id);
        return terminalTypesRepository.findById(id).map(terminalTypesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TerminalTypes : {}", id);
        terminalTypesRepository.deleteById(id);
        terminalTypesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalTypesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TerminalTypes for query {}", query);
        return terminalTypesSearchRepository.search(query, pageable).map(terminalTypesMapper::toDto);
    }
}
