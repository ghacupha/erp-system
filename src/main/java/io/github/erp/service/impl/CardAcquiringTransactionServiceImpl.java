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
