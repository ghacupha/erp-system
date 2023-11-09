package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.domain.CardUsageInformation;
import io.github.erp.repository.CardUsageInformationRepository;
import io.github.erp.repository.search.CardUsageInformationSearchRepository;
import io.github.erp.service.CardUsageInformationService;
import io.github.erp.service.dto.CardUsageInformationDTO;
import io.github.erp.service.mapper.CardUsageInformationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardUsageInformation}.
 */
@Service
@Transactional
public class CardUsageInformationServiceImpl implements CardUsageInformationService {

    private final Logger log = LoggerFactory.getLogger(CardUsageInformationServiceImpl.class);

    private final CardUsageInformationRepository cardUsageInformationRepository;

    private final CardUsageInformationMapper cardUsageInformationMapper;

    private final CardUsageInformationSearchRepository cardUsageInformationSearchRepository;

    public CardUsageInformationServiceImpl(
        CardUsageInformationRepository cardUsageInformationRepository,
        CardUsageInformationMapper cardUsageInformationMapper,
        CardUsageInformationSearchRepository cardUsageInformationSearchRepository
    ) {
        this.cardUsageInformationRepository = cardUsageInformationRepository;
        this.cardUsageInformationMapper = cardUsageInformationMapper;
        this.cardUsageInformationSearchRepository = cardUsageInformationSearchRepository;
    }

    @Override
    public CardUsageInformationDTO save(CardUsageInformationDTO cardUsageInformationDTO) {
        log.debug("Request to save CardUsageInformation : {}", cardUsageInformationDTO);
        CardUsageInformation cardUsageInformation = cardUsageInformationMapper.toEntity(cardUsageInformationDTO);
        cardUsageInformation = cardUsageInformationRepository.save(cardUsageInformation);
        CardUsageInformationDTO result = cardUsageInformationMapper.toDto(cardUsageInformation);
        cardUsageInformationSearchRepository.save(cardUsageInformation);
        return result;
    }

    @Override
    public Optional<CardUsageInformationDTO> partialUpdate(CardUsageInformationDTO cardUsageInformationDTO) {
        log.debug("Request to partially update CardUsageInformation : {}", cardUsageInformationDTO);

        return cardUsageInformationRepository
            .findById(cardUsageInformationDTO.getId())
            .map(existingCardUsageInformation -> {
                cardUsageInformationMapper.partialUpdate(existingCardUsageInformation, cardUsageInformationDTO);

                return existingCardUsageInformation;
            })
            .map(cardUsageInformationRepository::save)
            .map(savedCardUsageInformation -> {
                cardUsageInformationSearchRepository.save(savedCardUsageInformation);

                return savedCardUsageInformation;
            })
            .map(cardUsageInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardUsageInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardUsageInformations");
        return cardUsageInformationRepository.findAll(pageable).map(cardUsageInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardUsageInformationDTO> findOne(Long id) {
        log.debug("Request to get CardUsageInformation : {}", id);
        return cardUsageInformationRepository.findById(id).map(cardUsageInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardUsageInformation : {}", id);
        cardUsageInformationRepository.deleteById(id);
        cardUsageInformationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardUsageInformationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardUsageInformations for query {}", query);
        return cardUsageInformationSearchRepository.search(query, pageable).map(cardUsageInformationMapper::toDto);
    }
}
