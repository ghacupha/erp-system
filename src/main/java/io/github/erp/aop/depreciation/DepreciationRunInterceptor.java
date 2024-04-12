package io.github.erp.aop.depreciation;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
