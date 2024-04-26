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

import io.github.erp.domain.GdiMasterDataIndex;
import io.github.erp.repository.GdiMasterDataIndexRepository;
import io.github.erp.repository.search.GdiMasterDataIndexSearchRepository;
import io.github.erp.service.GdiMasterDataIndexService;
import io.github.erp.service.dto.GdiMasterDataIndexDTO;
import io.github.erp.service.mapper.GdiMasterDataIndexMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GdiMasterDataIndex}.
 */
@Service
@Transactional
public class GdiMasterDataIndexServiceImpl implements GdiMasterDataIndexService {

    private final Logger log = LoggerFactory.getLogger(GdiMasterDataIndexServiceImpl.class);

    private final GdiMasterDataIndexRepository gdiMasterDataIndexRepository;

    private final GdiMasterDataIndexMapper gdiMasterDataIndexMapper;

    private final GdiMasterDataIndexSearchRepository gdiMasterDataIndexSearchRepository;

    public GdiMasterDataIndexServiceImpl(
        GdiMasterDataIndexRepository gdiMasterDataIndexRepository,
        GdiMasterDataIndexMapper gdiMasterDataIndexMapper,
        GdiMasterDataIndexSearchRepository gdiMasterDataIndexSearchRepository
    ) {
        this.gdiMasterDataIndexRepository = gdiMasterDataIndexRepository;
        this.gdiMasterDataIndexMapper = gdiMasterDataIndexMapper;
        this.gdiMasterDataIndexSearchRepository = gdiMasterDataIndexSearchRepository;
    }

    @Override
    public GdiMasterDataIndexDTO save(GdiMasterDataIndexDTO gdiMasterDataIndexDTO) {
        log.debug("Request to save GdiMasterDataIndex : {}", gdiMasterDataIndexDTO);
        GdiMasterDataIndex gdiMasterDataIndex = gdiMasterDataIndexMapper.toEntity(gdiMasterDataIndexDTO);
        gdiMasterDataIndex = gdiMasterDataIndexRepository.save(gdiMasterDataIndex);
        GdiMasterDataIndexDTO result = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);
        gdiMasterDataIndexSearchRepository.save(gdiMasterDataIndex);
        return result;
    }

    @Override
    public Optional<GdiMasterDataIndexDTO> partialUpdate(GdiMasterDataIndexDTO gdiMasterDataIndexDTO) {
        log.debug("Request to partially update GdiMasterDataIndex : {}", gdiMasterDataIndexDTO);

        return gdiMasterDataIndexRepository
            .findById(gdiMasterDataIndexDTO.getId())
            .map(existingGdiMasterDataIndex -> {
                gdiMasterDataIndexMapper.partialUpdate(existingGdiMasterDataIndex, gdiMasterDataIndexDTO);

                return existingGdiMasterDataIndex;
            })
            .map(gdiMasterDataIndexRepository::save)
            .map(savedGdiMasterDataIndex -> {
                gdiMasterDataIndexSearchRepository.save(savedGdiMasterDataIndex);

                return savedGdiMasterDataIndex;
            })
            .map(gdiMasterDataIndexMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GdiMasterDataIndexDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GdiMasterDataIndices");
        return gdiMasterDataIndexRepository.findAll(pageable).map(gdiMasterDataIndexMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GdiMasterDataIndexDTO> findOne(Long id) {
        log.debug("Request to get GdiMasterDataIndex : {}", id);
        return gdiMasterDataIndexRepository.findById(id).map(gdiMasterDataIndexMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GdiMasterDataIndex : {}", id);
        gdiMasterDataIndexRepository.deleteById(id);
        gdiMasterDataIndexSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GdiMasterDataIndexDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GdiMasterDataIndices for query {}", query);
        return gdiMasterDataIndexSearchRepository.search(query, pageable).map(gdiMasterDataIndexMapper::toDto);
    }
}
