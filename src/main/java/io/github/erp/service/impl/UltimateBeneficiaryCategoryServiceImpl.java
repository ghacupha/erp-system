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
