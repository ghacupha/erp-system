package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
