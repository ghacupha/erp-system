package io.github.erp.aop.reporting;

import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
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
public class AmortizationPostingReportRequisitionAttachmentInterceptor {

    private final ReportAttachmentService<AmortizationPostingReportRequisitionDTO> reportAttachmentService;

    public AmortizationPostingReportRequisitionAttachmentInterceptor(ReportAttachmentService<AmortizationPostingReportRequisitionDTO> reportAttachmentService) {
        this.reportAttachmentService = reportAttachmentService;
    }

    /**
     * Advice that attaches a report to a response when we are responding to client.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around(value = "reportRequisitionResponsePointcut()")
    public ResponseEntity<AmortizationPostingReportRequisitionDTO> attachReportToResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            ResponseEntity<AmortizationPostingReportRequisitionDTO> result = (ResponseEntity<AmortizationPostingReportRequisitionDTO>)joinPoint.proceed();

            ResponseEntity<AmortizationPostingReportRequisitionDTO> advisedReport =
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
     * Pointcut for amortization-posting-report file attachment
     */
    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.AmortizationPostingReportRequisitionResourceProd.getAmortizationPostingReportRequisition(..))")
    public void reportRequisitionResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
