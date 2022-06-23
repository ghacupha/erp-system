package io.github.erp.aop.reporting;

import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.ReportAttachmentService;
import io.github.erp.service.dto.ReportRequisitionDTO;
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
 * This object the #getReportRequisition(id) method call and attaches the appropriate report
 * from the file system.
 */
@Aspect
public class ReportRequisitionAttachmentInterceptor {

    private final ReportAttachmentService<ReportRequisitionDTO> reportAttachmentService;

    public ReportRequisitionAttachmentInterceptor(ReportAttachmentService<ReportRequisitionDTO> reportAttachmentService) {
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
    public ResponseEntity<ReportRequisitionDTO> attachReportToResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            ResponseEntity<ReportRequisitionDTO> result = (ResponseEntity<ReportRequisitionDTO>)joinPoint.proceed();

            ResponseEntity<ReportRequisitionDTO> advisedReport =
                ResponseUtil.wrapOrNotFound(
                    Optional.of(reportAttachmentService.attachReport(Objects.requireNonNull(result.getBody())))
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
    @Pointcut("execution(* io.github.erp.erp.resources.ReportRequisitionResource.getReportRequisition(..))")
    public void reportResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

}
