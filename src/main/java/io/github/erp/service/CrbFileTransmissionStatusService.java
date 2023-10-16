package io.github.erp.service;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
