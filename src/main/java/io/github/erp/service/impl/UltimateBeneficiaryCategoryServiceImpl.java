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

import io.github.erp.domain.UltimateBeneficiaryCategory;
import io.github.erp.repository.UltimateBeneficiaryCategoryRepository;
import io.github.erp.repository.search.UltimateBeneficiaryCategorySearchRepository;
import io.github.erp.service.UltimateBeneficiaryCategoryService;
import io.github.erp.service.dto.UltimateBeneficiaryCategoryDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UltimateBeneficiaryCategory}.
 */
@Service
@Transactional
public class UltimateBeneficiaryCategoryServiceImpl implements UltimateBeneficiaryCategoryService {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryCategoryServiceImpl.class);

    private final UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository;

    private final UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper;

    private final UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository;

    public UltimateBeneficiaryCategoryServiceImpl(
        UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository,
        UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper,
        UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository
    ) {
        this.ultimateBeneficiaryCategoryRepository = ultimateBeneficiaryCategoryRepository;
        this.ultimateBeneficiaryCategoryMapper = ultimateBeneficiaryCategoryMapper;
        this.ultimateBeneficiaryCategorySearchRepository = ultimateBeneficiaryCategorySearchRepository;
    }

    @Override
    public UltimateBeneficiaryCategoryDTO save(UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO) {
        log.debug("Request to save UltimateBeneficiaryCategory : {}", ultimateBeneficiaryCategoryDTO);
        UltimateBeneficiaryCategory ultimateBeneficiaryCategory = ultimateBeneficiaryCategoryMapper.toEntity(
            ultimateBeneficiaryCategoryDTO
        );
        ultimateBeneficiaryCategory = ultimateBeneficiaryCategoryRepository.save(ultimateBeneficiaryCategory);
        UltimateBeneficiaryCategoryDTO result = ultimateBeneficiaryCategoryMapper.toDto(ultimateBeneficiaryCategory);
        ultimateBeneficiaryCategorySearchRepository.save(ultimateBeneficiaryCategory);
        return result;
    }

    @Override
    public Optional<UltimateBeneficiaryCategoryDTO> partialUpdate(UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO) {
        log.debug("Request to partially update UltimateBeneficiaryCategory : {}", ultimateBeneficiaryCategoryDTO);

        return ultimateBeneficiaryCategoryRepository
            .findById(ultimateBeneficiaryCategoryDTO.getId())
            .map(existingUltimateBeneficiaryCategory -> {
                ultimateBeneficiaryCategoryMapper.partialUpdate(existingUltimateBeneficiaryCategory, ultimateBeneficiaryCategoryDTO);

                return existingUltimateBeneficiaryCategory;
            })
            .map(ultimateBeneficiaryCategoryRepository::save)
            .map(savedUltimateBeneficiaryCategory -> {
                ultimateBeneficiaryCategorySearchRepository.save(savedUltimateBeneficiaryCategory);

                return savedUltimateBeneficiaryCategory;
            })
            .map(ultimateBeneficiaryCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UltimateBeneficiaryCategories");
        return ultimateBeneficiaryCategoryRepository.findAll(pageable).map(ultimateBeneficiaryCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UltimateBeneficiaryCategoryDTO> findOne(Long id) {
        log.debug("Request to get UltimateBeneficiaryCategory : {}", id);
        return ultimateBeneficiaryCategoryRepository.findById(id).map(ultimateBeneficiaryCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UltimateBeneficiaryCategory : {}", id);
        ultimateBeneficiaryCategoryRepository.deleteById(id);
        ultimateBeneficiaryCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UltimateBeneficiaryCategories for query {}", query);
        return ultimateBeneficiaryCategorySearchRepository.search(query, pageable).map(ultimateBeneficiaryCategoryMapper::toDto);
    }
}
