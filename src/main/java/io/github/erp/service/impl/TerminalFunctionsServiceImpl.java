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
