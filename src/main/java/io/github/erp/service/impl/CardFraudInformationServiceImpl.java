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
