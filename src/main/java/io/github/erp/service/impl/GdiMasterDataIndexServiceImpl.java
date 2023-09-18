package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
