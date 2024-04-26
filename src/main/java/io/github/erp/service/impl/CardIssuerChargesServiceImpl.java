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

import io.github.erp.domain.CardIssuerCharges;
import io.github.erp.repository.CardIssuerChargesRepository;
import io.github.erp.repository.search.CardIssuerChargesSearchRepository;
import io.github.erp.service.CardIssuerChargesService;
import io.github.erp.service.dto.CardIssuerChargesDTO;
import io.github.erp.service.mapper.CardIssuerChargesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardIssuerCharges}.
 */
@Service
@Transactional
public class CardIssuerChargesServiceImpl implements CardIssuerChargesService {

    private final Logger log = LoggerFactory.getLogger(CardIssuerChargesServiceImpl.class);

    private final CardIssuerChargesRepository cardIssuerChargesRepository;

    private final CardIssuerChargesMapper cardIssuerChargesMapper;

    private final CardIssuerChargesSearchRepository cardIssuerChargesSearchRepository;

    public CardIssuerChargesServiceImpl(
        CardIssuerChargesRepository cardIssuerChargesRepository,
        CardIssuerChargesMapper cardIssuerChargesMapper,
        CardIssuerChargesSearchRepository cardIssuerChargesSearchRepository
    ) {
        this.cardIssuerChargesRepository = cardIssuerChargesRepository;
        this.cardIssuerChargesMapper = cardIssuerChargesMapper;
        this.cardIssuerChargesSearchRepository = cardIssuerChargesSearchRepository;
    }

    @Override
    public CardIssuerChargesDTO save(CardIssuerChargesDTO cardIssuerChargesDTO) {
        log.debug("Request to save CardIssuerCharges : {}", cardIssuerChargesDTO);
        CardIssuerCharges cardIssuerCharges = cardIssuerChargesMapper.toEntity(cardIssuerChargesDTO);
        cardIssuerCharges = cardIssuerChargesRepository.save(cardIssuerCharges);
        CardIssuerChargesDTO result = cardIssuerChargesMapper.toDto(cardIssuerCharges);
        cardIssuerChargesSearchRepository.save(cardIssuerCharges);
        return result;
    }

    @Override
    public Optional<CardIssuerChargesDTO> partialUpdate(CardIssuerChargesDTO cardIssuerChargesDTO) {
        log.debug("Request to partially update CardIssuerCharges : {}", cardIssuerChargesDTO);

        return cardIssuerChargesRepository
            .findById(cardIssuerChargesDTO.getId())
            .map(existingCardIssuerCharges -> {
                cardIssuerChargesMapper.partialUpdate(existingCardIssuerCharges, cardIssuerChargesDTO);

                return existingCardIssuerCharges;
            })
            .map(cardIssuerChargesRepository::save)
            .map(savedCardIssuerCharges -> {
                cardIssuerChargesSearchRepository.save(savedCardIssuerCharges);

                return savedCardIssuerCharges;
            })
            .map(cardIssuerChargesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardIssuerChargesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardIssuerCharges");
        return cardIssuerChargesRepository.findAll(pageable).map(cardIssuerChargesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardIssuerChargesDTO> findOne(Long id) {
        log.debug("Request to get CardIssuerCharges : {}", id);
        return cardIssuerChargesRepository.findById(id).map(cardIssuerChargesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardIssuerCharges : {}", id);
        cardIssuerChargesRepository.deleteById(id);
        cardIssuerChargesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardIssuerChargesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardIssuerCharges for query {}", query);
        return cardIssuerChargesSearchRepository.search(query, pageable).map(cardIssuerChargesMapper::toDto);
    }
}
