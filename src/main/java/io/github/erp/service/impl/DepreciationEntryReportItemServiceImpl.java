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
