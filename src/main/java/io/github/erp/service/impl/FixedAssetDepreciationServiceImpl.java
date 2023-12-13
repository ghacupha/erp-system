package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.FixedAssetDepreciation;
import io.github.erp.repository.FixedAssetDepreciationRepository;
import io.github.erp.repository.search.FixedAssetDepreciationSearchRepository;
import io.github.erp.service.FixedAssetDepreciationService;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import io.github.erp.service.mapper.FixedAssetDepreciationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FixedAssetDepreciation}.
 */
@Service
@Transactional
public class FixedAssetDepreciationServiceImpl implements FixedAssetDepreciationService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetDepreciationServiceImpl.class);

    private final FixedAssetDepreciationRepository fixedAssetDepreciationRepository;

    private final FixedAssetDepreciationMapper fixedAssetDepreciationMapper;

    private final FixedAssetDepreciationSearchRepository fixedAssetDepreciationSearchRepository;

    public FixedAssetDepreciationServiceImpl(
        FixedAssetDepreciationRepository fixedAssetDepreciationRepository,
        FixedAssetDepreciationMapper fixedAssetDepreciationMapper,
        FixedAssetDepreciationSearchRepository fixedAssetDepreciationSearchRepository
    ) {
        this.fixedAssetDepreciationRepository = fixedAssetDepreciationRepository;
        this.fixedAssetDepreciationMapper = fixedAssetDepreciationMapper;
        this.fixedAssetDepreciationSearchRepository = fixedAssetDepreciationSearchRepository;
    }

    @Override
    public FixedAssetDepreciationDTO save(FixedAssetDepreciationDTO fixedAssetDepreciationDTO) {
        log.debug("Request to save FixedAssetDepreciation : {}", fixedAssetDepreciationDTO);
        FixedAssetDepreciation fixedAssetDepreciation = fixedAssetDepreciationMapper.toEntity(fixedAssetDepreciationDTO);
        fixedAssetDepreciation = fixedAssetDepreciationRepository.save(fixedAssetDepreciation);
        FixedAssetDepreciationDTO result = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);
        fixedAssetDepreciationSearchRepository.save(fixedAssetDepreciation);
        return result;
    }

    @Override
    public Optional<FixedAssetDepreciationDTO> partialUpdate(FixedAssetDepreciationDTO fixedAssetDepreciationDTO) {
        log.debug("Request to partially update FixedAssetDepreciation : {}", fixedAssetDepreciationDTO);

        return fixedAssetDepreciationRepository
            .findById(fixedAssetDepreciationDTO.getId())
            .map(existingFixedAssetDepreciation -> {
                fixedAssetDepreciationMapper.partialUpdate(existingFixedAssetDepreciation, fixedAssetDepreciationDTO);

                return existingFixedAssetDepreciation;
            })
            .map(fixedAssetDepreciationRepository::save)
            .map(savedFixedAssetDepreciation -> {
                fixedAssetDepreciationSearchRepository.save(savedFixedAssetDepreciation);

                return savedFixedAssetDepreciation;
            })
            .map(fixedAssetDepreciationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetDepreciationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetDepreciations");
        return fixedAssetDepreciationRepository.findAll(pageable).map(fixedAssetDepreciationMapper::toDto);
    }

    public Page<FixedAssetDepreciationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fixedAssetDepreciationRepository.findAllWithEagerRelationships(pageable).map(fixedAssetDepreciationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetDepreciationDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetDepreciation : {}", id);
        return fixedAssetDepreciationRepository.findOneWithEagerRelationships(id).map(fixedAssetDepreciationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetDepreciation : {}", id);
        fixedAssetDepreciationRepository.deleteById(id);
        fixedAssetDepreciationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetDepreciationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FixedAssetDepreciations for query {}", query);
        return fixedAssetDepreciationSearchRepository.search(query, pageable).map(fixedAssetDepreciationMapper::toDto);
    }
}
