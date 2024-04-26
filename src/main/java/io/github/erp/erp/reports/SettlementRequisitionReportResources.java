package io.github.erp.erp.reports;

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
