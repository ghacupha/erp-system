package io.github.erp.erp.reports;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.ReportMetadata;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.service.ReportMetadataService;
import io.github.erp.service.dto.ReportMetadataDTO;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Seeds the database with baseline report metadata so the dynamic reporting UI has meaningful defaults.
 */
@Component
@Transactional
public class ReportMetadataSeederExtension implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ReportMetadataSeederExtension.class);

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataService reportMetadataService;

    private final List<ReportMetadataSeed> seeds = List.of(
        new ReportMetadataSeed(
            "Lease Liability by Account",
            "Aggregated lease liability positions grouped by GL account with the latest recognised balances.",
            "Leases",
            "reports/view/lease-liability-by-account",
            "api/lease-liability-by-account-report-items",
            true,
            true,
            false,
            "leasePeriodId.equals",
            null
        ),
        new ReportMetadataSeed(
            "ROU Asset Balance by Account",
            "Right-of-use asset positions summarised by asset account including net book values per fiscal period.",
            "Leases",
            "reports/view/rou-account-balance",
            "api/rou-account-balance-report-items",
            true,
            true,
            false,
            "fiscalPeriodEndDate.equals",
            null
        ),
        new ReportMetadataSeed(
            "ROU Depreciation by Account",
            "Scheduled depreciation postings for right-of-use assets broken down by transaction account.",
            "Leases",
            "reports/view/rou-depreciation-by-account",
            "api/rou-depreciation-posting-report-items",
            true,
            true,
            false,
            "leasePeriodId.equals",
            null
        ),
        new ReportMetadataSeed(
            "Lease Liability by Service Outlet",
            "Lease liability schedule totals grouped by service outlet for detailed operational analysis.",
            "Leases",
            "reports/view/lease-liability-by-outlet",
            "api/lease-liability-schedule-report-items",
            false,
            true,
            false,
            "leasePeriodId.equals",
            null
        ),
        new ReportMetadataSeed(
            "Lease Liability by Dealer",
            "Comparative view of liability balances per dealer contract within a selected period.",
            "Leases",
            "reports/view/lease-liability-by-dealer",
            "api/lease-liability-report-items",
            false,
            true,
            true,
            "leasePeriodId.equals",
            "leaseLiabilityId.equals"
        ),
        new ReportMetadataSeed(
            "Lease Interest Accrued by Account",
            "Interest accrued on lease liabilities with drill-down by contra account for reconciliation.",
            "Leases",
            "reports/view/lease-interest-accrued",
            "api/lease-liability-schedule-report-items",
            false,
            true,
            false,
            "leasePeriodId.equals",
            null
        ),
        new ReportMetadataSeed(
            "Lease Interest Payments by Liability",
            "Cash interest settlements grouped by lease liability and transaction account for a chosen period.",
            "Leases",
            "reports/view/lease-interest-payments",
            "api/lease-liability-schedule-report-items",
            false,
            true,
            true,
            "leasePeriodId.equals",
            "leaseLiabilityId.equals"
        )
    );

    public ReportMetadataSeederExtension(
        ReportMetadataRepository reportMetadataRepository,
        ReportMetadataService reportMetadataService
    ) {
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataService = reportMetadataService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Seeding default report metadata definitions");
        seeds.forEach(this::createOrUpdateMetadata);
    }

    private void createOrUpdateMetadata(ReportMetadataSeed seed) {
        Optional<ReportMetadata> existing = reportMetadataRepository.findOneByPagePath(seed.pagePath());
        if (existing.isPresent()) {
            ReportMetadata reportMetadata = existing.get();
            boolean changed = seed.apply(reportMetadata);
            if (changed) {
                log.debug("Updating report metadata definition for path: {}", seed.pagePath());
                reportMetadataService.save(toDto(reportMetadata));
            }
        } else {
            log.debug("Creating report metadata definition for path: {}", seed.pagePath());
            ReportMetadataDTO dto = new ReportMetadataDTO();
            dto.setReportTitle(seed.reportTitle());
            dto.setDescription(seed.description());
            dto.setModule(seed.module());
            dto.setPagePath(seed.pagePath());
            dto.setBackendApi(seed.normalizeApi(seed.backendApi()));
            dto.setActive(seed.active());
            dto.setDisplayLeasePeriod(seed.displayLeasePeriod());
            dto.setDisplayLeaseContract(seed.displayLeaseContract());
            dto.setLeasePeriodQueryParam(seed.leasePeriodQueryParam());
            dto.setLeaseContractQueryParam(seed.leaseContractQueryParam());
            reportMetadataService.save(dto);
        }
    }

    private ReportMetadataDTO toDto(ReportMetadata reportMetadata) {
        ReportMetadataDTO dto = new ReportMetadataDTO();
        dto.setId(reportMetadata.getId());
        dto.setReportTitle(reportMetadata.getReportTitle());
        dto.setDescription(reportMetadata.getDescription());
        dto.setModule(reportMetadata.getModule());
        dto.setPagePath(reportMetadata.getPagePath());
        dto.setBackendApi(reportMetadata.getBackendApi());
        dto.setActive(reportMetadata.getActive());
        dto.setDisplayLeasePeriod(reportMetadata.getDisplayLeasePeriod());
        dto.setDisplayLeaseContract(reportMetadata.getDisplayLeaseContract());
        dto.setLeasePeriodQueryParam(reportMetadata.getLeasePeriodQueryParam());
        dto.setLeaseContractQueryParam(reportMetadata.getLeaseContractQueryParam());
        return dto;
    }

    private record ReportMetadataSeed(
        String reportTitle,
        String description,
        String module,
        String pagePath,
        String backendApi,
        boolean active,
        boolean displayLeasePeriod,
        boolean displayLeaseContract,
        String leasePeriodQueryParam,
        String leaseContractQueryParam
    ) {
        boolean apply(ReportMetadata entity) {
            boolean changed = false;
            if (!Objects.equals(entity.getReportTitle(), reportTitle)) {
                entity.setReportTitle(reportTitle);
                changed = true;
            }
            if (!Objects.equals(entity.getDescription(), description)) {
                entity.setDescription(description);
                changed = true;
            }
            if (!Objects.equals(entity.getModule(), module)) {
                entity.setModule(module);
                changed = true;
            }
            if (!Objects.equals(entity.getPagePath(), pagePath)) {
                entity.setPagePath(pagePath);
                changed = true;
            }
            if (!Objects.equals(entity.getBackendApi(), backendApi)) {
                entity.setBackendApi(normalizeApi(backendApi));
                changed = true;
            }
            if (!Objects.equals(entity.getActive(), active)) {
                entity.setActive(active);
                changed = true;
            }
            if (!Objects.equals(entity.getDisplayLeasePeriod(), displayLeasePeriod)) {
                entity.setDisplayLeasePeriod(displayLeasePeriod);
                changed = true;
            }
            if (!Objects.equals(entity.getDisplayLeaseContract(), displayLeaseContract)) {
                entity.setDisplayLeaseContract(displayLeaseContract);
                changed = true;
            }
            if (!Objects.equals(entity.getLeasePeriodQueryParam(), leasePeriodQueryParam)) {
                entity.setLeasePeriodQueryParam(leasePeriodQueryParam);
                changed = true;
            }
            if (!Objects.equals(entity.getLeaseContractQueryParam(), leaseContractQueryParam)) {
                entity.setLeaseContractQueryParam(leaseContractQueryParam);
                changed = true;
            }
            return changed;
        }

        private String normalizeApi(String api) {
            if (!StringUtils.hasText(api)) {
                return api;
            }
            String trimmed = api.trim();
            if (trimmed.toLowerCase(Locale.ROOT).startsWith("http")) {
                return trimmed;
            }
            if (trimmed.startsWith("/")) {
                return trimmed.substring(1);
            }
            return trimmed;
        }
    }
}
