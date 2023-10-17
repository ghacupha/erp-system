package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.CardAcquiringTransaction;
import io.github.erp.repository.CardAcquiringTransactionRepository;
import io.github.erp.repository.search.CardAcquiringTransactionSearchRepository;
import io.github.erp.service.CardAcquiringTransactionService;
import io.github.erp.service.dto.CardAcquiringTransactionDTO;
import io.github.erp.service.mapper.CardAcquiringTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardAcquiringTransaction}.
 */
@Service
@Transactional
public class CardAcquiringTransactionServiceImpl implements CardAcquiringTransactionService {

    private final Logger log = LoggerFactory.getLogger(CardAcquiringTransactionServiceImpl.class);

    private final CardAcquiringTransactionRepository cardAcquiringTransactionRepository;

    private final CardAcquiringTransactionMapper cardAcquiringTransactionMapper;

    private final CardAcquiringTransactionSearchRepository cardAcquiringTransactionSearchRepository;

    public CardAcquiringTransactionServiceImpl(
        CardAcquiringTransactionRepository cardAcquiringTransactionRepository,
        CardAcquiringTransactionMapper cardAcquiringTransactionMapper,
        CardAcquiringTransactionSearchRepository cardAcquiringTransactionSearchRepository
    ) {
        this.cardAcquiringTransactionRepository = cardAcquiringTransactionRepository;
        this.cardAcquiringTransactionMapper = cardAcquiringTransactionMapper;
        this.cardAcquiringTransactionSearchRepository = cardAcquiringTransactionSearchRepository;
    }

    @Override
    public CardAcquiringTransactionDTO save(CardAcquiringTransactionDTO cardAcquiringTransactionDTO) {
        log.debug("Request to save CardAcquiringTransaction : {}", cardAcquiringTransactionDTO);
        CardAcquiringTransaction cardAcquiringTransaction = cardAcquiringTransactionMapper.toEntity(cardAcquiringTransactionDTO);
        cardAcquiringTransaction = cardAcquiringTransactionRepository.save(cardAcquiringTransaction);
        CardAcquiringTransactionDTO result = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);
        cardAcquiringTransactionSearchRepository.save(cardAcquiringTransaction);
        return result;
    }

    @Override
    public Optional<CardAcquiringTransactionDTO> partialUpdate(CardAcquiringTransactionDTO cardAcquiringTransactionDTO) {
        log.debug("Request to partially update CardAcquiringTransaction : {}", cardAcquiringTransactionDTO);

        return cardAcquiringTransactionRepository
            .findById(cardAcquiringTransactionDTO.getId())
            .map(existingCardAcquiringTransaction -> {
                cardAcquiringTransactionMapper.partialUpdate(existingCardAcquiringTransaction, cardAcquiringTransactionDTO);

                return existingCardAcquiringTransaction;
            })
            .map(cardAcquiringTransactionRepository::save)
            .map(savedCardAcquiringTransaction -> {
                cardAcquiringTransactionSearchRepository.save(savedCardAcquiringTransaction);

                return savedCardAcquiringTransaction;
            })
            .map(cardAcquiringTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardAcquiringTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardAcquiringTransactions");
        return cardAcquiringTransactionRepository.findAll(pageable).map(cardAcquiringTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardAcquiringTransactionDTO> findOne(Long id) {
        log.debug("Request to get CardAcquiringTransaction : {}", id);
        return cardAcquiringTransactionRepository.findById(id).map(cardAcquiringTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardAcquiringTransaction : {}", id);
        cardAcquiringTransactionRepository.deleteById(id);
        cardAcquiringTransactionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardAcquiringTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardAcquiringTransactions for query {}", query);
        return cardAcquiringTransactionSearchRepository.search(query, pageable).map(cardAcquiringTransactionMapper::toDto);
    }
}
