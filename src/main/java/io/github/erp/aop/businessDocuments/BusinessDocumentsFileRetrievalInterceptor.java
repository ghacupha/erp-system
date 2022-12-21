package io.github.erp.aop.businessDocuments;

import io.github.erp.internal.files.documents.FileAttachmentService;
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.service.dto.ExcelReportExportDTO;
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
 * This aspect intercepts around the get-one API and includes the file bytestream of the file for which the
 * entity instance has been persisted, using the file-attachement-service
 */
@Aspect
public class BusinessDocumentsFileRetrievalInterceptor {


    private final FileAttachmentService<BusinessDocumentFSO> fileAttachmentService;

    public BusinessDocumentsFileRetrievalInterceptor(FileAttachmentService<BusinessDocumentFSO> fileAttachmentService) {
        this.fileAttachmentService = fileAttachmentService;
    }

    /**
     * Advice that attaches a report to a response when we are responding to client.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around(value = "reportResponsePointcut()")
    public ResponseEntity<BusinessDocumentFSO> retrieveDocumentToResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            ResponseEntity<BusinessDocumentFSO> result = (ResponseEntity<BusinessDocumentFSO>)joinPoint.proceed();

            ResponseEntity<BusinessDocumentFSO> advisedReport =
                ResponseUtil.wrapOrNotFound(
                    Optional.of(
                        fileAttachmentService.attach(Objects.requireNonNull(result.getBody())))
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
    @Pointcut("execution(* io.github.erp.erp.resources.BusinessDocumentResource.getBusinessDocument(..))")
    public void reportResponsePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

}
