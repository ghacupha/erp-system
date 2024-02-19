package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.CardFraudIncidentCategory;
import io.github.erp.repository.CardFraudIncidentCategoryRepository;
import io.github.erp.repository.search.CardFraudIncidentCategorySearchRepository;
import io.github.erp.service.CardFraudIncidentCategoryService;
import io.github.erp.service.dto.CardFraudIncidentCategoryDTO;
import io.github.erp.service.mapper.CardFraudIncidentCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardFraudIncidentCategory}.
 */
@Service
@Transactional
public class CardFraudIncidentCategoryServiceImpl implements CardFraudIncidentCategoryService {

    private final Logger log = LoggerFactory.getLogger(CardFraudIncidentCategoryServiceImpl.class);

    private final CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository;

    private final CardFraudIncidentCategoryMapper cardFraudIncidentCategoryMapper;

    private final CardFraudIncidentCategorySearchRepository cardFraudIncidentCategorySearchRepository;

    public CardFraudIncidentCategoryServiceImpl(
        CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository,
        CardFraudIncidentCategoryMapper cardFraudIncidentCategoryMapper,
        CardFraudIncidentCategorySearchRepository cardFraudIncidentCategorySearchRepository
    ) {
        this.cardFraudIncidentCategoryRepository = cardFraudIncidentCategoryRepository;
        this.cardFraudIncidentCategoryMapper = cardFraudIncidentCategoryMapper;
        this.cardFraudIncidentCategorySearchRepository = cardFraudIncidentCategorySearchRepository;
    }

    @Override
    public CardFraudIncidentCategoryDTO save(CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO) {
        log.debug("Request to save CardFraudIncidentCategory : {}", cardFraudIncidentCategoryDTO);
        CardFraudIncidentCategory cardFraudIncidentCategory = cardFraudIncidentCategoryMapper.toEntity(cardFraudIncidentCategoryDTO);
        cardFraudIncidentCategory = cardFraudIncidentCategoryRepository.save(cardFraudIncidentCategory);
        CardFraudIncidentCategoryDTO result = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);
        cardFraudIncidentCategorySearchRepository.save(cardFraudIncidentCategory);
        return result;
    }

    @Override
    public Optional<CardFraudIncidentCategoryDTO> partialUpdate(CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO) {
        log.debug("Request to partially update CardFraudIncidentCategory : {}", cardFraudIncidentCategoryDTO);

        return cardFraudIncidentCategoryRepository
            .findById(cardFraudIncidentCategoryDTO.getId())
            .map(existingCardFraudIncidentCategory -> {
                cardFraudIncidentCategoryMapper.partialUpdate(existingCardFraudIncidentCategory, cardFraudIncidentCategoryDTO);

                return existingCardFraudIncidentCategory;
            })
            .map(cardFraudIncidentCategoryRepository::save)
            .map(savedCardFraudIncidentCategory -> {
                cardFraudIncidentCategorySearchRepository.save(savedCardFraudIncidentCategory);

                return savedCardFraudIncidentCategory;
            })
            .map(cardFraudIncidentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardFraudIncidentCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardFraudIncidentCategories");
        return cardFraudIncidentCategoryRepository.findAll(pageable).map(cardFraudIncidentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardFraudIncidentCategoryDTO> findOne(Long id) {
        log.debug("Request to get CardFraudIncidentCategory : {}", id);
        return cardFraudIncidentCategoryRepository.findById(id).map(cardFraudIncidentCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardFraudIncidentCategory : {}", id);
        cardFraudIncidentCategoryRepository.deleteById(id);
        cardFraudIncidentCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardFraudIncidentCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardFraudIncidentCategories for query {}", query);
        return cardFraudIncidentCategorySearchRepository.search(query, pageable).map(cardFraudIncidentCategoryMapper::toDto);
    }
}
