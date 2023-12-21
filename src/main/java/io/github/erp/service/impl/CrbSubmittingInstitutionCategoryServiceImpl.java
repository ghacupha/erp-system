package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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

import io.github.erp.domain.CrbSubmittingInstitutionCategory;
import io.github.erp.repository.CrbSubmittingInstitutionCategoryRepository;
import io.github.erp.repository.search.CrbSubmittingInstitutionCategorySearchRepository;
import io.github.erp.service.CrbSubmittingInstitutionCategoryService;
import io.github.erp.service.dto.CrbSubmittingInstitutionCategoryDTO;
import io.github.erp.service.mapper.CrbSubmittingInstitutionCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbSubmittingInstitutionCategory}.
 */
@Service
@Transactional
public class CrbSubmittingInstitutionCategoryServiceImpl implements CrbSubmittingInstitutionCategoryService {

    private final Logger log = LoggerFactory.getLogger(CrbSubmittingInstitutionCategoryServiceImpl.class);

    private final CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository;

    private final CrbSubmittingInstitutionCategoryMapper crbSubmittingInstitutionCategoryMapper;

    private final CrbSubmittingInstitutionCategorySearchRepository crbSubmittingInstitutionCategorySearchRepository;

    public CrbSubmittingInstitutionCategoryServiceImpl(
        CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository,
        CrbSubmittingInstitutionCategoryMapper crbSubmittingInstitutionCategoryMapper,
        CrbSubmittingInstitutionCategorySearchRepository crbSubmittingInstitutionCategorySearchRepository
    ) {
        this.crbSubmittingInstitutionCategoryRepository = crbSubmittingInstitutionCategoryRepository;
        this.crbSubmittingInstitutionCategoryMapper = crbSubmittingInstitutionCategoryMapper;
        this.crbSubmittingInstitutionCategorySearchRepository = crbSubmittingInstitutionCategorySearchRepository;
    }

    @Override
    public CrbSubmittingInstitutionCategoryDTO save(CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO) {
        log.debug("Request to save CrbSubmittingInstitutionCategory : {}", crbSubmittingInstitutionCategoryDTO);
        CrbSubmittingInstitutionCategory crbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryMapper.toEntity(
            crbSubmittingInstitutionCategoryDTO
        );
        crbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryRepository.save(crbSubmittingInstitutionCategory);
        CrbSubmittingInstitutionCategoryDTO result = crbSubmittingInstitutionCategoryMapper.toDto(crbSubmittingInstitutionCategory);
        crbSubmittingInstitutionCategorySearchRepository.save(crbSubmittingInstitutionCategory);
        return result;
    }

    @Override
    public Optional<CrbSubmittingInstitutionCategoryDTO> partialUpdate(
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO
    ) {
        log.debug("Request to partially update CrbSubmittingInstitutionCategory : {}", crbSubmittingInstitutionCategoryDTO);

        return crbSubmittingInstitutionCategoryRepository
            .findById(crbSubmittingInstitutionCategoryDTO.getId())
            .map(existingCrbSubmittingInstitutionCategory -> {
                crbSubmittingInstitutionCategoryMapper.partialUpdate(
                    existingCrbSubmittingInstitutionCategory,
                    crbSubmittingInstitutionCategoryDTO
                );

                return existingCrbSubmittingInstitutionCategory;
            })
            .map(crbSubmittingInstitutionCategoryRepository::save)
            .map(savedCrbSubmittingInstitutionCategory -> {
                crbSubmittingInstitutionCategorySearchRepository.save(savedCrbSubmittingInstitutionCategory);

                return savedCrbSubmittingInstitutionCategory;
            })
            .map(crbSubmittingInstitutionCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbSubmittingInstitutionCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbSubmittingInstitutionCategories");
        return crbSubmittingInstitutionCategoryRepository.findAll(pageable).map(crbSubmittingInstitutionCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbSubmittingInstitutionCategoryDTO> findOne(Long id) {
        log.debug("Request to get CrbSubmittingInstitutionCategory : {}", id);
        return crbSubmittingInstitutionCategoryRepository.findById(id).map(crbSubmittingInstitutionCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbSubmittingInstitutionCategory : {}", id);
        crbSubmittingInstitutionCategoryRepository.deleteById(id);
        crbSubmittingInstitutionCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbSubmittingInstitutionCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbSubmittingInstitutionCategories for query {}", query);
        return crbSubmittingInstitutionCategorySearchRepository.search(query, pageable).map(crbSubmittingInstitutionCategoryMapper::toDto);
    }
}
