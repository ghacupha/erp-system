package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.TerminalFunctions;
import io.github.erp.repository.TerminalFunctionsRepository;
import io.github.erp.repository.search.TerminalFunctionsSearchRepository;
import io.github.erp.service.TerminalFunctionsService;
import io.github.erp.service.dto.TerminalFunctionsDTO;
import io.github.erp.service.mapper.TerminalFunctionsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TerminalFunctions}.
 */
@Service
@Transactional
public class TerminalFunctionsServiceImpl implements TerminalFunctionsService {

    private final Logger log = LoggerFactory.getLogger(TerminalFunctionsServiceImpl.class);

    private final TerminalFunctionsRepository terminalFunctionsRepository;

    private final TerminalFunctionsMapper terminalFunctionsMapper;

    private final TerminalFunctionsSearchRepository terminalFunctionsSearchRepository;

    public TerminalFunctionsServiceImpl(
        TerminalFunctionsRepository terminalFunctionsRepository,
        TerminalFunctionsMapper terminalFunctionsMapper,
        TerminalFunctionsSearchRepository terminalFunctionsSearchRepository
    ) {
        this.terminalFunctionsRepository = terminalFunctionsRepository;
        this.terminalFunctionsMapper = terminalFunctionsMapper;
        this.terminalFunctionsSearchRepository = terminalFunctionsSearchRepository;
    }

    @Override
    public TerminalFunctionsDTO save(TerminalFunctionsDTO terminalFunctionsDTO) {
        log.debug("Request to save TerminalFunctions : {}", terminalFunctionsDTO);
        TerminalFunctions terminalFunctions = terminalFunctionsMapper.toEntity(terminalFunctionsDTO);
        terminalFunctions = terminalFunctionsRepository.save(terminalFunctions);
        TerminalFunctionsDTO result = terminalFunctionsMapper.toDto(terminalFunctions);
        terminalFunctionsSearchRepository.save(terminalFunctions);
        return result;
    }

    @Override
    public Optional<TerminalFunctionsDTO> partialUpdate(TerminalFunctionsDTO terminalFunctionsDTO) {
        log.debug("Request to partially update TerminalFunctions : {}", terminalFunctionsDTO);

        return terminalFunctionsRepository
            .findById(terminalFunctionsDTO.getId())
            .map(existingTerminalFunctions -> {
                terminalFunctionsMapper.partialUpdate(existingTerminalFunctions, terminalFunctionsDTO);

                return existingTerminalFunctions;
            })
            .map(terminalFunctionsRepository::save)
            .map(savedTerminalFunctions -> {
                terminalFunctionsSearchRepository.save(savedTerminalFunctions);

                return savedTerminalFunctions;
            })
            .map(terminalFunctionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalFunctionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TerminalFunctions");
        return terminalFunctionsRepository.findAll(pageable).map(terminalFunctionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerminalFunctionsDTO> findOne(Long id) {
        log.debug("Request to get TerminalFunctions : {}", id);
        return terminalFunctionsRepository.findById(id).map(terminalFunctionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TerminalFunctions : {}", id);
        terminalFunctionsRepository.deleteById(id);
        terminalFunctionsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalFunctionsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TerminalFunctions for query {}", query);
        return terminalFunctionsSearchRepository.search(query, pageable).map(terminalFunctionsMapper::toDto);
    }
}
