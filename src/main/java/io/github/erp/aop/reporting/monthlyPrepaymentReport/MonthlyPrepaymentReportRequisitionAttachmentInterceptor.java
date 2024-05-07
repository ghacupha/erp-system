package io.github.erp.aop.reporting.monthlyPrepaymentReport;

import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
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
 * Interceptor for the monthly-prepayment API on the get path for the
 * api/prepayments/monthly-prepayment-report-requisitions/{id}
 */
@Aspect
public class MonthlyPrepaymentReportRequisitionAttachmentInterceptor {


    private final ReportAttachmentService<MonthlyPrepaymentReportRequisitionDTO> reportAttachmentService;

    public MonthlyPrepaymentReportRequisitionAttachmentInterceptor(ReportAttachmentService<MonthlyPrepaymentReportRequisitionDTO> reportAttachmentService) {
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
    public ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> attachReportToResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> result = (ResponseEntity<MonthlyPrepaymentReportRequisitionDTO>)joinPoint.proceed();

            ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> advisedReport =
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
    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.MonthlyPrepaymentReportRequisitionResourceProd.getMonthlyPrepaymentReportRequisition(..)))")
    public void reportResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
