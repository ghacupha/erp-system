
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
package io.github.erp.aop.reporting.prepaymentByAccount;

import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import tech.jhipster.web.util.ResponseUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Intercept for the get request on the api/prepayments/prepayment-by-account-report-requisitions/{id}
 * path which is responsible for attaching to the object that is fetched from the server with the
 * file related to the same object that is fetched from the file system
 */
@Aspect
public class PrepaymentByAccountReportAttachmentInterceptor {

    private final ReportAttachmentService<PrepaymentByAccountReportRequisitionDTO> reportAttachmentService;

    public PrepaymentByAccountReportAttachmentInterceptor(ReportAttachmentService<PrepaymentByAccountReportRequisitionDTO> reportAttachmentService) {
        this.reportAttachmentService = reportAttachmentService;
    }

    /**
     * Advice that attaches a report to a response when we are responding to client.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around(value = "reportResponsePointcut()")
    public ResponseEntity<PrepaymentByAccountReportRequisitionDTO> attachReportToResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            ResponseEntity<PrepaymentByAccountReportRequisitionDTO> result = (ResponseEntity<PrepaymentByAccountReportRequisitionDTO>)joinPoint.proceed();

            ResponseEntity<PrepaymentByAccountReportRequisitionDTO> advisedReport =
                ResponseUtil.wrapOrNotFound(
                    Optional.of(
                        reportAttachmentService.attachReport(Objects.requireNonNull(result.getBody())))
                );

            if (log.isDebugEnabled()) {
                log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
            }
            return advisedReport;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Pointcut for report-requisition file attachment
     */
    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.PrepaymentByAccountReportRequisitionResourceProd.getPrepaymentByAccountReportRequisition(..)))")
    public void reportResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
