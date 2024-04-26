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

import io.github.erp.domain.CardCategoryType;
import io.github.erp.repository.CardCategoryTypeRepository;
import io.github.erp.repository.search.CardCategoryTypeSearchRepository;
import io.github.erp.service.CardCategoryTypeService;
import io.github.erp.service.dto.CardCategoryTypeDTO;
import io.github.erp.service.mapper.CardCategoryTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardCategoryType}.
 */
@Service
@Transactional
public class CardCategoryTypeServiceImpl implements CardCategoryTypeService {

    private final Logger log = LoggerFactory.getLogger(CardCategoryTypeServiceImpl.class);

    private final CardCategoryTypeRepository cardCategoryTypeRepository;

    private final CardCategoryTypeMapper cardCategoryTypeMapper;

    private final CardCategoryTypeSearchRepository cardCategoryTypeSearchRepository;

    public CardCategoryTypeServiceImpl(
        CardCategoryTypeRepository cardCategoryTypeRepository,
        CardCategoryTypeMapper cardCategoryTypeMapper,
        CardCategoryTypeSearchRepository cardCategoryTypeSearchRepository
    ) {
        this.cardCategoryTypeRepository = cardCategoryTypeRepository;
        this.cardCategoryTypeMapper = cardCategoryTypeMapper;
        this.cardCategoryTypeSearchRepository = cardCategoryTypeSearchRepository;
    }

    @Override
    public CardCategoryTypeDTO save(CardCategoryTypeDTO cardCategoryTypeDTO) {
        log.debug("Request to save CardCategoryType : {}", cardCategoryTypeDTO);
        CardCategoryType cardCategoryType = cardCategoryTypeMapper.toEntity(cardCategoryTypeDTO);
        cardCategoryType = cardCategoryTypeRepository.save(cardCategoryType);
        CardCategoryTypeDTO result = cardCategoryTypeMapper.toDto(cardCategoryType);
        cardCategoryTypeSearchRepository.save(cardCategoryType);
        return result;
    }

    @Override
    public Optional<CardCategoryTypeDTO> partialUpdate(CardCategoryTypeDTO cardCategoryTypeDTO) {
        log.debug("Request to partially update CardCategoryType : {}", cardCategoryTypeDTO);

        return cardCategoryTypeRepository
            .findById(cardCategoryTypeDTO.getId())
            .map(existingCardCategoryType -> {
                cardCategoryTypeMapper.partialUpdate(existingCardCategoryType, cardCategoryTypeDTO);

                return existingCardCategoryType;
            })
            .map(cardCategoryTypeRepository::save)
            .map(savedCardCategoryType -> {
                cardCategoryTypeSearchRepository.save(savedCardCategoryType);

                return savedCardCategoryType;
            })
            .map(cardCategoryTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardCategoryTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardCategoryTypes");
        return cardCategoryTypeRepository.findAll(pageable).map(cardCategoryTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardCategoryTypeDTO> findOne(Long id) {
        log.debug("Request to get CardCategoryType : {}", id);
        return cardCategoryTypeRepository.findById(id).map(cardCategoryTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardCategoryType : {}", id);
        cardCategoryTypeRepository.deleteById(id);
        cardCategoryTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardCategoryTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardCategoryTypes for query {}", query);
        return cardCategoryTypeSearchRepository.search(query, pageable).map(cardCategoryTypeMapper::toDto);
    }
}
