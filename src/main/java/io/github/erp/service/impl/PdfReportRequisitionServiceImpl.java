package io.github.erp.service.impl;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.PdfReportRequisition;
import io.github.erp.repository.PdfReportRequisitionRepository;
import io.github.erp.repository.search.PdfReportRequisitionSearchRepository;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.mapper.PdfReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PdfReportRequisition}.
 */
@Service
@Transactional
public class PdfReportRequisitionServiceImpl implements PdfReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(PdfReportRequisitionServiceImpl.class);

    private final PdfReportRequisitionRepository pdfReportRequisitionRepository;

    private final PdfReportRequisitionMapper pdfReportRequisitionMapper;

    private final PdfReportRequisitionSearchRepository pdfReportRequisitionSearchRepository;

    public PdfReportRequisitionServiceImpl(
        PdfReportRequisitionRepository pdfReportRequisitionRepository,
        PdfReportRequisitionMapper pdfReportRequisitionMapper,
        PdfReportRequisitionSearchRepository pdfReportRequisitionSearchRepository
    ) {
        this.pdfReportRequisitionRepository = pdfReportRequisitionRepository;
        this.pdfReportRequisitionMapper = pdfReportRequisitionMapper;
        this.pdfReportRequisitionSearchRepository = pdfReportRequisitionSearchRepository;
    }

    @Override
    public PdfReportRequisitionDTO save(PdfReportRequisitionDTO pdfReportRequisitionDTO) {
        log.debug("Request to save PdfReportRequisition : {}", pdfReportRequisitionDTO);
        PdfReportRequisition pdfReportRequisition = pdfReportRequisitionMapper.toEntity(pdfReportRequisitionDTO);
        pdfReportRequisition = pdfReportRequisitionRepository.save(pdfReportRequisition);
        PdfReportRequisitionDTO result = pdfReportRequisitionMapper.toDto(pdfReportRequisition);
        pdfReportRequisitionSearchRepository.save(pdfReportRequisition);
        return result;
    }

    @Override
    public Optional<PdfReportRequisitionDTO> partialUpdate(PdfReportRequisitionDTO pdfReportRequisitionDTO) {
        log.debug("Request to partially update PdfReportRequisition : {}", pdfReportRequisitionDTO);

        return pdfReportRequisitionRepository
            .findById(pdfReportRequisitionDTO.getId())
            .map(existingPdfReportRequisition -> {
                pdfReportRequisitionMapper.partialUpdate(existingPdfReportRequisition, pdfReportRequisitionDTO);

                return existingPdfReportRequisition;
            })
            .map(pdfReportRequisitionRepository::save)
            .map(savedPdfReportRequisition -> {
                pdfReportRequisitionSearchRepository.save(savedPdfReportRequisition);

                return savedPdfReportRequisition;
            })
            .map(pdfReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PdfReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PdfReportRequisitions");
        return pdfReportRequisitionRepository.findAll(pageable).map(pdfReportRequisitionMapper::toDto);
    }

    public Page<PdfReportRequisitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pdfReportRequisitionRepository.findAllWithEagerRelationships(pageable).map(pdfReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PdfReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PdfReportRequisition : {}", id);
        return pdfReportRequisitionRepository.findOneWithEagerRelationships(id).map(pdfReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PdfReportRequisition : {}", id);
        pdfReportRequisitionRepository.deleteById(id);
        pdfReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PdfReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PdfReportRequisitions for query {}", query);
        return pdfReportRequisitionSearchRepository.search(query, pageable).map(pdfReportRequisitionMapper::toDto);
    }
}
