package io.github.erp.service.impl;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.CardBrandType;
import io.github.erp.repository.CardBrandTypeRepository;
import io.github.erp.repository.search.CardBrandTypeSearchRepository;
import io.github.erp.service.CardBrandTypeService;
import io.github.erp.service.dto.CardBrandTypeDTO;
import io.github.erp.service.mapper.CardBrandTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardBrandType}.
 */
@Service
@Transactional
public class CardBrandTypeServiceImpl implements CardBrandTypeService {

    private final Logger log = LoggerFactory.getLogger(CardBrandTypeServiceImpl.class);

    private final CardBrandTypeRepository cardBrandTypeRepository;

    private final CardBrandTypeMapper cardBrandTypeMapper;

    private final CardBrandTypeSearchRepository cardBrandTypeSearchRepository;

    public CardBrandTypeServiceImpl(
        CardBrandTypeRepository cardBrandTypeRepository,
        CardBrandTypeMapper cardBrandTypeMapper,
        CardBrandTypeSearchRepository cardBrandTypeSearchRepository
    ) {
        this.cardBrandTypeRepository = cardBrandTypeRepository;
        this.cardBrandTypeMapper = cardBrandTypeMapper;
        this.cardBrandTypeSearchRepository = cardBrandTypeSearchRepository;
    }

    @Override
    public CardBrandTypeDTO save(CardBrandTypeDTO cardBrandTypeDTO) {
        log.debug("Request to save CardBrandType : {}", cardBrandTypeDTO);
        CardBrandType cardBrandType = cardBrandTypeMapper.toEntity(cardBrandTypeDTO);
        cardBrandType = cardBrandTypeRepository.save(cardBrandType);
        CardBrandTypeDTO result = cardBrandTypeMapper.toDto(cardBrandType);
        cardBrandTypeSearchRepository.save(cardBrandType);
        return result;
    }

    @Override
    public Optional<CardBrandTypeDTO> partialUpdate(CardBrandTypeDTO cardBrandTypeDTO) {
        log.debug("Request to partially update CardBrandType : {}", cardBrandTypeDTO);

        return cardBrandTypeRepository
            .findById(cardBrandTypeDTO.getId())
            .map(existingCardBrandType -> {
                cardBrandTypeMapper.partialUpdate(existingCardBrandType, cardBrandTypeDTO);

                return existingCardBrandType;
            })
            .map(cardBrandTypeRepository::save)
            .map(savedCardBrandType -> {
                cardBrandTypeSearchRepository.save(savedCardBrandType);

                return savedCardBrandType;
            })
            .map(cardBrandTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardBrandTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardBrandTypes");
        return cardBrandTypeRepository.findAll(pageable).map(cardBrandTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardBrandTypeDTO> findOne(Long id) {
        log.debug("Request to get CardBrandType : {}", id);
        return cardBrandTypeRepository.findById(id).map(cardBrandTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardBrandType : {}", id);
        cardBrandTypeRepository.deleteById(id);
        cardBrandTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardBrandTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardBrandTypes for query {}", query);
        return cardBrandTypeSearchRepository.search(query, pageable).map(cardBrandTypeMapper::toDto);
    }
}
