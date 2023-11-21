package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
