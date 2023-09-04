package io.github.erp.aop.depreciation;

import io.github.erp.erp.depreciation.DepreciationJobSequenceService;
import io.github.erp.service.dto.DepreciationJobDTO;
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
public class DepreciationRunInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DepreciationRunInterceptor.class);

    private final DepreciationJobSequenceService<DepreciationJobDTO> depreciationJobSequenceService;

    public DepreciationRunInterceptor(DepreciationJobSequenceService<DepreciationJobDTO> depreciationJobSequenceService) {
        this.depreciationJobSequenceService = depreciationJobSequenceService;
    }

    @AfterReturning(
        pointcut = "execution(* io.github.erp.erp.resources.DepreciationJobResourceProd.createDepreciationJob(..))",
        returning = "response")
    @SneakyThrows
    @Async
    void createJob(JoinPoint joinPoint, ResponseEntity<DepreciationJobDTO> response) {

        long start = System.currentTimeMillis();

        DepreciationJobDTO job = Objects.requireNonNull(response.getBody());

        launchDepreciationJob(job);

        log.info("Depreciation Job successfully launched in {} milliseconds, standby for execution...", System.currentTimeMillis() - start);
    }

    @SneakyThrows
    @Async
    void launchDepreciationJob(DepreciationJobDTO depreciationJob) {

        // starting depreciation...
        depreciationJobSequenceService.triggerDepreciation(depreciationJob);

    }
}
