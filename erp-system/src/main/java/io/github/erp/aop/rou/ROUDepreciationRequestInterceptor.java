package io.github.erp.aop.rou;

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
import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationJob;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
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
public class ROUDepreciationRequestInterceptor {


    private static final Logger log = LoggerFactory.getLogger(ROUDepreciationRequestInterceptor.class);

    private final ROUDepreciationEntryCompilationJob rouDepreciationEntryCompilationJob;

    public ROUDepreciationRequestInterceptor(ROUDepreciationEntryCompilationJob rouDepreciationEntryCompilationJob) {
        this.rouDepreciationEntryCompilationJob = rouDepreciationEntryCompilationJob;
    }

    @AfterReturning(
        pointcut="reportRequisitionPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<RouDepreciationRequestDTO> response) {

        log.info("ROU Depreciation compilation request response intercept completed successfully");

        RouDepreciationRequestDTO requestDTO = Objects.requireNonNull(response.getBody());
        Long requestId = response.getBody().getId();
        long entityId = requestDTO.getId();

        log.info("ROU Depreciation compilation request with id: {} has been registered, with entity id # {} commencing compilation sequence...", requestId, entityId);

        compileDepreciation(requestDTO);
    }

    @Async
    void compileDepreciation(RouDepreciationRequestDTO reportDTO) {

        rouDepreciationEntryCompilationJob.compileROUDepreciationEntries(reportDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.leases.RouDepreciationRequestResourceProd.createRouDepreciationRequest(..))")
    public void reportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
