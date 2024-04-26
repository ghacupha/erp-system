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

import io.github.erp.domain.TerminalsAndPOS;
import io.github.erp.repository.TerminalsAndPOSRepository;
import io.github.erp.repository.search.TerminalsAndPOSSearchRepository;
import io.github.erp.service.TerminalsAndPOSService;
import io.github.erp.service.dto.TerminalsAndPOSDTO;
import io.github.erp.service.mapper.TerminalsAndPOSMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TerminalsAndPOS}.
 */
@Service
@Transactional
public class TerminalsAndPOSServiceImpl implements TerminalsAndPOSService {

    private final Logger log = LoggerFactory.getLogger(TerminalsAndPOSServiceImpl.class);

    private final TerminalsAndPOSRepository terminalsAndPOSRepository;

    private final TerminalsAndPOSMapper terminalsAndPOSMapper;

    private final TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository;

    public TerminalsAndPOSServiceImpl(
        TerminalsAndPOSRepository terminalsAndPOSRepository,
        TerminalsAndPOSMapper terminalsAndPOSMapper,
        TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository
    ) {
        this.terminalsAndPOSRepository = terminalsAndPOSRepository;
        this.terminalsAndPOSMapper = terminalsAndPOSMapper;
        this.terminalsAndPOSSearchRepository = terminalsAndPOSSearchRepository;
    }

    @Override
    public TerminalsAndPOSDTO save(TerminalsAndPOSDTO terminalsAndPOSDTO) {
        log.debug("Request to save TerminalsAndPOS : {}", terminalsAndPOSDTO);
        TerminalsAndPOS terminalsAndPOS = terminalsAndPOSMapper.toEntity(terminalsAndPOSDTO);
        terminalsAndPOS = terminalsAndPOSRepository.save(terminalsAndPOS);
        TerminalsAndPOSDTO result = terminalsAndPOSMapper.toDto(terminalsAndPOS);
        terminalsAndPOSSearchRepository.save(terminalsAndPOS);
        return result;
    }

    @Override
    public Optional<TerminalsAndPOSDTO> partialUpdate(TerminalsAndPOSDTO terminalsAndPOSDTO) {
        log.debug("Request to partially update TerminalsAndPOS : {}", terminalsAndPOSDTO);

        return terminalsAndPOSRepository
            .findById(terminalsAndPOSDTO.getId())
            .map(existingTerminalsAndPOS -> {
                terminalsAndPOSMapper.partialUpdate(existingTerminalsAndPOS, terminalsAndPOSDTO);

                return existingTerminalsAndPOS;
            })
            .map(terminalsAndPOSRepository::save)
            .map(savedTerminalsAndPOS -> {
                terminalsAndPOSSearchRepository.save(savedTerminalsAndPOS);

                return savedTerminalsAndPOS;
            })
            .map(terminalsAndPOSMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalsAndPOSDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TerminalsAndPOS");
        return terminalsAndPOSRepository.findAll(pageable).map(terminalsAndPOSMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerminalsAndPOSDTO> findOne(Long id) {
        log.debug("Request to get TerminalsAndPOS : {}", id);
        return terminalsAndPOSRepository.findById(id).map(terminalsAndPOSMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TerminalsAndPOS : {}", id);
        terminalsAndPOSRepository.deleteById(id);
        terminalsAndPOSSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalsAndPOSDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TerminalsAndPOS for query {}", query);
        return terminalsAndPOSSearchRepository.search(query, pageable).map(terminalsAndPOSMapper::toDto);
    }
}
