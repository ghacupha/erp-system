package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
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

import io.github.erp.domain.CustomerIDDocumentType;
import io.github.erp.repository.CustomerIDDocumentTypeRepository;
import io.github.erp.repository.search.CustomerIDDocumentTypeSearchRepository;
import io.github.erp.service.CustomerIDDocumentTypeService;
import io.github.erp.service.dto.CustomerIDDocumentTypeDTO;
import io.github.erp.service.mapper.CustomerIDDocumentTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerIDDocumentType}.
 */
@Service
@Transactional
public class CustomerIDDocumentTypeServiceImpl implements CustomerIDDocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(CustomerIDDocumentTypeServiceImpl.class);

    private final CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository;

    private final CustomerIDDocumentTypeMapper customerIDDocumentTypeMapper;

    private final CustomerIDDocumentTypeSearchRepository customerIDDocumentTypeSearchRepository;

    public CustomerIDDocumentTypeServiceImpl(
        CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository,
        CustomerIDDocumentTypeMapper customerIDDocumentTypeMapper,
        CustomerIDDocumentTypeSearchRepository customerIDDocumentTypeSearchRepository
    ) {
        this.customerIDDocumentTypeRepository = customerIDDocumentTypeRepository;
        this.customerIDDocumentTypeMapper = customerIDDocumentTypeMapper;
        this.customerIDDocumentTypeSearchRepository = customerIDDocumentTypeSearchRepository;
    }

    @Override
    public CustomerIDDocumentTypeDTO save(CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO) {
        log.debug("Request to save CustomerIDDocumentType : {}", customerIDDocumentTypeDTO);
        CustomerIDDocumentType customerIDDocumentType = customerIDDocumentTypeMapper.toEntity(customerIDDocumentTypeDTO);
        customerIDDocumentType = customerIDDocumentTypeRepository.save(customerIDDocumentType);
        CustomerIDDocumentTypeDTO result = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);
        customerIDDocumentTypeSearchRepository.save(customerIDDocumentType);
        return result;
    }

    @Override
    public Optional<CustomerIDDocumentTypeDTO> partialUpdate(CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO) {
        log.debug("Request to partially update CustomerIDDocumentType : {}", customerIDDocumentTypeDTO);

        return customerIDDocumentTypeRepository
            .findById(customerIDDocumentTypeDTO.getId())
            .map(existingCustomerIDDocumentType -> {
                customerIDDocumentTypeMapper.partialUpdate(existingCustomerIDDocumentType, customerIDDocumentTypeDTO);

                return existingCustomerIDDocumentType;
            })
            .map(customerIDDocumentTypeRepository::save)
            .map(savedCustomerIDDocumentType -> {
                customerIDDocumentTypeSearchRepository.save(savedCustomerIDDocumentType);

                return savedCustomerIDDocumentType;
            })
            .map(customerIDDocumentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerIDDocumentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerIDDocumentTypes");
        return customerIDDocumentTypeRepository.findAll(pageable).map(customerIDDocumentTypeMapper::toDto);
    }

    public Page<CustomerIDDocumentTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return customerIDDocumentTypeRepository.findAllWithEagerRelationships(pageable).map(customerIDDocumentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerIDDocumentTypeDTO> findOne(Long id) {
        log.debug("Request to get CustomerIDDocumentType : {}", id);
        return customerIDDocumentTypeRepository.findOneWithEagerRelationships(id).map(customerIDDocumentTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerIDDocumentType : {}", id);
        customerIDDocumentTypeRepository.deleteById(id);
        customerIDDocumentTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerIDDocumentTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerIDDocumentTypes for query {}", query);
        return customerIDDocumentTypeSearchRepository.search(query, pageable).map(customerIDDocumentTypeMapper::toDto);
    }
}
