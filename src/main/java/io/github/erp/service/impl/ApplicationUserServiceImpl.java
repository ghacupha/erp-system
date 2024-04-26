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

import io.github.erp.domain.ApplicationUser;
import io.github.erp.repository.ApplicationUserRepository;
import io.github.erp.repository.search.ApplicationUserSearchRepository;
import io.github.erp.service.ApplicationUserService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserMapper applicationUserMapper;

    private final ApplicationUserSearchRepository applicationUserSearchRepository;

    public ApplicationUserServiceImpl(
        ApplicationUserRepository applicationUserRepository,
        ApplicationUserMapper applicationUserMapper,
        ApplicationUserSearchRepository applicationUserSearchRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
        this.applicationUserSearchRepository = applicationUserSearchRepository;
    }

    @Override
    public ApplicationUserDTO save(ApplicationUserDTO applicationUserDTO) {
        log.debug("Request to save ApplicationUser : {}", applicationUserDTO);
        ApplicationUser applicationUser = applicationUserMapper.toEntity(applicationUserDTO);
        applicationUser = applicationUserRepository.save(applicationUser);
        ApplicationUserDTO result = applicationUserMapper.toDto(applicationUser);
        applicationUserSearchRepository.save(applicationUser);
        return result;
    }

    @Override
    public Optional<ApplicationUserDTO> partialUpdate(ApplicationUserDTO applicationUserDTO) {
        log.debug("Request to partially update ApplicationUser : {}", applicationUserDTO);

        return applicationUserRepository
            .findById(applicationUserDTO.getId())
            .map(existingApplicationUser -> {
                applicationUserMapper.partialUpdate(existingApplicationUser, applicationUserDTO);

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save)
            .map(savedApplicationUser -> {
                applicationUserSearchRepository.save(savedApplicationUser);

                return savedApplicationUser;
            })
            .map(applicationUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll(pageable).map(applicationUserMapper::toDto);
    }

    public Page<ApplicationUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return applicationUserRepository.findAllWithEagerRelationships(pageable).map(applicationUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationUserDTO> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findOneWithEagerRelationships(id).map(applicationUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
        applicationUserSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ApplicationUsers for query {}", query);
        return applicationUserSearchRepository.search(query, pageable).map(applicationUserMapper::toDto);
    }
}
