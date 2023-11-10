package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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

import io.github.erp.domain.CustomerType;
import io.github.erp.repository.CustomerTypeRepository;
import io.github.erp.repository.search.CustomerTypeSearchRepository;
import io.github.erp.service.CustomerTypeService;
import io.github.erp.service.dto.CustomerTypeDTO;
import io.github.erp.service.mapper.CustomerTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerType}.
 */
@Service
@Transactional
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeServiceImpl.class);

    private final CustomerTypeRepository customerTypeRepository;

    private final CustomerTypeMapper customerTypeMapper;

    private final CustomerTypeSearchRepository customerTypeSearchRepository;

    public CustomerTypeServiceImpl(
        CustomerTypeRepository customerTypeRepository,
        CustomerTypeMapper customerTypeMapper,
        CustomerTypeSearchRepository customerTypeSearchRepository
    ) {
        this.customerTypeRepository = customerTypeRepository;
        this.customerTypeMapper = customerTypeMapper;
        this.customerTypeSearchRepository = customerTypeSearchRepository;
    }

    @Override
    public CustomerTypeDTO save(CustomerTypeDTO customerTypeDTO) {
        log.debug("Request to save CustomerType : {}", customerTypeDTO);
        CustomerType customerType = customerTypeMapper.toEntity(customerTypeDTO);
        customerType = customerTypeRepository.save(customerType);
        CustomerTypeDTO result = customerTypeMapper.toDto(customerType);
        customerTypeSearchRepository.save(customerType);
        return result;
    }

    @Override
    public Optional<CustomerTypeDTO> partialUpdate(CustomerTypeDTO customerTypeDTO) {
        log.debug("Request to partially update CustomerType : {}", customerTypeDTO);

        return customerTypeRepository
            .findById(customerTypeDTO.getId())
            .map(existingCustomerType -> {
                customerTypeMapper.partialUpdate(existingCustomerType, customerTypeDTO);

                return existingCustomerType;
            })
            .map(customerTypeRepository::save)
            .map(savedCustomerType -> {
                customerTypeSearchRepository.save(savedCustomerType);

                return savedCustomerType;
            })
            .map(customerTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerTypes");
        return customerTypeRepository.findAll(pageable).map(customerTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerTypeDTO> findOne(Long id) {
        log.debug("Request to get CustomerType : {}", id);
        return customerTypeRepository.findById(id).map(customerTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerType : {}", id);
        customerTypeRepository.deleteById(id);
        customerTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerTypes for query {}", query);
        return customerTypeSearchRepository.search(query, pageable).map(customerTypeMapper::toDto);
    }
}
