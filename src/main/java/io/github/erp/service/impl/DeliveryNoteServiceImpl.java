package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.DeliveryNote;
import io.github.erp.repository.DeliveryNoteRepository;
import io.github.erp.repository.search.DeliveryNoteSearchRepository;
import io.github.erp.service.DeliveryNoteService;
import io.github.erp.service.dto.DeliveryNoteDTO;
import io.github.erp.service.mapper.DeliveryNoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryNote}.
 */
@Service
@Transactional
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteServiceImpl.class);

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteMapper deliveryNoteMapper;

    private final DeliveryNoteSearchRepository deliveryNoteSearchRepository;

    public DeliveryNoteServiceImpl(
        DeliveryNoteRepository deliveryNoteRepository,
        DeliveryNoteMapper deliveryNoteMapper,
        DeliveryNoteSearchRepository deliveryNoteSearchRepository
    ) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteMapper = deliveryNoteMapper;
        this.deliveryNoteSearchRepository = deliveryNoteSearchRepository;
    }

    @Override
    public DeliveryNoteDTO save(DeliveryNoteDTO deliveryNoteDTO) {
        log.debug("Request to save DeliveryNote : {}", deliveryNoteDTO);
        DeliveryNote deliveryNote = deliveryNoteMapper.toEntity(deliveryNoteDTO);
        deliveryNote = deliveryNoteRepository.save(deliveryNote);
        DeliveryNoteDTO result = deliveryNoteMapper.toDto(deliveryNote);
        deliveryNoteSearchRepository.save(deliveryNote);
        return result;
    }

    @Override
    public Optional<DeliveryNoteDTO> partialUpdate(DeliveryNoteDTO deliveryNoteDTO) {
        log.debug("Request to partially update DeliveryNote : {}", deliveryNoteDTO);

        return deliveryNoteRepository
            .findById(deliveryNoteDTO.getId())
            .map(existingDeliveryNote -> {
                deliveryNoteMapper.partialUpdate(existingDeliveryNote, deliveryNoteDTO);

                return existingDeliveryNote;
            })
            .map(deliveryNoteRepository::save)
            .map(savedDeliveryNote -> {
                deliveryNoteSearchRepository.save(savedDeliveryNote);

                return savedDeliveryNote;
            })
            .map(deliveryNoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryNotes");
        return deliveryNoteRepository.findAll(pageable).map(deliveryNoteMapper::toDto);
    }

    public Page<DeliveryNoteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deliveryNoteRepository.findAllWithEagerRelationships(pageable).map(deliveryNoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryNoteDTO> findOne(Long id) {
        log.debug("Request to get DeliveryNote : {}", id);
        return deliveryNoteRepository.findOneWithEagerRelationships(id).map(deliveryNoteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeliveryNote : {}", id);
        deliveryNoteRepository.deleteById(id);
        deliveryNoteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeliveryNotes for query {}", query);
        return deliveryNoteSearchRepository.search(query, pageable).map(deliveryNoteMapper::toDto);
    }
}
