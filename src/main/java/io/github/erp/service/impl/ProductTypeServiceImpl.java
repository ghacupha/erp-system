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

import io.github.erp.domain.ProductType;
import io.github.erp.repository.ProductTypeRepository;
import io.github.erp.repository.search.ProductTypeSearchRepository;
import io.github.erp.service.ProductTypeService;
import io.github.erp.service.dto.ProductTypeDTO;
import io.github.erp.service.mapper.ProductTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductType}.
 */
@Service
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService {

    private final Logger log = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeServiceImpl(
        ProductTypeRepository productTypeRepository,
        ProductTypeMapper productTypeMapper,
        ProductTypeSearchRepository productTypeSearchRepository
    ) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    @Override
    public ProductTypeDTO save(ProductTypeDTO productTypeDTO) {
        log.debug("Request to save ProductType : {}", productTypeDTO);
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        ProductTypeDTO result = productTypeMapper.toDto(productType);
        productTypeSearchRepository.save(productType);
        return result;
    }

    @Override
    public Optional<ProductTypeDTO> partialUpdate(ProductTypeDTO productTypeDTO) {
        log.debug("Request to partially update ProductType : {}", productTypeDTO);

        return productTypeRepository
            .findById(productTypeDTO.getId())
            .map(existingProductType -> {
                productTypeMapper.partialUpdate(existingProductType, productTypeDTO);

                return existingProductType;
            })
            .map(productTypeRepository::save)
            .map(savedProductType -> {
                productTypeSearchRepository.save(savedProductType);

                return savedProductType;
            })
            .map(productTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductTypes");
        return productTypeRepository.findAll(pageable).map(productTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductTypeDTO> findOne(Long id) {
        log.debug("Request to get ProductType : {}", id);
        return productTypeRepository.findById(id).map(productTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductType : {}", id);
        productTypeRepository.deleteById(id);
        productTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductTypes for query {}", query);
        return productTypeSearchRepository.search(query, pageable).map(productTypeMapper::toDto);
    }
}
