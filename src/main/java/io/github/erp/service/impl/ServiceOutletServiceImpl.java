package io.github.erp.service.impl;

/*-
 * Erp System - Mark I Ver 1 (Artaxerxes)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
