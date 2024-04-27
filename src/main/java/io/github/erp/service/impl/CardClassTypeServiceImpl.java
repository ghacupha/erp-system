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

import io.github.erp.domain.CardClassType;
import io.github.erp.repository.CardClassTypeRepository;
import io.github.erp.repository.search.CardClassTypeSearchRepository;
import io.github.erp.service.CardClassTypeService;
import io.github.erp.service.dto.CardClassTypeDTO;
import io.github.erp.service.mapper.CardClassTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardClassType}.
 */
@Service
@Transactional
public class CardClassTypeServiceImpl implements CardClassTypeService {

    private final Logger log = LoggerFactory.getLogger(CardClassTypeServiceImpl.class);

    private final CardClassTypeRepository cardClassTypeRepository;

    private final CardClassTypeMapper cardClassTypeMapper;

    private final CardClassTypeSearchRepository cardClassTypeSearchRepository;

    public CardClassTypeServiceImpl(
        CardClassTypeRepository cardClassTypeRepository,
        CardClassTypeMapper cardClassTypeMapper,
        CardClassTypeSearchRepository cardClassTypeSearchRepository
    ) {
        this.cardClassTypeRepository = cardClassTypeRepository;
        this.cardClassTypeMapper = cardClassTypeMapper;
        this.cardClassTypeSearchRepository = cardClassTypeSearchRepository;
    }

    @Override
    public CardClassTypeDTO save(CardClassTypeDTO cardClassTypeDTO) {
        log.debug("Request to save CardClassType : {}", cardClassTypeDTO);
        CardClassType cardClassType = cardClassTypeMapper.toEntity(cardClassTypeDTO);
        cardClassType = cardClassTypeRepository.save(cardClassType);
        CardClassTypeDTO result = cardClassTypeMapper.toDto(cardClassType);
        cardClassTypeSearchRepository.save(cardClassType);
        return result;
    }

    @Override
    public Optional<CardClassTypeDTO> partialUpdate(CardClassTypeDTO cardClassTypeDTO) {
        log.debug("Request to partially update CardClassType : {}", cardClassTypeDTO);

        return cardClassTypeRepository
            .findById(cardClassTypeDTO.getId())
            .map(existingCardClassType -> {
                cardClassTypeMapper.partialUpdate(existingCardClassType, cardClassTypeDTO);

                return existingCardClassType;
            })
            .map(cardClassTypeRepository::save)
            .map(savedCardClassType -> {
                cardClassTypeSearchRepository.save(savedCardClassType);

                return savedCardClassType;
            })
            .map(cardClassTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardClassTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardClassTypes");
        return cardClassTypeRepository.findAll(pageable).map(cardClassTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardClassTypeDTO> findOne(Long id) {
        log.debug("Request to get CardClassType : {}", id);
        return cardClassTypeRepository.findById(id).map(cardClassTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardClassType : {}", id);
        cardClassTypeRepository.deleteById(id);
        cardClassTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardClassTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardClassTypes for query {}", query);
        return cardClassTypeSearchRepository.search(query, pageable).map(cardClassTypeMapper::toDto);
    }
}
