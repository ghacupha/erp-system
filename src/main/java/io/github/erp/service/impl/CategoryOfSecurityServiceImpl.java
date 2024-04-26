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

import io.github.erp.domain.CategoryOfSecurity;
import io.github.erp.repository.CategoryOfSecurityRepository;
import io.github.erp.repository.search.CategoryOfSecuritySearchRepository;
import io.github.erp.service.CategoryOfSecurityService;
import io.github.erp.service.dto.CategoryOfSecurityDTO;
import io.github.erp.service.mapper.CategoryOfSecurityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoryOfSecurity}.
 */
@Service
@Transactional
public class CategoryOfSecurityServiceImpl implements CategoryOfSecurityService {

    private final Logger log = LoggerFactory.getLogger(CategoryOfSecurityServiceImpl.class);

    private final CategoryOfSecurityRepository categoryOfSecurityRepository;

    private final CategoryOfSecurityMapper categoryOfSecurityMapper;

    private final CategoryOfSecuritySearchRepository categoryOfSecuritySearchRepository;

    public CategoryOfSecurityServiceImpl(
        CategoryOfSecurityRepository categoryOfSecurityRepository,
        CategoryOfSecurityMapper categoryOfSecurityMapper,
        CategoryOfSecuritySearchRepository categoryOfSecuritySearchRepository
    ) {
        this.categoryOfSecurityRepository = categoryOfSecurityRepository;
        this.categoryOfSecurityMapper = categoryOfSecurityMapper;
        this.categoryOfSecuritySearchRepository = categoryOfSecuritySearchRepository;
    }

    @Override
    public CategoryOfSecurityDTO save(CategoryOfSecurityDTO categoryOfSecurityDTO) {
        log.debug("Request to save CategoryOfSecurity : {}", categoryOfSecurityDTO);
        CategoryOfSecurity categoryOfSecurity = categoryOfSecurityMapper.toEntity(categoryOfSecurityDTO);
        categoryOfSecurity = categoryOfSecurityRepository.save(categoryOfSecurity);
        CategoryOfSecurityDTO result = categoryOfSecurityMapper.toDto(categoryOfSecurity);
        categoryOfSecuritySearchRepository.save(categoryOfSecurity);
        return result;
    }

    @Override
    public Optional<CategoryOfSecurityDTO> partialUpdate(CategoryOfSecurityDTO categoryOfSecurityDTO) {
        log.debug("Request to partially update CategoryOfSecurity : {}", categoryOfSecurityDTO);

        return categoryOfSecurityRepository
            .findById(categoryOfSecurityDTO.getId())
            .map(existingCategoryOfSecurity -> {
                categoryOfSecurityMapper.partialUpdate(existingCategoryOfSecurity, categoryOfSecurityDTO);

                return existingCategoryOfSecurity;
            })
            .map(categoryOfSecurityRepository::save)
            .map(savedCategoryOfSecurity -> {
                categoryOfSecuritySearchRepository.save(savedCategoryOfSecurity);

                return savedCategoryOfSecurity;
            })
            .map(categoryOfSecurityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryOfSecurityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CategoryOfSecurities");
        return categoryOfSecurityRepository.findAll(pageable).map(categoryOfSecurityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryOfSecurityDTO> findOne(Long id) {
        log.debug("Request to get CategoryOfSecurity : {}", id);
        return categoryOfSecurityRepository.findById(id).map(categoryOfSecurityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoryOfSecurity : {}", id);
        categoryOfSecurityRepository.deleteById(id);
        categoryOfSecuritySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryOfSecurityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CategoryOfSecurities for query {}", query);
        return categoryOfSecuritySearchRepository.search(query, pageable).map(categoryOfSecurityMapper::toDto);
    }
}
