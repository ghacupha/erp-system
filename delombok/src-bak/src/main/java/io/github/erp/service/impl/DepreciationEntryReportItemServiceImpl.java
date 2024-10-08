package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.DepreciationEntryReportItem;
import io.github.erp.repository.DepreciationEntryReportItemRepository;
import io.github.erp.repository.search.DepreciationEntryReportItemSearchRepository;
import io.github.erp.service.DepreciationEntryReportItemService;
import io.github.erp.service.dto.DepreciationEntryReportItemDTO;
import io.github.erp.service.mapper.DepreciationEntryReportItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationEntryReportItem}.
 */
@Service
@Transactional
public class DepreciationEntryReportItemServiceImpl implements DepreciationEntryReportItemService {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryReportItemServiceImpl.class);

    private final DepreciationEntryReportItemRepository depreciationEntryReportItemRepository;

    private final DepreciationEntryReportItemMapper depreciationEntryReportItemMapper;

    private final DepreciationEntryReportItemSearchRepository depreciationEntryReportItemSearchRepository;

    public DepreciationEntryReportItemServiceImpl(
        DepreciationEntryReportItemRepository depreciationEntryReportItemRepository,
        DepreciationEntryReportItemMapper depreciationEntryReportItemMapper,
        DepreciationEntryReportItemSearchRepository depreciationEntryReportItemSearchRepository
    ) {
        this.depreciationEntryReportItemRepository = depreciationEntryReportItemRepository;
        this.depreciationEntryReportItemMapper = depreciationEntryReportItemMapper;
        this.depreciationEntryReportItemSearchRepository = depreciationEntryReportItemSearchRepository;
    }

    @Override
    public DepreciationEntryReportItemDTO save(DepreciationEntryReportItemDTO depreciationEntryReportItemDTO) {
        log.debug("Request to save DepreciationEntryReportItem : {}", depreciationEntryReportItemDTO);
        DepreciationEntryReportItem depreciationEntryReportItem = depreciationEntryReportItemMapper.toEntity(
            depreciationEntryReportItemDTO
        );
        depreciationEntryReportItem = depreciationEntryReportItemRepository.save(depreciationEntryReportItem);
        DepreciationEntryReportItemDTO result = depreciationEntryReportItemMapper.toDto(depreciationEntryReportItem);
        depreciationEntryReportItemSearchRepository.save(depreciationEntryReportItem);
        return result;
    }

    @Override
    public Optional<DepreciationEntryReportItemDTO> partialUpdate(DepreciationEntryReportItemDTO depreciationEntryReportItemDTO) {
        log.debug("Request to partially update DepreciationEntryReportItem : {}", depreciationEntryReportItemDTO);

        return depreciationEntryReportItemRepository
            .findById(depreciationEntryReportItemDTO.getId())
            .map(existingDepreciationEntryReportItem -> {
                depreciationEntryReportItemMapper.partialUpdate(existingDepreciationEntryReportItem, depreciationEntryReportItemDTO);

                return existingDepreciationEntryReportItem;
            })
            .map(depreciationEntryReportItemRepository::save)
            .map(savedDepreciationEntryReportItem -> {
                depreciationEntryReportItemSearchRepository.save(savedDepreciationEntryReportItem);

                return savedDepreciationEntryReportItem;
            })
            .map(depreciationEntryReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationEntryReportItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationEntryReportItems");
        return depreciationEntryReportItemRepository.findAll(pageable).map(depreciationEntryReportItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationEntryReportItemDTO> findOne(Long id) {
        log.debug("Request to get DepreciationEntryReportItem : {}", id);
        return depreciationEntryReportItemRepository.findById(id).map(depreciationEntryReportItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationEntryReportItem : {}", id);
        depreciationEntryReportItemRepository.deleteById(id);
        depreciationEntryReportItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationEntryReportItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationEntryReportItems for query {}", query);
        return depreciationEntryReportItemSearchRepository.search(query, pageable).map(depreciationEntryReportItemMapper::toDto);
    }
}
