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
