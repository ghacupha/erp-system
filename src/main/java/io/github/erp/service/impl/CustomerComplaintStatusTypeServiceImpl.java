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

import io.github.erp.domain.CustomerComplaintStatusType;
import io.github.erp.repository.CustomerComplaintStatusTypeRepository;
import io.github.erp.repository.search.CustomerComplaintStatusTypeSearchRepository;
import io.github.erp.service.CustomerComplaintStatusTypeService;
import io.github.erp.service.dto.CustomerComplaintStatusTypeDTO;
import io.github.erp.service.mapper.CustomerComplaintStatusTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerComplaintStatusType}.
 */
@Service
@Transactional
public class CustomerComplaintStatusTypeServiceImpl implements CustomerComplaintStatusTypeService {

    private final Logger log = LoggerFactory.getLogger(CustomerComplaintStatusTypeServiceImpl.class);

    private final CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository;

    private final CustomerComplaintStatusTypeMapper customerComplaintStatusTypeMapper;

    private final CustomerComplaintStatusTypeSearchRepository customerComplaintStatusTypeSearchRepository;

    public CustomerComplaintStatusTypeServiceImpl(
        CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository,
        CustomerComplaintStatusTypeMapper customerComplaintStatusTypeMapper,
        CustomerComplaintStatusTypeSearchRepository customerComplaintStatusTypeSearchRepository
    ) {
        this.customerComplaintStatusTypeRepository = customerComplaintStatusTypeRepository;
        this.customerComplaintStatusTypeMapper = customerComplaintStatusTypeMapper;
        this.customerComplaintStatusTypeSearchRepository = customerComplaintStatusTypeSearchRepository;
    }

    @Override
    public CustomerComplaintStatusTypeDTO save(CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO) {
        log.debug("Request to save CustomerComplaintStatusType : {}", customerComplaintStatusTypeDTO);
        CustomerComplaintStatusType customerComplaintStatusType = customerComplaintStatusTypeMapper.toEntity(
            customerComplaintStatusTypeDTO
        );
        customerComplaintStatusType = customerComplaintStatusTypeRepository.save(customerComplaintStatusType);
        CustomerComplaintStatusTypeDTO result = customerComplaintStatusTypeMapper.toDto(customerComplaintStatusType);
        customerComplaintStatusTypeSearchRepository.save(customerComplaintStatusType);
        return result;
    }

    @Override
    public Optional<CustomerComplaintStatusTypeDTO> partialUpdate(CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO) {
        log.debug("Request to partially update CustomerComplaintStatusType : {}", customerComplaintStatusTypeDTO);

        return customerComplaintStatusTypeRepository
            .findById(customerComplaintStatusTypeDTO.getId())
            .map(existingCustomerComplaintStatusType -> {
                customerComplaintStatusTypeMapper.partialUpdate(existingCustomerComplaintStatusType, customerComplaintStatusTypeDTO);

                return existingCustomerComplaintStatusType;
            })
            .map(customerComplaintStatusTypeRepository::save)
            .map(savedCustomerComplaintStatusType -> {
                customerComplaintStatusTypeSearchRepository.save(savedCustomerComplaintStatusType);

                return savedCustomerComplaintStatusType;
            })
            .map(customerComplaintStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerComplaintStatusTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerComplaintStatusTypes");
        return customerComplaintStatusTypeRepository.findAll(pageable).map(customerComplaintStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerComplaintStatusTypeDTO> findOne(Long id) {
        log.debug("Request to get CustomerComplaintStatusType : {}", id);
        return customerComplaintStatusTypeRepository.findById(id).map(customerComplaintStatusTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerComplaintStatusType : {}", id);
        customerComplaintStatusTypeRepository.deleteById(id);
        customerComplaintStatusTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerComplaintStatusTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerComplaintStatusTypes for query {}", query);
        return customerComplaintStatusTypeSearchRepository.search(query, pageable).map(customerComplaintStatusTypeMapper::toDto);
    }
}
