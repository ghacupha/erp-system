package io.github.erp.aop.lease;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.internal.service.leases.LeaseLiabilityCompilationJob;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

@Aspect
public class LeaseLiabilityCompilationRequestInterceptor {


    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityCompilationRequestInterceptor.class);

    private final LeaseLiabilityCompilationJob leaseLiabilityCompilationJob;

    public LeaseLiabilityCompilationRequestInterceptor(LeaseLiabilityCompilationJob leaseLiabilityCompilationJob) {
        this.leaseLiabilityCompilationJob = leaseLiabilityCompilationJob;
    }

    @AfterReturning(
        pointcut="reportRequisitionPointcut()",
        returning="response")
    public void getCompilationInfo(JoinPoint joinPoint, ResponseEntity<LeaseLiabilityCompilationDTO> response) {

        log.info("Lease compilation request response intercept completed successfully");

        LeaseLiabilityCompilationDTO requestDTO = Objects.requireNonNull(response.getBody());
        Long requestId = response.getBody().getId();
        long entityId = requestDTO.getId();

        log.info("Lease compilation request with id: {} has been registered, with entity id # {} commencing compilation sequence...", requestId, entityId);

        compileAmortization(requestDTO);
    }

    @Async
    void compileAmortization(LeaseLiabilityCompilationDTO requestDTO) {

        leaseLiabilityCompilationJob.compileLeaseLiabilitySchedule(requestDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.leases.LeaseLiabilityCompilationResourceProd.createLeaseLiabilityCompilation(..))")
    public void reportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
