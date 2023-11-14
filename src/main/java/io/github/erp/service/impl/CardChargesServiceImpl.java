package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

import io.github.erp.domain.CardCharges;
import io.github.erp.repository.CardChargesRepository;
import io.github.erp.repository.search.CardChargesSearchRepository;
import io.github.erp.service.CardChargesService;
import io.github.erp.service.dto.CardChargesDTO;
import io.github.erp.service.mapper.CardChargesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardCharges}.
 */
@Service
@Transactional
public class CardChargesServiceImpl implements CardChargesService {

    private final Logger log = LoggerFactory.getLogger(CardChargesServiceImpl.class);

    private final CardChargesRepository cardChargesRepository;

    private final CardChargesMapper cardChargesMapper;

    private final CardChargesSearchRepository cardChargesSearchRepository;

    public CardChargesServiceImpl(
        CardChargesRepository cardChargesRepository,
        CardChargesMapper cardChargesMapper,
        CardChargesSearchRepository cardChargesSearchRepository
    ) {
        this.cardChargesRepository = cardChargesRepository;
        this.cardChargesMapper = cardChargesMapper;
        this.cardChargesSearchRepository = cardChargesSearchRepository;
    }

    @Override
    public CardChargesDTO save(CardChargesDTO cardChargesDTO) {
        log.debug("Request to save CardCharges : {}", cardChargesDTO);
        CardCharges cardCharges = cardChargesMapper.toEntity(cardChargesDTO);
        cardCharges = cardChargesRepository.save(cardCharges);
        CardChargesDTO result = cardChargesMapper.toDto(cardCharges);
        cardChargesSearchRepository.save(cardCharges);
        return result;
    }

    @Override
    public Optional<CardChargesDTO> partialUpdate(CardChargesDTO cardChargesDTO) {
        log.debug("Request to partially update CardCharges : {}", cardChargesDTO);

        return cardChargesRepository
            .findById(cardChargesDTO.getId())
            .map(existingCardCharges -> {
                cardChargesMapper.partialUpdate(existingCardCharges, cardChargesDTO);

                return existingCardCharges;
            })
            .map(cardChargesRepository::save)
            .map(savedCardCharges -> {
                cardChargesSearchRepository.save(savedCardCharges);

                return savedCardCharges;
            })
            .map(cardChargesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardChargesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardCharges");
        return cardChargesRepository.findAll(pageable).map(cardChargesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardChargesDTO> findOne(Long id) {
        log.debug("Request to get CardCharges : {}", id);
        return cardChargesRepository.findById(id).map(cardChargesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardCharges : {}", id);
        cardChargesRepository.deleteById(id);
        cardChargesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardChargesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardCharges for query {}", query);
        return cardChargesSearchRepository.search(query, pageable).map(cardChargesMapper::toDto);
    }
}
