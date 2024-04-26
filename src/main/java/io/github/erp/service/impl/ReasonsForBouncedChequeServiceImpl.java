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

import io.github.erp.domain.ReasonsForBouncedCheque;
import io.github.erp.repository.ReasonsForBouncedChequeRepository;
import io.github.erp.repository.search.ReasonsForBouncedChequeSearchRepository;
import io.github.erp.service.ReasonsForBouncedChequeService;
import io.github.erp.service.dto.ReasonsForBouncedChequeDTO;
import io.github.erp.service.mapper.ReasonsForBouncedChequeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReasonsForBouncedCheque}.
 */
@Service
@Transactional
public class ReasonsForBouncedChequeServiceImpl implements ReasonsForBouncedChequeService {

    private final Logger log = LoggerFactory.getLogger(ReasonsForBouncedChequeServiceImpl.class);

    private final ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository;

    private final ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper;

    private final ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository;

    public ReasonsForBouncedChequeServiceImpl(
        ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository,
        ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper,
        ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository
    ) {
        this.reasonsForBouncedChequeRepository = reasonsForBouncedChequeRepository;
        this.reasonsForBouncedChequeMapper = reasonsForBouncedChequeMapper;
        this.reasonsForBouncedChequeSearchRepository = reasonsForBouncedChequeSearchRepository;
    }

    @Override
    public ReasonsForBouncedChequeDTO save(ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO) {
        log.debug("Request to save ReasonsForBouncedCheque : {}", reasonsForBouncedChequeDTO);
        ReasonsForBouncedCheque reasonsForBouncedCheque = reasonsForBouncedChequeMapper.toEntity(reasonsForBouncedChequeDTO);
        reasonsForBouncedCheque = reasonsForBouncedChequeRepository.save(reasonsForBouncedCheque);
        ReasonsForBouncedChequeDTO result = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);
        reasonsForBouncedChequeSearchRepository.save(reasonsForBouncedCheque);
        return result;
    }

    @Override
    public Optional<ReasonsForBouncedChequeDTO> partialUpdate(ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO) {
        log.debug("Request to partially update ReasonsForBouncedCheque : {}", reasonsForBouncedChequeDTO);

        return reasonsForBouncedChequeRepository
            .findById(reasonsForBouncedChequeDTO.getId())
            .map(existingReasonsForBouncedCheque -> {
                reasonsForBouncedChequeMapper.partialUpdate(existingReasonsForBouncedCheque, reasonsForBouncedChequeDTO);

                return existingReasonsForBouncedCheque;
            })
            .map(reasonsForBouncedChequeRepository::save)
            .map(savedReasonsForBouncedCheque -> {
                reasonsForBouncedChequeSearchRepository.save(savedReasonsForBouncedCheque);

                return savedReasonsForBouncedCheque;
            })
            .map(reasonsForBouncedChequeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReasonsForBouncedChequeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReasonsForBouncedCheques");
        return reasonsForBouncedChequeRepository.findAll(pageable).map(reasonsForBouncedChequeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReasonsForBouncedChequeDTO> findOne(Long id) {
        log.debug("Request to get ReasonsForBouncedCheque : {}", id);
        return reasonsForBouncedChequeRepository.findById(id).map(reasonsForBouncedChequeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReasonsForBouncedCheque : {}", id);
        reasonsForBouncedChequeRepository.deleteById(id);
        reasonsForBouncedChequeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReasonsForBouncedChequeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReasonsForBouncedCheques for query {}", query);
        return reasonsForBouncedChequeSearchRepository.search(query, pageable).map(reasonsForBouncedChequeMapper::toDto);
    }
}
