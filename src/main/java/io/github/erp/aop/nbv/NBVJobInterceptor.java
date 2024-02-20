package io.github.erp.aop.nbv;

import io.github.erp.aop.depreciation.DepreciationRunInterceptor;
import io.github.erp.erp.assets.nbv.NBVJobSequenceService;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import io.github.erp.service.dto.NbvReportDTO;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

@Aspect
public class NBVJobInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DepreciationRunInterceptor.class);

    private final NBVJobSequenceService<NbvCompilationJobDTO> nbvJobSequenceService;

    public NBVJobInterceptor(NBVJobSequenceService<NbvCompilationJobDTO> nbvJobSequenceService) {
        this.nbvJobSequenceService = nbvJobSequenceService;
    }

    @AfterReturning(
        pointcut = "execution(* io.github.erp.erp.resources.assets.NbvCompilationJobResourceProd.createNbvCompilationJob(..))",
        returning = "response")
    @SneakyThrows
    @Async
    void createJob(JoinPoint joinPoint, ResponseEntity<NbvCompilationJobDTO> response) {

        long start = System.currentTimeMillis();

        NbvCompilationJobDTO job = Objects.requireNonNull(response.getBody());

        launchJob(job);

        log.info("Job successfully launched in {} milliseconds, standby for execution...", System.currentTimeMillis() - start);
    }

    @SneakyThrows
    @Async
    void launchJob(NbvCompilationJobDTO job) {

        nbvJobSequenceService.triggerJobStart(job);

    }
}
