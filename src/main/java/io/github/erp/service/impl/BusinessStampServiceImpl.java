package io.github.erp.service.impl;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.BusinessStamp;
import io.github.erp.repository.BusinessStampRepository;
import io.github.erp.repository.search.BusinessStampSearchRepository;
import io.github.erp.service.BusinessStampService;
import io.github.erp.service.dto.BusinessStampDTO;
import io.github.erp.service.mapper.BusinessStampMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusinessStamp}.
 */
@Service
@Transactional
public class BusinessStampServiceImpl implements BusinessStampService {

    private final Logger log = LoggerFactory.getLogger(BusinessStampServiceImpl.class);

    private final BusinessStampRepository businessStampRepository;

    private final BusinessStampMapper businessStampMapper;

    private final BusinessStampSearchRepository businessStampSearchRepository;

    public BusinessStampServiceImpl(
        BusinessStampRepository businessStampRepository,
        BusinessStampMapper businessStampMapper,
        BusinessStampSearchRepository businessStampSearchRepository
    ) {
        this.businessStampRepository = businessStampRepository;
        this.businessStampMapper = businessStampMapper;
        this.businessStampSearchRepository = businessStampSearchRepository;
    }

    @Override
    public BusinessStampDTO save(BusinessStampDTO businessStampDTO) {
        log.debug("Request to save BusinessStamp : {}", businessStampDTO);
        BusinessStamp businessStamp = businessStampMapper.toEntity(businessStampDTO);
        businessStamp = businessStampRepository.save(businessStamp);
        BusinessStampDTO result = businessStampMapper.toDto(businessStamp);
        businessStampSearchRepository.save(businessStamp);
        return result;
    }

    @Override
    public Optional<BusinessStampDTO> partialUpdate(BusinessStampDTO businessStampDTO) {
        log.debug("Request to partially update BusinessStamp : {}", businessStampDTO);

        return businessStampRepository
            .findById(businessStampDTO.getId())
            .map(existingBusinessStamp -> {
                businessStampMapper.partialUpdate(existingBusinessStamp, businessStampDTO);

                return existingBusinessStamp;
            })
            .map(businessStampRepository::save)
            .map(savedBusinessStamp -> {
                businessStampSearchRepository.save(savedBusinessStamp);

                return savedBusinessStamp;
            })
            .map(businessStampMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessStampDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessStamps");
        return businessStampRepository.findAll(pageable).map(businessStampMapper::toDto);
    }

    public Page<BusinessStampDTO> findAllWithEagerRelationships(Pageable pageable) {
        return businessStampRepository.findAllWithEagerRelationships(pageable).map(businessStampMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessStampDTO> findOne(Long id) {
        log.debug("Request to get BusinessStamp : {}", id);
        return businessStampRepository.findOneWithEagerRelationships(id).map(businessStampMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessStamp : {}", id);
        businessStampRepository.deleteById(id);
        businessStampSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessStampDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessStamps for query {}", query);
        return businessStampSearchRepository.search(query, pageable).map(businessStampMapper::toDto);
    }
}
