package io.github.erp.aop.nbv;

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
