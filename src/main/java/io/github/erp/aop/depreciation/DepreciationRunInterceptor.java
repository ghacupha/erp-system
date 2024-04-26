package io.github.erp.aop.depreciation;

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
import io.github.erp.erp.assets.depreciation.DepreciationJobSequenceService;
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
        pointcut = "execution(* io.github.erp.erp.resources.depreciation.DepreciationJobResourceProd.createDepreciationJob(..))",
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
