package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.NbvReport;
import io.github.erp.repository.NbvReportRepository;
import io.github.erp.repository.search.NbvReportSearchRepository;
import io.github.erp.service.NbvReportService;
import io.github.erp.service.dto.NbvReportDTO;
import io.github.erp.service.mapper.NbvReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NbvReport}.
 */
@Service
@Transactional
public class NbvReportServiceImpl implements NbvReportService {

    private final Logger log = LoggerFactory.getLogger(NbvReportServiceImpl.class);

    private final NbvReportRepository nbvReportRepository;

    private final NbvReportMapper nbvReportMapper;

    private final NbvReportSearchRepository nbvReportSearchRepository;

    public NbvReportServiceImpl(
        NbvReportRepository nbvReportRepository,
        NbvReportMapper nbvReportMapper,
        NbvReportSearchRepository nbvReportSearchRepository
    ) {
        this.nbvReportRepository = nbvReportRepository;
        this.nbvReportMapper = nbvReportMapper;
        this.nbvReportSearchRepository = nbvReportSearchRepository;
    }

    @Override
    public NbvReportDTO save(NbvReportDTO nbvReportDTO) {
        log.debug("Request to save NbvReport : {}", nbvReportDTO);
        NbvReport nbvReport = nbvReportMapper.toEntity(nbvReportDTO);
        nbvReport = nbvReportRepository.save(nbvReport);
        NbvReportDTO result = nbvReportMapper.toDto(nbvReport);
        nbvReportSearchRepository.save(nbvReport);
        return result;
    }

    @Override
    public Optional<NbvReportDTO> partialUpdate(NbvReportDTO nbvReportDTO) {
        log.debug("Request to partially update NbvReport : {}", nbvReportDTO);

        return nbvReportRepository
            .findById(nbvReportDTO.getId())
            .map(existingNbvReport -> {
                nbvReportMapper.partialUpdate(existingNbvReport, nbvReportDTO);

                return existingNbvReport;
            })
            .map(nbvReportRepository::save)
            .map(savedNbvReport -> {
                nbvReportSearchRepository.save(savedNbvReport);

                return savedNbvReport;
            })
            .map(nbvReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NbvReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NbvReports");
        return nbvReportRepository.findAll(pageable).map(nbvReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NbvReportDTO> findOne(Long id) {
        log.debug("Request to get NbvReport : {}", id);
        return nbvReportRepository.findById(id).map(nbvReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NbvReport : {}", id);
        nbvReportRepository.deleteById(id);
        nbvReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NbvReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NbvReports for query {}", query);
        return nbvReportSearchRepository.search(query, pageable).map(nbvReportMapper::toDto);
    }
}
