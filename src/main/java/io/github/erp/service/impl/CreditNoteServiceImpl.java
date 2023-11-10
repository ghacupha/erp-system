package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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

import io.github.erp.domain.CreditNote;
import io.github.erp.repository.CreditNoteRepository;
import io.github.erp.repository.search.CreditNoteSearchRepository;
import io.github.erp.service.CreditNoteService;
import io.github.erp.service.dto.CreditNoteDTO;
import io.github.erp.service.mapper.CreditNoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CreditNote}.
 */
@Service
@Transactional
public class CreditNoteServiceImpl implements CreditNoteService {

    private final Logger log = LoggerFactory.getLogger(CreditNoteServiceImpl.class);

    private final CreditNoteRepository creditNoteRepository;

    private final CreditNoteMapper creditNoteMapper;

    private final CreditNoteSearchRepository creditNoteSearchRepository;

    public CreditNoteServiceImpl(
        CreditNoteRepository creditNoteRepository,
        CreditNoteMapper creditNoteMapper,
        CreditNoteSearchRepository creditNoteSearchRepository
    ) {
        this.creditNoteRepository = creditNoteRepository;
        this.creditNoteMapper = creditNoteMapper;
        this.creditNoteSearchRepository = creditNoteSearchRepository;
    }

    @Override
    public CreditNoteDTO save(CreditNoteDTO creditNoteDTO) {
        log.debug("Request to save CreditNote : {}", creditNoteDTO);
        CreditNote creditNote = creditNoteMapper.toEntity(creditNoteDTO);
        creditNote = creditNoteRepository.save(creditNote);
        CreditNoteDTO result = creditNoteMapper.toDto(creditNote);
        creditNoteSearchRepository.save(creditNote);
        return result;
    }

    @Override
    public Optional<CreditNoteDTO> partialUpdate(CreditNoteDTO creditNoteDTO) {
        log.debug("Request to partially update CreditNote : {}", creditNoteDTO);

        return creditNoteRepository
            .findById(creditNoteDTO.getId())
            .map(existingCreditNote -> {
                creditNoteMapper.partialUpdate(existingCreditNote, creditNoteDTO);

                return existingCreditNote;
            })
            .map(creditNoteRepository::save)
            .map(savedCreditNote -> {
                creditNoteSearchRepository.save(savedCreditNote);

                return savedCreditNote;
            })
            .map(creditNoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditNoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreditNotes");
        return creditNoteRepository.findAll(pageable).map(creditNoteMapper::toDto);
    }

    public Page<CreditNoteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return creditNoteRepository.findAllWithEagerRelationships(pageable).map(creditNoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditNoteDTO> findOne(Long id) {
        log.debug("Request to get CreditNote : {}", id);
        return creditNoteRepository.findOneWithEagerRelationships(id).map(creditNoteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreditNote : {}", id);
        creditNoteRepository.deleteById(id);
        creditNoteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditNoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CreditNotes for query {}", query);
        return creditNoteSearchRepository.search(query, pageable).map(creditNoteMapper::toDto);
    }
}
