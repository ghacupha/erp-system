
/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
package io.github.erp.aop.reporting.amortizationPosting;

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
import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

/**
 * Amortization posting requisition intercept intercepts the API call for the AmortizationPostingReportRequisition
 * routing the same to actual (but asynchronous) report-generating entities which will run a report
 * and export it to the file system.
 */
@Aspect
public class AmortizationPostingReportRequisitionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AmortizationPostingReportRequisitionInterceptor.class);

    private final ExportReportService<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionExportReportService;

    public AmortizationPostingReportRequisitionInterceptor(
        ExportReportService<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionExportReportService){
        this.amortizationPostingReportRequisitionExportReportService = amortizationPostingReportRequisitionExportReportService;
    }

    @AfterReturning(
        pointcut="reportRequisitionPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<AmortizationPostingReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        AmortizationPostingReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getRequestId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async
    void createReport(AmortizationPostingReportRequisitionDTO reportDTO) {

        amortizationPostingReportRequisitionExportReportService.exportReport(reportDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.AmortizationPostingReportRequisitionResourceProd.createAmortizationPostingReportRequisition(..))")
    public void reportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
