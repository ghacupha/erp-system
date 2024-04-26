package io.github.erp.service;

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

import io.github.erp.domain.CardStatusFlag;
import io.github.erp.repository.CardStatusFlagRepository;
import io.github.erp.repository.search.CardStatusFlagSearchRepository;
import io.github.erp.service.dto.CardStatusFlagDTO;
import io.github.erp.service.mapper.CardStatusFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardStatusFlag}.
 */
@Service
@Transactional
public class CardStatusFlagService {

    private final Logger log = LoggerFactory.getLogger(CardStatusFlagService.class);

    private final CardStatusFlagRepository cardStatusFlagRepository;

    private final CardStatusFlagMapper cardStatusFlagMapper;

    private final CardStatusFlagSearchRepository cardStatusFlagSearchRepository;

    public CardStatusFlagService(
        CardStatusFlagRepository cardStatusFlagRepository,
        CardStatusFlagMapper cardStatusFlagMapper,
        CardStatusFlagSearchRepository cardStatusFlagSearchRepository
    ) {
        this.cardStatusFlagRepository = cardStatusFlagRepository;
        this.cardStatusFlagMapper = cardStatusFlagMapper;
        this.cardStatusFlagSearchRepository = cardStatusFlagSearchRepository;
    }

    /**
     * Save a cardStatusFlag.
     *
     * @param cardStatusFlagDTO the entity to save.
     * @return the persisted entity.
     */
    public CardStatusFlagDTO save(CardStatusFlagDTO cardStatusFlagDTO) {
        log.debug("Request to save CardStatusFlag : {}", cardStatusFlagDTO);
        CardStatusFlag cardStatusFlag = cardStatusFlagMapper.toEntity(cardStatusFlagDTO);
        cardStatusFlag = cardStatusFlagRepository.save(cardStatusFlag);
        CardStatusFlagDTO result = cardStatusFlagMapper.toDto(cardStatusFlag);
        cardStatusFlagSearchRepository.save(cardStatusFlag);
        return result;
    }

    /**
     * Partially update a cardStatusFlag.
     *
     * @param cardStatusFlagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CardStatusFlagDTO> partialUpdate(CardStatusFlagDTO cardStatusFlagDTO) {
        log.debug("Request to partially update CardStatusFlag : {}", cardStatusFlagDTO);

        return cardStatusFlagRepository
            .findById(cardStatusFlagDTO.getId())
            .map(existingCardStatusFlag -> {
                cardStatusFlagMapper.partialUpdate(existingCardStatusFlag, cardStatusFlagDTO);

                return existingCardStatusFlag;
            })
            .map(cardStatusFlagRepository::save)
            .map(savedCardStatusFlag -> {
                cardStatusFlagSearchRepository.save(savedCardStatusFlag);

                return savedCardStatusFlag;
            })
            .map(cardStatusFlagMapper::toDto);
    }

    /**
     * Get all the cardStatusFlags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CardStatusFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardStatusFlags");
        return cardStatusFlagRepository.findAll(pageable).map(cardStatusFlagMapper::toDto);
    }

    /**
     * Get one cardStatusFlag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CardStatusFlagDTO> findOne(Long id) {
        log.debug("Request to get CardStatusFlag : {}", id);
        return cardStatusFlagRepository.findById(id).map(cardStatusFlagMapper::toDto);
    }

    /**
     * Delete the cardStatusFlag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CardStatusFlag : {}", id);
        cardStatusFlagRepository.deleteById(id);
        cardStatusFlagSearchRepository.deleteById(id);
    }

    /**
     * Search for the cardStatusFlag corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CardStatusFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardStatusFlags for query {}", query);
        return cardStatusFlagSearchRepository.search(query, pageable).map(cardStatusFlagMapper::toDto);
    }
}
