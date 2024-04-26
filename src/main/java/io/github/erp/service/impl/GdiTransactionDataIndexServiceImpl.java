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

import io.github.erp.domain.GdiTransactionDataIndex;
import io.github.erp.repository.GdiTransactionDataIndexRepository;
import io.github.erp.repository.search.GdiTransactionDataIndexSearchRepository;
import io.github.erp.service.GdiTransactionDataIndexService;
import io.github.erp.service.dto.GdiTransactionDataIndexDTO;
import io.github.erp.service.mapper.GdiTransactionDataIndexMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GdiTransactionDataIndex}.
 */
@Service
@Transactional
public class GdiTransactionDataIndexServiceImpl implements GdiTransactionDataIndexService {

    private final Logger log = LoggerFactory.getLogger(GdiTransactionDataIndexServiceImpl.class);

    private final GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository;

    private final GdiTransactionDataIndexMapper gdiTransactionDataIndexMapper;

    private final GdiTransactionDataIndexSearchRepository gdiTransactionDataIndexSearchRepository;

    public GdiTransactionDataIndexServiceImpl(
        GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository,
        GdiTransactionDataIndexMapper gdiTransactionDataIndexMapper,
        GdiTransactionDataIndexSearchRepository gdiTransactionDataIndexSearchRepository
    ) {
        this.gdiTransactionDataIndexRepository = gdiTransactionDataIndexRepository;
        this.gdiTransactionDataIndexMapper = gdiTransactionDataIndexMapper;
        this.gdiTransactionDataIndexSearchRepository = gdiTransactionDataIndexSearchRepository;
    }

    @Override
    public GdiTransactionDataIndexDTO save(GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO) {
        log.debug("Request to save GdiTransactionDataIndex : {}", gdiTransactionDataIndexDTO);
        GdiTransactionDataIndex gdiTransactionDataIndex = gdiTransactionDataIndexMapper.toEntity(gdiTransactionDataIndexDTO);
        gdiTransactionDataIndex = gdiTransactionDataIndexRepository.save(gdiTransactionDataIndex);
        GdiTransactionDataIndexDTO result = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);
        gdiTransactionDataIndexSearchRepository.save(gdiTransactionDataIndex);
        return result;
    }

    @Override
    public Optional<GdiTransactionDataIndexDTO> partialUpdate(GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO) {
        log.debug("Request to partially update GdiTransactionDataIndex : {}", gdiTransactionDataIndexDTO);

        return gdiTransactionDataIndexRepository
            .findById(gdiTransactionDataIndexDTO.getId())
            .map(existingGdiTransactionDataIndex -> {
                gdiTransactionDataIndexMapper.partialUpdate(existingGdiTransactionDataIndex, gdiTransactionDataIndexDTO);

                return existingGdiTransactionDataIndex;
            })
            .map(gdiTransactionDataIndexRepository::save)
            .map(savedGdiTransactionDataIndex -> {
                gdiTransactionDataIndexSearchRepository.save(savedGdiTransactionDataIndex);

                return savedGdiTransactionDataIndex;
            })
            .map(gdiTransactionDataIndexMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GdiTransactionDataIndexDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GdiTransactionDataIndices");
        return gdiTransactionDataIndexRepository.findAll(pageable).map(gdiTransactionDataIndexMapper::toDto);
    }

    public Page<GdiTransactionDataIndexDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gdiTransactionDataIndexRepository.findAllWithEagerRelationships(pageable).map(gdiTransactionDataIndexMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GdiTransactionDataIndexDTO> findOne(Long id) {
        log.debug("Request to get GdiTransactionDataIndex : {}", id);
        return gdiTransactionDataIndexRepository.findOneWithEagerRelationships(id).map(gdiTransactionDataIndexMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GdiTransactionDataIndex : {}", id);
        gdiTransactionDataIndexRepository.deleteById(id);
        gdiTransactionDataIndexSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GdiTransactionDataIndexDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GdiTransactionDataIndices for query {}", query);
        return gdiTransactionDataIndexSearchRepository.search(query, pageable).map(gdiTransactionDataIndexMapper::toDto);
    }
}
