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
