package io.github.erp.aop.reporting;

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
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.dto.DepreciationReportDTO;
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

@Aspect
public class DepreciationReportAttachmentInterceptor {

    private final ReportAttachmentService<DepreciationReportDTO> reportAttachmentService;

    public DepreciationReportAttachmentInterceptor(ReportAttachmentService<DepreciationReportDTO> reportAttachmentService) {
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
    public ResponseEntity<DepreciationReportDTO> attachReportToResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            ResponseEntity<DepreciationReportDTO> result = (ResponseEntity<DepreciationReportDTO>)joinPoint.proceed();

            ResponseEntity<DepreciationReportDTO> advisedReport =
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
    @Pointcut("execution(* io.github.erp.erp.resources.depreciation.DepreciationReportResourceProd.getDepreciationReport(..))")
    public void reportResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
