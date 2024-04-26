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

import io.github.erp.domain.CardTypes;
import io.github.erp.repository.CardTypesRepository;
import io.github.erp.repository.search.CardTypesSearchRepository;
import io.github.erp.service.CardTypesService;
import io.github.erp.service.dto.CardTypesDTO;
import io.github.erp.service.mapper.CardTypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardTypes}.
 */
@Service
@Transactional
public class CardTypesServiceImpl implements CardTypesService {

    private final Logger log = LoggerFactory.getLogger(CardTypesServiceImpl.class);

    private final CardTypesRepository cardTypesRepository;

    private final CardTypesMapper cardTypesMapper;

    private final CardTypesSearchRepository cardTypesSearchRepository;

    public CardTypesServiceImpl(
        CardTypesRepository cardTypesRepository,
        CardTypesMapper cardTypesMapper,
        CardTypesSearchRepository cardTypesSearchRepository
    ) {
        this.cardTypesRepository = cardTypesRepository;
        this.cardTypesMapper = cardTypesMapper;
        this.cardTypesSearchRepository = cardTypesSearchRepository;
    }

    @Override
    public CardTypesDTO save(CardTypesDTO cardTypesDTO) {
        log.debug("Request to save CardTypes : {}", cardTypesDTO);
        CardTypes cardTypes = cardTypesMapper.toEntity(cardTypesDTO);
        cardTypes = cardTypesRepository.save(cardTypes);
        CardTypesDTO result = cardTypesMapper.toDto(cardTypes);
        cardTypesSearchRepository.save(cardTypes);
        return result;
    }

    @Override
    public Optional<CardTypesDTO> partialUpdate(CardTypesDTO cardTypesDTO) {
        log.debug("Request to partially update CardTypes : {}", cardTypesDTO);

        return cardTypesRepository
            .findById(cardTypesDTO.getId())
            .map(existingCardTypes -> {
                cardTypesMapper.partialUpdate(existingCardTypes, cardTypesDTO);

                return existingCardTypes;
            })
            .map(cardTypesRepository::save)
            .map(savedCardTypes -> {
                cardTypesSearchRepository.save(savedCardTypes);

                return savedCardTypes;
            })
            .map(cardTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardTypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardTypes");
        return cardTypesRepository.findAll(pageable).map(cardTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardTypesDTO> findOne(Long id) {
        log.debug("Request to get CardTypes : {}", id);
        return cardTypesRepository.findById(id).map(cardTypesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardTypes : {}", id);
        cardTypesRepository.deleteById(id);
        cardTypesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardTypesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardTypes for query {}", query);
        return cardTypesSearchRepository.search(query, pageable).map(cardTypesMapper::toDto);
    }
}
