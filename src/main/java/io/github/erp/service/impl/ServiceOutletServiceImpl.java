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

import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.ServiceOutletRepository;
import io.github.erp.repository.search.ServiceOutletSearchRepository;
import io.github.erp.service.ServiceOutletService;
import io.github.erp.service.dto.ServiceOutletDTO;
import io.github.erp.service.mapper.ServiceOutletMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ServiceOutlet}.
 */
@Service
@Transactional
public class ServiceOutletServiceImpl implements ServiceOutletService {

    private final Logger log = LoggerFactory.getLogger(ServiceOutletServiceImpl.class);

    private final ServiceOutletRepository serviceOutletRepository;

    private final ServiceOutletMapper serviceOutletMapper;

    private final ServiceOutletSearchRepository serviceOutletSearchRepository;

    public ServiceOutletServiceImpl(
        ServiceOutletRepository serviceOutletRepository,
        ServiceOutletMapper serviceOutletMapper,
        ServiceOutletSearchRepository serviceOutletSearchRepository
    ) {
        this.serviceOutletRepository = serviceOutletRepository;
        this.serviceOutletMapper = serviceOutletMapper;
        this.serviceOutletSearchRepository = serviceOutletSearchRepository;
    }

    @Override
    public ServiceOutletDTO save(ServiceOutletDTO serviceOutletDTO) {
        log.debug("Request to save ServiceOutlet : {}", serviceOutletDTO);
        ServiceOutlet serviceOutlet = serviceOutletMapper.toEntity(serviceOutletDTO);
        serviceOutlet = serviceOutletRepository.save(serviceOutlet);
        ServiceOutletDTO result = serviceOutletMapper.toDto(serviceOutlet);
        serviceOutletSearchRepository.save(serviceOutlet);
        return result;
    }

    @Override
    public Optional<ServiceOutletDTO> partialUpdate(ServiceOutletDTO serviceOutletDTO) {
        log.debug("Request to partially update ServiceOutlet : {}", serviceOutletDTO);

        return serviceOutletRepository
            .findById(serviceOutletDTO.getId())
            .map(existingServiceOutlet -> {
                serviceOutletMapper.partialUpdate(existingServiceOutlet, serviceOutletDTO);

                return existingServiceOutlet;
            })
            .map(serviceOutletRepository::save)
            .map(savedServiceOutlet -> {
                serviceOutletSearchRepository.save(savedServiceOutlet);

                return savedServiceOutlet;
            })
            .map(serviceOutletMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceOutletDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceOutlets");
        return serviceOutletRepository.findAll(pageable).map(serviceOutletMapper::toDto);
    }

    public Page<ServiceOutletDTO> findAllWithEagerRelationships(Pageable pageable) {
        return serviceOutletRepository.findAllWithEagerRelationships(pageable).map(serviceOutletMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceOutletDTO> findOne(Long id) {
        log.debug("Request to get ServiceOutlet : {}", id);
        return serviceOutletRepository.findOneWithEagerRelationships(id).map(serviceOutletMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceOutlet : {}", id);
        serviceOutletRepository.deleteById(id);
        serviceOutletSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceOutletDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceOutlets for query {}", query);
        return serviceOutletSearchRepository.search(query, pageable).map(serviceOutletMapper::toDto);
    }
}
