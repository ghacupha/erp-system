package io.github.erp.aop.reporting;

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
import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
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
public class AssetAdditionsReportInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DepreciationReportInterceptor.class);

    private final ExportReportService<AssetAdditionsReportDTO> assetReportExportReportService;

    public AssetAdditionsReportInterceptor(ExportReportService<AssetAdditionsReportDTO> assetReportExportReportService) {
        this.assetReportExportReportService = assetReportExportReportService;
    }

    @AfterReturning(
        pointcut="assetAdditionsReportPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<AssetAdditionsReportDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        AssetAdditionsReportDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getRequestId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async void createReport(AssetAdditionsReportDTO reportDTO) {
        assetReportExportReportService.exportReport(reportDTO);
    }

    /**
     * Pointcut for report-requisition file attachment
     */
    @Pointcut("execution(* io.github.erp.erp.resources.assets.AssetAdditionsReportResourceProd.createAssetAdditionsReport(..))")
    public void assetAdditionsReportPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
