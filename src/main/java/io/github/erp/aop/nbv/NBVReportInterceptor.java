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
import io.github.erp.internal.report.service.NetBookValueEntryExportReportService;
import io.github.erp.service.dto.NbvReportDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Aspect
public class NBVReportInterceptor {

    private static final Logger log = LoggerFactory.getLogger(NBVReportInterceptor.class);

    private final NetBookValueEntryExportReportService<NbvReportDTO> netBookValueEntryExportReportService;

    public NBVReportInterceptor(NetBookValueEntryExportReportService<NbvReportDTO> netBookValueEntryExportReportService) {
        this.netBookValueEntryExportReportService = netBookValueEntryExportReportService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.assets.NbvReportResourceProd.createNbvReport(..))",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<NbvReportDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        NbvReportDTO reportDTO = Objects.requireNonNull(response.getBody());

        String reportId = reportDTO.getReportName();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        netBookValueEntryExportReportService.exportEntryReport(reportDTO);
    }

}
