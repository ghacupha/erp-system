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

import io.github.erp.domain.CardState;
import io.github.erp.repository.CardStateRepository;
import io.github.erp.repository.search.CardStateSearchRepository;
import io.github.erp.service.CardStateService;
import io.github.erp.service.dto.CardStateDTO;
import io.github.erp.service.mapper.CardStateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardState}.
 */
@Service
@Transactional
public class CardStateServiceImpl implements CardStateService {

    private final Logger log = LoggerFactory.getLogger(CardStateServiceImpl.class);

    private final CardStateRepository cardStateRepository;

    private final CardStateMapper cardStateMapper;

    private final CardStateSearchRepository cardStateSearchRepository;

    public CardStateServiceImpl(
        CardStateRepository cardStateRepository,
        CardStateMapper cardStateMapper,
        CardStateSearchRepository cardStateSearchRepository
    ) {
        this.cardStateRepository = cardStateRepository;
        this.cardStateMapper = cardStateMapper;
        this.cardStateSearchRepository = cardStateSearchRepository;
    }

    @Override
    public CardStateDTO save(CardStateDTO cardStateDTO) {
        log.debug("Request to save CardState : {}", cardStateDTO);
        CardState cardState = cardStateMapper.toEntity(cardStateDTO);
        cardState = cardStateRepository.save(cardState);
        CardStateDTO result = cardStateMapper.toDto(cardState);
        cardStateSearchRepository.save(cardState);
        return result;
    }

    @Override
    public Optional<CardStateDTO> partialUpdate(CardStateDTO cardStateDTO) {
        log.debug("Request to partially update CardState : {}", cardStateDTO);

        return cardStateRepository
            .findById(cardStateDTO.getId())
            .map(existingCardState -> {
                cardStateMapper.partialUpdate(existingCardState, cardStateDTO);

                return existingCardState;
            })
            .map(cardStateRepository::save)
            .map(savedCardState -> {
                cardStateSearchRepository.save(savedCardState);

                return savedCardState;
            })
            .map(cardStateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardStateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardStates");
        return cardStateRepository.findAll(pageable).map(cardStateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardStateDTO> findOne(Long id) {
        log.debug("Request to get CardState : {}", id);
        return cardStateRepository.findById(id).map(cardStateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardState : {}", id);
        cardStateRepository.deleteById(id);
        cardStateSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardStateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardStates for query {}", query);
        return cardStateSearchRepository.search(query, pageable).map(cardStateMapper::toDto);
    }
}
