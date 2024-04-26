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

import io.github.erp.domain.XlsxReportRequisition;
import io.github.erp.repository.XlsxReportRequisitionRepository;
import io.github.erp.repository.search.XlsxReportRequisitionSearchRepository;
import io.github.erp.service.XlsxReportRequisitionService;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import io.github.erp.service.mapper.XlsxReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link XlsxReportRequisition}.
 */
@Service
@Transactional
public class XlsxReportRequisitionServiceImpl implements XlsxReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(XlsxReportRequisitionServiceImpl.class);

    private final XlsxReportRequisitionRepository xlsxReportRequisitionRepository;

    private final XlsxReportRequisitionMapper xlsxReportRequisitionMapper;

    private final XlsxReportRequisitionSearchRepository xlsxReportRequisitionSearchRepository;

    public XlsxReportRequisitionServiceImpl(
        XlsxReportRequisitionRepository xlsxReportRequisitionRepository,
        XlsxReportRequisitionMapper xlsxReportRequisitionMapper,
        XlsxReportRequisitionSearchRepository xlsxReportRequisitionSearchRepository
    ) {
        this.xlsxReportRequisitionRepository = xlsxReportRequisitionRepository;
        this.xlsxReportRequisitionMapper = xlsxReportRequisitionMapper;
        this.xlsxReportRequisitionSearchRepository = xlsxReportRequisitionSearchRepository;
    }

    @Override
    public XlsxReportRequisitionDTO save(XlsxReportRequisitionDTO xlsxReportRequisitionDTO) {
        log.debug("Request to save XlsxReportRequisition : {}", xlsxReportRequisitionDTO);
        XlsxReportRequisition xlsxReportRequisition = xlsxReportRequisitionMapper.toEntity(xlsxReportRequisitionDTO);
        xlsxReportRequisition = xlsxReportRequisitionRepository.save(xlsxReportRequisition);
        XlsxReportRequisitionDTO result = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);
        xlsxReportRequisitionSearchRepository.save(xlsxReportRequisition);
        return result;
    }

    @Override
    public Optional<XlsxReportRequisitionDTO> partialUpdate(XlsxReportRequisitionDTO xlsxReportRequisitionDTO) {
        log.debug("Request to partially update XlsxReportRequisition : {}", xlsxReportRequisitionDTO);

        return xlsxReportRequisitionRepository
            .findById(xlsxReportRequisitionDTO.getId())
            .map(existingXlsxReportRequisition -> {
                xlsxReportRequisitionMapper.partialUpdate(existingXlsxReportRequisition, xlsxReportRequisitionDTO);

                return existingXlsxReportRequisition;
            })
            .map(xlsxReportRequisitionRepository::save)
            .map(savedXlsxReportRequisition -> {
                xlsxReportRequisitionSearchRepository.save(savedXlsxReportRequisition);

                return savedXlsxReportRequisition;
            })
            .map(xlsxReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XlsxReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XlsxReportRequisitions");
        return xlsxReportRequisitionRepository.findAll(pageable).map(xlsxReportRequisitionMapper::toDto);
    }

    public Page<XlsxReportRequisitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return xlsxReportRequisitionRepository.findAllWithEagerRelationships(pageable).map(xlsxReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<XlsxReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get XlsxReportRequisition : {}", id);
        return xlsxReportRequisitionRepository.findOneWithEagerRelationships(id).map(xlsxReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete XlsxReportRequisition : {}", id);
        xlsxReportRequisitionRepository.deleteById(id);
        xlsxReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XlsxReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of XlsxReportRequisitions for query {}", query);
        return xlsxReportRequisitionSearchRepository.search(query, pageable).map(xlsxReportRequisitionMapper::toDto);
    }
}
