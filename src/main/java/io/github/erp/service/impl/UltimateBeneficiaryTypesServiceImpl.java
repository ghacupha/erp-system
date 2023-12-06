package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

import io.github.erp.domain.UltimateBeneficiaryTypes;
import io.github.erp.repository.UltimateBeneficiaryTypesRepository;
import io.github.erp.repository.search.UltimateBeneficiaryTypesSearchRepository;
import io.github.erp.service.UltimateBeneficiaryTypesService;
import io.github.erp.service.dto.UltimateBeneficiaryTypesDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryTypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UltimateBeneficiaryTypes}.
 */
@Service
@Transactional
public class UltimateBeneficiaryTypesServiceImpl implements UltimateBeneficiaryTypesService {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryTypesServiceImpl.class);

    private final UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository;

    private final UltimateBeneficiaryTypesMapper ultimateBeneficiaryTypesMapper;

    private final UltimateBeneficiaryTypesSearchRepository ultimateBeneficiaryTypesSearchRepository;

    public UltimateBeneficiaryTypesServiceImpl(
        UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository,
        UltimateBeneficiaryTypesMapper ultimateBeneficiaryTypesMapper,
        UltimateBeneficiaryTypesSearchRepository ultimateBeneficiaryTypesSearchRepository
    ) {
        this.ultimateBeneficiaryTypesRepository = ultimateBeneficiaryTypesRepository;
        this.ultimateBeneficiaryTypesMapper = ultimateBeneficiaryTypesMapper;
        this.ultimateBeneficiaryTypesSearchRepository = ultimateBeneficiaryTypesSearchRepository;
    }

    @Override
    public UltimateBeneficiaryTypesDTO save(UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO) {
        log.debug("Request to save UltimateBeneficiaryTypes : {}", ultimateBeneficiaryTypesDTO);
        UltimateBeneficiaryTypes ultimateBeneficiaryTypes = ultimateBeneficiaryTypesMapper.toEntity(ultimateBeneficiaryTypesDTO);
        ultimateBeneficiaryTypes = ultimateBeneficiaryTypesRepository.save(ultimateBeneficiaryTypes);
        UltimateBeneficiaryTypesDTO result = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);
        ultimateBeneficiaryTypesSearchRepository.save(ultimateBeneficiaryTypes);
        return result;
    }

    @Override
    public Optional<UltimateBeneficiaryTypesDTO> partialUpdate(UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO) {
        log.debug("Request to partially update UltimateBeneficiaryTypes : {}", ultimateBeneficiaryTypesDTO);

        return ultimateBeneficiaryTypesRepository
            .findById(ultimateBeneficiaryTypesDTO.getId())
            .map(existingUltimateBeneficiaryTypes -> {
                ultimateBeneficiaryTypesMapper.partialUpdate(existingUltimateBeneficiaryTypes, ultimateBeneficiaryTypesDTO);

                return existingUltimateBeneficiaryTypes;
            })
            .map(ultimateBeneficiaryTypesRepository::save)
            .map(savedUltimateBeneficiaryTypes -> {
                ultimateBeneficiaryTypesSearchRepository.save(savedUltimateBeneficiaryTypes);

                return savedUltimateBeneficiaryTypes;
            })
            .map(ultimateBeneficiaryTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryTypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UltimateBeneficiaryTypes");
        return ultimateBeneficiaryTypesRepository.findAll(pageable).map(ultimateBeneficiaryTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UltimateBeneficiaryTypesDTO> findOne(Long id) {
        log.debug("Request to get UltimateBeneficiaryTypes : {}", id);
        return ultimateBeneficiaryTypesRepository.findById(id).map(ultimateBeneficiaryTypesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UltimateBeneficiaryTypes : {}", id);
        ultimateBeneficiaryTypesRepository.deleteById(id);
        ultimateBeneficiaryTypesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryTypesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UltimateBeneficiaryTypes for query {}", query);
        return ultimateBeneficiaryTypesSearchRepository.search(query, pageable).map(ultimateBeneficiaryTypesMapper::toDto);
    }
}
