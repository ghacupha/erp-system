package io.github.erp.service;

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

import io.github.erp.domain.BouncedChequeCategories;
import io.github.erp.repository.BouncedChequeCategoriesRepository;
import io.github.erp.repository.search.BouncedChequeCategoriesSearchRepository;
import io.github.erp.service.dto.BouncedChequeCategoriesDTO;
import io.github.erp.service.mapper.BouncedChequeCategoriesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BouncedChequeCategories}.
 */
@Service
@Transactional
public class BouncedChequeCategoriesService {

    private final Logger log = LoggerFactory.getLogger(BouncedChequeCategoriesService.class);

    private final BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository;

    private final BouncedChequeCategoriesMapper bouncedChequeCategoriesMapper;

    private final BouncedChequeCategoriesSearchRepository bouncedChequeCategoriesSearchRepository;

    public BouncedChequeCategoriesService(
        BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository,
        BouncedChequeCategoriesMapper bouncedChequeCategoriesMapper,
        BouncedChequeCategoriesSearchRepository bouncedChequeCategoriesSearchRepository
    ) {
        this.bouncedChequeCategoriesRepository = bouncedChequeCategoriesRepository;
        this.bouncedChequeCategoriesMapper = bouncedChequeCategoriesMapper;
        this.bouncedChequeCategoriesSearchRepository = bouncedChequeCategoriesSearchRepository;
    }

    /**
     * Save a bouncedChequeCategories.
     *
     * @param bouncedChequeCategoriesDTO the entity to save.
     * @return the persisted entity.
     */
    public BouncedChequeCategoriesDTO save(BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO) {
        log.debug("Request to save BouncedChequeCategories : {}", bouncedChequeCategoriesDTO);
        BouncedChequeCategories bouncedChequeCategories = bouncedChequeCategoriesMapper.toEntity(bouncedChequeCategoriesDTO);
        bouncedChequeCategories = bouncedChequeCategoriesRepository.save(bouncedChequeCategories);
        BouncedChequeCategoriesDTO result = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);
        bouncedChequeCategoriesSearchRepository.save(bouncedChequeCategories);
        return result;
    }

    /**
     * Partially update a bouncedChequeCategories.
     *
     * @param bouncedChequeCategoriesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BouncedChequeCategoriesDTO> partialUpdate(BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO) {
        log.debug("Request to partially update BouncedChequeCategories : {}", bouncedChequeCategoriesDTO);

        return bouncedChequeCategoriesRepository
            .findById(bouncedChequeCategoriesDTO.getId())
            .map(existingBouncedChequeCategories -> {
                bouncedChequeCategoriesMapper.partialUpdate(existingBouncedChequeCategories, bouncedChequeCategoriesDTO);

                return existingBouncedChequeCategories;
            })
            .map(bouncedChequeCategoriesRepository::save)
            .map(savedBouncedChequeCategories -> {
                bouncedChequeCategoriesSearchRepository.save(savedBouncedChequeCategories);

                return savedBouncedChequeCategories;
            })
            .map(bouncedChequeCategoriesMapper::toDto);
    }

    /**
     * Get all the bouncedChequeCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BouncedChequeCategoriesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BouncedChequeCategories");
        return bouncedChequeCategoriesRepository.findAll(pageable).map(bouncedChequeCategoriesMapper::toDto);
    }

    /**
     * Get one bouncedChequeCategories by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BouncedChequeCategoriesDTO> findOne(Long id) {
        log.debug("Request to get BouncedChequeCategories : {}", id);
        return bouncedChequeCategoriesRepository.findById(id).map(bouncedChequeCategoriesMapper::toDto);
    }

    /**
     * Delete the bouncedChequeCategories by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BouncedChequeCategories : {}", id);
        bouncedChequeCategoriesRepository.deleteById(id);
        bouncedChequeCategoriesSearchRepository.deleteById(id);
    }

    /**
     * Search for the bouncedChequeCategories corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BouncedChequeCategoriesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BouncedChequeCategories for query {}", query);
        return bouncedChequeCategoriesSearchRepository.search(query, pageable).map(bouncedChequeCategoriesMapper::toDto);
    }
}
