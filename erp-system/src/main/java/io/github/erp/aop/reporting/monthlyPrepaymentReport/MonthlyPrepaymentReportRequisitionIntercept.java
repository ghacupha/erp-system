
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
package io.github.erp.aop.reporting.monthlyPrepaymentReport;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.aop.reporting.amortizationPosting.AmortizationPostingReportRequisitionInterceptor;
import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Locale;
import java.util.Objects;

@Aspect
public class MonthlyPrepaymentReportRequisitionIntercept {


    private static final Logger log = LoggerFactory.getLogger(AmortizationPostingReportRequisitionInterceptor.class);

    private final ExportReportService<MonthlyPrepaymentReportRequisitionDTO> prepaymentByAccountReportRequisitionDTOExportReportService;

    public MonthlyPrepaymentReportRequisitionIntercept(ExportReportService<MonthlyPrepaymentReportRequisitionDTO> prepaymentByAccountReportRequisitionDTOExportReportService) {
        this.prepaymentByAccountReportRequisitionDTOExportReportService = prepaymentByAccountReportRequisitionDTOExportReportService;
    }

    @AfterReturning(
        pointcut="reportRequisitionPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        MonthlyPrepaymentReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getRequestId().toLowerCase(Locale.ROOT);
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async
    void createReport(MonthlyPrepaymentReportRequisitionDTO reportDTO) {

        prepaymentByAccountReportRequisitionDTOExportReportService.exportReport(reportDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.MonthlyPrepaymentReportRequisitionResourceProd.createMonthlyPrepaymentReportRequisition(..))")
    public void reportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
