package io.github.erp.erp.reports;

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
import io.github.erp.internal.report.ReportModel;
import io.github.erp.internal.report.service.SettlementBillerReportDTO;
import io.github.erp.internal.report.service.SettlementBillerReportRequisitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/requisition")
public class SettlementRequisitionReportResources {

    private static final Logger log = LoggerFactory.getLogger(SettlementRequisitionReportResources.class);

    private final SettlementBillerReportRequisitionService settlementBillerReportRequisitionService;

    public SettlementRequisitionReportResources(@Qualifier("settlementBillerReportRequisitionServiceJPQL")SettlementBillerReportRequisitionService settlementBillerReportRequisitionService) {
        this.settlementBillerReportRequisitionService = settlementBillerReportRequisitionService;
    }

    /**
     * {@code POST  /biller-report} : Create a new settlementRequisition.
     *
     */
    @PostMapping("/biller-report")
    public ResponseEntity<List<SettlementBillerReportDTO>> createBillerReport(
        @Valid @RequestBody SettlementBillerReportRequisitionDTO reportRequisition
    ) {
        log.debug("REST request to save SettlementRequisitionBillerReport : {}", reportRequisition);

        ReportModel<List<SettlementBillerReportDTO>> reportModel = settlementBillerReportRequisitionService.generateReport(reportRequisition);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), new PageImpl<>(reportModel.getReportData(), Pageable.unpaged(), reportModel.getReportData().size()));
        return ResponseEntity.ok().headers(headers).body(reportModel.getReportData());
    }
}
