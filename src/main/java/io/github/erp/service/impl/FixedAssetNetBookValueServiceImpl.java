package io.github.erp.service.impl;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.service.FixedAssetNetBookValueService;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.service.mapper.FixedAssetNetBookValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FixedAssetNetBookValue}.
 */
@Service
@Transactional
public class FixedAssetNetBookValueServiceImpl implements FixedAssetNetBookValueService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetNetBookValueServiceImpl.class);

    private final FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository;

    private final FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper;

    private final FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository;

    public FixedAssetNetBookValueServiceImpl(
        FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository,
        FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper,
        FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository
    ) {
        this.fixedAssetNetBookValueRepository = fixedAssetNetBookValueRepository;
        this.fixedAssetNetBookValueMapper = fixedAssetNetBookValueMapper;
        this.fixedAssetNetBookValueSearchRepository = fixedAssetNetBookValueSearchRepository;
    }

    @Override
    public FixedAssetNetBookValueDTO save(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) {
        log.debug("Request to save FixedAssetNetBookValue : {}", fixedAssetNetBookValueDTO);
        FixedAssetNetBookValue fixedAssetNetBookValue = fixedAssetNetBookValueMapper.toEntity(fixedAssetNetBookValueDTO);
        fixedAssetNetBookValue = fixedAssetNetBookValueRepository.save(fixedAssetNetBookValue);
        FixedAssetNetBookValueDTO result = fixedAssetNetBookValueMapper.toDto(fixedAssetNetBookValue);
        fixedAssetNetBookValueSearchRepository.save(fixedAssetNetBookValue);
        return result;
    }

    @Override
    public Optional<FixedAssetNetBookValueDTO> partialUpdate(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) {
        log.debug("Request to partially update FixedAssetNetBookValue : {}", fixedAssetNetBookValueDTO);

        return fixedAssetNetBookValueRepository
            .findById(fixedAssetNetBookValueDTO.getId())
            .map(existingFixedAssetNetBookValue -> {
                fixedAssetNetBookValueMapper.partialUpdate(existingFixedAssetNetBookValue, fixedAssetNetBookValueDTO);

                return existingFixedAssetNetBookValue;
            })
            .map(fixedAssetNetBookValueRepository::save)
            .map(savedFixedAssetNetBookValue -> {
                fixedAssetNetBookValueSearchRepository.save(savedFixedAssetNetBookValue);

                return savedFixedAssetNetBookValue;
            })
            .map(fixedAssetNetBookValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetNetBookValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetNetBookValues");
        return fixedAssetNetBookValueRepository.findAll(pageable).map(fixedAssetNetBookValueMapper::toDto);
    }

    public Page<FixedAssetNetBookValueDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fixedAssetNetBookValueRepository.findAllWithEagerRelationships(pageable).map(fixedAssetNetBookValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetNetBookValueDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetNetBookValue : {}", id);
        return fixedAssetNetBookValueRepository.findOneWithEagerRelationships(id).map(fixedAssetNetBookValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetNetBookValue : {}", id);
        fixedAssetNetBookValueRepository.deleteById(id);
        fixedAssetNetBookValueSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetNetBookValueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FixedAssetNetBookValues for query {}", query);
        return fixedAssetNetBookValueSearchRepository.search(query, pageable).map(fixedAssetNetBookValueMapper::toDto);
    }
}
