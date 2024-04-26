package io.github.erp.aop.nbv;

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

    private static final Logger log = LoggerFactory.getLogger(NBVJobInterceptor.class);

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

        log.info("NBV Job successfully launched in {} milliseconds, standby for execution...", System.currentTimeMillis() - start);
    }

    @SneakyThrows
    @Async
    void launchJob(NbvCompilationJobDTO job) {

        nbvJobSequenceService.triggerJobStart(job);

    }
}
