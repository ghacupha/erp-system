package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.AssetAdditionsReport;
import io.github.erp.repository.AssetAdditionsReportRepository;
import io.github.erp.repository.search.AssetAdditionsReportSearchRepository;
import io.github.erp.service.AssetAdditionsReportService;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import io.github.erp.service.mapper.AssetAdditionsReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetAdditionsReport}.
 */
@Service
@Transactional
public class AssetAdditionsReportServiceImpl implements AssetAdditionsReportService {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportServiceImpl.class);

    private final AssetAdditionsReportRepository assetAdditionsReportRepository;

    private final AssetAdditionsReportMapper assetAdditionsReportMapper;

    private final AssetAdditionsReportSearchRepository assetAdditionsReportSearchRepository;

    public AssetAdditionsReportServiceImpl(
        AssetAdditionsReportRepository assetAdditionsReportRepository,
        AssetAdditionsReportMapper assetAdditionsReportMapper,
        AssetAdditionsReportSearchRepository assetAdditionsReportSearchRepository
    ) {
        this.assetAdditionsReportRepository = assetAdditionsReportRepository;
        this.assetAdditionsReportMapper = assetAdditionsReportMapper;
        this.assetAdditionsReportSearchRepository = assetAdditionsReportSearchRepository;
    }

    @Override
    public AssetAdditionsReportDTO save(AssetAdditionsReportDTO assetAdditionsReportDTO) {
        log.debug("Request to save AssetAdditionsReport : {}", assetAdditionsReportDTO);
        AssetAdditionsReport assetAdditionsReport = assetAdditionsReportMapper.toEntity(assetAdditionsReportDTO);
        assetAdditionsReport = assetAdditionsReportRepository.save(assetAdditionsReport);
        AssetAdditionsReportDTO result = assetAdditionsReportMapper.toDto(assetAdditionsReport);
        assetAdditionsReportSearchRepository.save(assetAdditionsReport);
        return result;
    }

    @Override
    public Optional<AssetAdditionsReportDTO> partialUpdate(AssetAdditionsReportDTO assetAdditionsReportDTO) {
        log.debug("Request to partially update AssetAdditionsReport : {}", assetAdditionsReportDTO);

        return assetAdditionsReportRepository
            .findById(assetAdditionsReportDTO.getId())
            .map(existingAssetAdditionsReport -> {
                assetAdditionsReportMapper.partialUpdate(existingAssetAdditionsReport, assetAdditionsReportDTO);

                return existingAssetAdditionsReport;
            })
            .map(assetAdditionsReportRepository::save)
            .map(savedAssetAdditionsReport -> {
                assetAdditionsReportSearchRepository.save(savedAssetAdditionsReport);

                return savedAssetAdditionsReport;
            })
            .map(assetAdditionsReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAdditionsReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetAdditionsReports");
        return assetAdditionsReportRepository.findAll(pageable).map(assetAdditionsReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetAdditionsReportDTO> findOne(Long id) {
        log.debug("Request to get AssetAdditionsReport : {}", id);
        return assetAdditionsReportRepository.findById(id).map(assetAdditionsReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetAdditionsReport : {}", id);
        assetAdditionsReportRepository.deleteById(id);
        assetAdditionsReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAdditionsReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetAdditionsReports for query {}", query);
        return assetAdditionsReportSearchRepository.search(query, pageable).map(assetAdditionsReportMapper::toDto);
    }
}
