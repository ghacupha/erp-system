package io.github.erp.service;

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

import io.github.erp.domain.CrbFileTransmissionStatus;
import io.github.erp.repository.CrbFileTransmissionStatusRepository;
import io.github.erp.repository.search.CrbFileTransmissionStatusSearchRepository;
import io.github.erp.service.dto.CrbFileTransmissionStatusDTO;
import io.github.erp.service.mapper.CrbFileTransmissionStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbFileTransmissionStatus}.
 */
@Service
@Transactional
public class CrbFileTransmissionStatusService {

    private final Logger log = LoggerFactory.getLogger(CrbFileTransmissionStatusService.class);

    private final CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository;

    private final CrbFileTransmissionStatusMapper crbFileTransmissionStatusMapper;

    private final CrbFileTransmissionStatusSearchRepository crbFileTransmissionStatusSearchRepository;

    public CrbFileTransmissionStatusService(
        CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository,
        CrbFileTransmissionStatusMapper crbFileTransmissionStatusMapper,
        CrbFileTransmissionStatusSearchRepository crbFileTransmissionStatusSearchRepository
    ) {
        this.crbFileTransmissionStatusRepository = crbFileTransmissionStatusRepository;
        this.crbFileTransmissionStatusMapper = crbFileTransmissionStatusMapper;
        this.crbFileTransmissionStatusSearchRepository = crbFileTransmissionStatusSearchRepository;
    }

    /**
     * Save a crbFileTransmissionStatus.
     *
     * @param crbFileTransmissionStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public CrbFileTransmissionStatusDTO save(CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO) {
        log.debug("Request to save CrbFileTransmissionStatus : {}", crbFileTransmissionStatusDTO);
        CrbFileTransmissionStatus crbFileTransmissionStatus = crbFileTransmissionStatusMapper.toEntity(crbFileTransmissionStatusDTO);
        crbFileTransmissionStatus = crbFileTransmissionStatusRepository.save(crbFileTransmissionStatus);
        CrbFileTransmissionStatusDTO result = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);
        crbFileTransmissionStatusSearchRepository.save(crbFileTransmissionStatus);
        return result;
    }

    /**
     * Partially update a crbFileTransmissionStatus.
     *
     * @param crbFileTransmissionStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CrbFileTransmissionStatusDTO> partialUpdate(CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO) {
        log.debug("Request to partially update CrbFileTransmissionStatus : {}", crbFileTransmissionStatusDTO);

        return crbFileTransmissionStatusRepository
            .findById(crbFileTransmissionStatusDTO.getId())
            .map(existingCrbFileTransmissionStatus -> {
                crbFileTransmissionStatusMapper.partialUpdate(existingCrbFileTransmissionStatus, crbFileTransmissionStatusDTO);

                return existingCrbFileTransmissionStatus;
            })
            .map(crbFileTransmissionStatusRepository::save)
            .map(savedCrbFileTransmissionStatus -> {
                crbFileTransmissionStatusSearchRepository.save(savedCrbFileTransmissionStatus);

                return savedCrbFileTransmissionStatus;
            })
            .map(crbFileTransmissionStatusMapper::toDto);
    }

    /**
     * Get all the crbFileTransmissionStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbFileTransmissionStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbFileTransmissionStatuses");
        return crbFileTransmissionStatusRepository.findAll(pageable).map(crbFileTransmissionStatusMapper::toDto);
    }

    /**
     * Get one crbFileTransmissionStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CrbFileTransmissionStatusDTO> findOne(Long id) {
        log.debug("Request to get CrbFileTransmissionStatus : {}", id);
        return crbFileTransmissionStatusRepository.findById(id).map(crbFileTransmissionStatusMapper::toDto);
    }

    /**
     * Delete the crbFileTransmissionStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CrbFileTransmissionStatus : {}", id);
        crbFileTransmissionStatusRepository.deleteById(id);
        crbFileTransmissionStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the crbFileTransmissionStatus corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbFileTransmissionStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbFileTransmissionStatuses for query {}", query);
        return crbFileTransmissionStatusSearchRepository.search(query, pageable).map(crbFileTransmissionStatusMapper::toDto);
    }
}
