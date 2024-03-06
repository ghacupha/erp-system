package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
