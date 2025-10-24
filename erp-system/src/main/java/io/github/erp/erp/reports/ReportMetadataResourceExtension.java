package io.github.erp.erp.reports;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.service.ReportMetadataQueryService;
import io.github.erp.service.criteria.ReportMetadataCriteria;
import io.github.erp.service.dto.ReportMetadataDTO;
import io.github.erp.service.mapper.ReportMetadataMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * Extension resource that exposes simplified, read-only endpoints for report metadata discovery.
 */
@RestController
@RequestMapping("/api/app")
public class ReportMetadataResourceExtension {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataResourceExtension.class);

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataMapper reportMetadataMapper;

    private final ReportMetadataQueryService reportMetadataQueryService;

    public ReportMetadataResourceExtension(
        ReportMetadataRepository reportMetadataRepository,
        ReportMetadataMapper reportMetadataMapper,
        ReportMetadataQueryService reportMetadataQueryService
    ) {
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataMapper = reportMetadataMapper;
        this.reportMetadataQueryService = reportMetadataQueryService;
    }

    @GetMapping("/report-metadata/search")
    public ResponseEntity<List<ReportMetadataDTO>> searchActiveReportMetadata(
        @RequestParam("term") String term,
        Pageable pageable
    ) {
        log.debug("REST request to search active report metadata by term: {}", term);
        Page<ReportMetadataDTO> page;
        if (StringUtils.hasText(term)) {
            page = reportMetadataRepository
                .searchActiveByTerm(term.trim(), pageable)
                .map(reportMetadataMapper::toDto);
        } else {
            ReportMetadataCriteria criteria = new ReportMetadataCriteria();
            criteria.active().setEquals(true);
            page = reportMetadataQueryService.findByCriteria(criteria, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/report-metadata/by-path")
    public ResponseEntity<ReportMetadataDTO> getByPagePath(@RequestParam("path") String pagePath) {
        log.debug("REST request to fetch report metadata by page path: {}", pagePath);
        Optional<ReportMetadataDTO> dto = reportMetadataRepository
            .findOneByPagePath(pagePath)
            .map(reportMetadataMapper::toDto);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @GetMapping("/report-metadata/active")
    public ResponseEntity<List<ReportMetadataDTO>> getActiveReportMetadata(Pageable pageable) {
        log.debug("REST request to fetch active report metadata");
        ReportMetadataCriteria criteria = new ReportMetadataCriteria();
        criteria.active().setEquals(true);
        Page<ReportMetadataDTO> page = reportMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
