package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.domain.CardFraudInformation;
import io.github.erp.repository.CardFraudInformationRepository;
import io.github.erp.repository.search.CardFraudInformationSearchRepository;
import io.github.erp.service.CardFraudInformationService;
import io.github.erp.service.dto.CardFraudInformationDTO;
import io.github.erp.service.mapper.CardFraudInformationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardFraudInformation}.
 */
@Service
@Transactional
public class CardFraudInformationServiceImpl implements CardFraudInformationService {

    private final Logger log = LoggerFactory.getLogger(CardFraudInformationServiceImpl.class);

    private final CardFraudInformationRepository cardFraudInformationRepository;

    private final CardFraudInformationMapper cardFraudInformationMapper;

    private final CardFraudInformationSearchRepository cardFraudInformationSearchRepository;

    public CardFraudInformationServiceImpl(
        CardFraudInformationRepository cardFraudInformationRepository,
        CardFraudInformationMapper cardFraudInformationMapper,
        CardFraudInformationSearchRepository cardFraudInformationSearchRepository
    ) {
        this.cardFraudInformationRepository = cardFraudInformationRepository;
        this.cardFraudInformationMapper = cardFraudInformationMapper;
        this.cardFraudInformationSearchRepository = cardFraudInformationSearchRepository;
    }

    @Override
    public CardFraudInformationDTO save(CardFraudInformationDTO cardFraudInformationDTO) {
        log.debug("Request to save CardFraudInformation : {}", cardFraudInformationDTO);
        CardFraudInformation cardFraudInformation = cardFraudInformationMapper.toEntity(cardFraudInformationDTO);
        cardFraudInformation = cardFraudInformationRepository.save(cardFraudInformation);
        CardFraudInformationDTO result = cardFraudInformationMapper.toDto(cardFraudInformation);
        cardFraudInformationSearchRepository.save(cardFraudInformation);
        return result;
    }

    @Override
    public Optional<CardFraudInformationDTO> partialUpdate(CardFraudInformationDTO cardFraudInformationDTO) {
        log.debug("Request to partially update CardFraudInformation : {}", cardFraudInformationDTO);

        return cardFraudInformationRepository
            .findById(cardFraudInformationDTO.getId())
            .map(existingCardFraudInformation -> {
                cardFraudInformationMapper.partialUpdate(existingCardFraudInformation, cardFraudInformationDTO);

                return existingCardFraudInformation;
            })
            .map(cardFraudInformationRepository::save)
            .map(savedCardFraudInformation -> {
                cardFraudInformationSearchRepository.save(savedCardFraudInformation);

                return savedCardFraudInformation;
            })
            .map(cardFraudInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardFraudInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardFraudInformations");
        return cardFraudInformationRepository.findAll(pageable).map(cardFraudInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardFraudInformationDTO> findOne(Long id) {
        log.debug("Request to get CardFraudInformation : {}", id);
        return cardFraudInformationRepository.findById(id).map(cardFraudInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardFraudInformation : {}", id);
        cardFraudInformationRepository.deleteById(id);
        cardFraudInformationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardFraudInformationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardFraudInformations for query {}", query);
        return cardFraudInformationSearchRepository.search(query, pageable).map(cardFraudInformationMapper::toDto);
    }
}
