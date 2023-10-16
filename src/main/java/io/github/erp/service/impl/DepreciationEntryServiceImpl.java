package io.github.erp.service.impl;

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

import io.github.erp.domain.DepreciationEntry;
import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.repository.search.DepreciationEntrySearchRepository;
import io.github.erp.service.DepreciationEntryService;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.mapper.DepreciationEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationEntry}.
 */
@Service
@Transactional
public class DepreciationEntryServiceImpl implements DepreciationEntryService {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryServiceImpl.class);

    private final DepreciationEntryRepository depreciationEntryRepository;

    private final DepreciationEntryMapper depreciationEntryMapper;

    private final DepreciationEntrySearchRepository depreciationEntrySearchRepository;

    public DepreciationEntryServiceImpl(
        DepreciationEntryRepository depreciationEntryRepository,
        DepreciationEntryMapper depreciationEntryMapper,
        DepreciationEntrySearchRepository depreciationEntrySearchRepository
    ) {
        this.depreciationEntryRepository = depreciationEntryRepository;
        this.depreciationEntryMapper = depreciationEntryMapper;
        this.depreciationEntrySearchRepository = depreciationEntrySearchRepository;
    }

    @Override
    public DepreciationEntryDTO save(DepreciationEntryDTO depreciationEntryDTO) {
        log.debug("Request to save DepreciationEntry : {}", depreciationEntryDTO);
        DepreciationEntry depreciationEntry = depreciationEntryMapper.toEntity(depreciationEntryDTO);
        depreciationEntry = depreciationEntryRepository.save(depreciationEntry);
        DepreciationEntryDTO result = depreciationEntryMapper.toDto(depreciationEntry);
        depreciationEntrySearchRepository.save(depreciationEntry);
        return result;
    }

    @Override
    public Optional<DepreciationEntryDTO> partialUpdate(DepreciationEntryDTO depreciationEntryDTO) {
        log.debug("Request to partially update DepreciationEntry : {}", depreciationEntryDTO);

        return depreciationEntryRepository
            .findById(depreciationEntryDTO.getId())
            .map(existingDepreciationEntry -> {
                depreciationEntryMapper.partialUpdate(existingDepreciationEntry, depreciationEntryDTO);

                return existingDepreciationEntry;
            })
            .map(depreciationEntryRepository::save)
            .map(savedDepreciationEntry -> {
                depreciationEntrySearchRepository.save(savedDepreciationEntry);

                return savedDepreciationEntry;
            })
            .map(depreciationEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationEntries");
        return depreciationEntryRepository.findAll(pageable).map(depreciationEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationEntryDTO> findOne(Long id) {
        log.debug("Request to get DepreciationEntry : {}", id);
        return depreciationEntryRepository.findById(id).map(depreciationEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationEntry : {}", id);
        depreciationEntryRepository.deleteById(id);
        depreciationEntrySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationEntryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationEntries for query {}", query);
        return depreciationEntrySearchRepository.search(query, pageable).map(depreciationEntryMapper::toDto);
    }
}
