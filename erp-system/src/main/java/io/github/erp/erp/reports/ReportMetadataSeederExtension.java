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

import io.github.erp.domain.ReportFilterDefinition;
import io.github.erp.domain.ReportMetadata;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.service.ReportMetadataService;
import io.github.erp.service.dto.ReportFilterDefinitionDTO;
import io.github.erp.service.dto.ReportMetadataDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
            "api/leases/lease-liability-by-account-report-items",
            true,
            List.of(filter("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"))
        ),
        new ReportMetadataSeed(
            "ROU Asset Balance by Account",
            "Right-of-use asset positions summarised by asset account including net book values per fiscal period.",
            "Leases",
            "reports/view/rou-account-balance",
            "api/leases/rou-account-balance-report-items/reports/{leasePeriodId}",
            true,
            List.of(filter("Lease Period", "leasePeriodId", "leasePeriods", "dropdown"))
        ),
        new ReportMetadataSeed(
            "ROU Depreciation by Account",
            "Scheduled depreciation postings for right-of-use assets broken down by transaction account.",
            "Leases",
            "reports/view/rou-depreciation-by-account",
            "api/leases/rou-depreciation-posting-report-items",
            true,
            List.of(filter("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"))
        ),
        new ReportMetadataSeed(
            "Lease Liability by Service Outlet",
            "Lease liability schedule totals grouped by service outlet for detailed operational analysis.",
            "Leases",
            "reports/view/lease-liability-by-outlet",
            "api/leases/lease-liability-schedule-report-items",
            false,
            List.of(filter("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"))
        ),
        new ReportMetadataSeed(
            "Lease Liability by Dealer",
            "Comparative view of liability balances per dealer contract within a selected period.",
            "Leases",
            "reports/view/lease-liability-by-dealer",
            "api/leases/lease-liability-report-items",
            false,
            List.of(
                filter("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"),
                filter("Lease Contract", "leaseLiabilityId.equals", "leaseContracts", "typeahead")
            )
        ),
        new ReportMetadataSeed(
            "Lease Interest Accrued by Account",
            "Interest accrued on lease liabilities with drill-down by contra account for reconciliation.",
            "Leases",
            "reports/view/lease-interest-accrued",
            "api/leases/lease-liability-schedule-report-items/interest-expense-summary/{leasePeriodId}",
            true,
            List.of(filter("Lease Period", "leasePeriodId", "leaseRepaymentPeriods", "dropdown"))
        ),
        new ReportMetadataSeed(
            "Lease Interest Payments by Liability",
            "Cash interest settlements grouped by lease liability and transaction account for a chosen period.",
            "Leases",
            "reports/view/lease-interest-payments",
            "api/leases/lease-liability-schedule-report-items",
            false,
            List.of(
                filter("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"),
                filter("Lease Contract", "leaseLiabilityId.equals", "leaseContracts", "typeahead")
            )
        ),
        new ReportMetadataSeed(
            "Prepayment Outstanding Overview",
            "Portfolio snapshot of outstanding prepayments grouped for financial reporting.",
            "Prepayments",
            "reports/view/prepayment-outstanding-overview",
            "api/prepayments/prepayment-outstanding-overview-report-tuples",
            true,
            List.of(
                filter("Reporting Month", "fiscalMonthId.equals", "fiscalMonths", "dropdown"),
                filter("Prepayment Account", "prepaymentAccountId.equals", "prepaymentAccounts", "typeahead")
            )
        ),
        new ReportMetadataSeed(
            "Prepayment Amortisation Schedule",
            "Detail of prepayment amortisation entries scheduled for a selected period.",
            "Prepayments",
            "reports/view/prepayment-amortisation",
            "api/prepayment-amortizations",
            true,
            List.of(
                filter("Schedule Period", "fiscalMonthId.equals", "fiscalMonths", "dropdown"),
                filter("Prepayment Account", "prepaymentAccountId.equals", "prepaymentAccounts", "typeahead")
            )
        ),
        new ReportMetadataSeed(
            "Amortisation Posting Summary",
            "Summary of amortisation postings grouped by recognition rule for reconciliation.",
            "Amortisation",
            "reports/view/amortisation-posting-summary",
            "api/amortization-posting-reports",
            true,
            List.of(
                filter("Posting Period", "fiscalMonthId.equals", "fiscalMonths", "dropdown"),
                filter("Recognition Rule", "recognitionRuleId.equals", "amortizationRules", "dropdown")
            )
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
            dto.setFilters(seed.toFilterDtos());
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
        dto.setFilters(reportMetadata.getFilters().stream().map(this::toFilterDto).collect(Collectors.toList()));
        return dto;
    }

    private ReportFilterDefinitionDTO toFilterDto(ReportFilterDefinition definition) {
        ReportFilterDefinitionDTO dto = new ReportFilterDefinitionDTO();
        dto.setLabel(definition.getLabel());
        dto.setQueryParameterKey(definition.getQueryParameterKey());
        dto.setValueSource(definition.getValueSource());
        dto.setUiHint(definition.getUiHint());
        return dto;
    }

    private record ReportMetadataSeed(
        String reportTitle,
        String description,
        String module,
        String pagePath,
        String backendApi,
        boolean active,
        List<ReportFilterDescriptor> filters
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
            List<ReportFilterDefinition> desiredFilters = toFilterDefinitions();
            if (!Objects.equals(entity.getFilters(), desiredFilters)) {
                entity.setFilters(new ArrayList<>(desiredFilters));
                changed = true;
            }
            return changed;
        }

        List<ReportFilterDefinitionDTO> toFilterDtos() {
            return filters.stream().map(ReportMetadataSeederExtension::toFilterDto).collect(Collectors.toList());
        }

        List<ReportFilterDefinition> toFilterDefinitions() {
            return filters.stream().map(ReportMetadataSeederExtension::toFilterDefinition).collect(Collectors.toList());
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

    private static ReportFilterDescriptor filter(String label, String queryParameterKey, String valueSource, String uiHint) {
        return new ReportFilterDescriptor(label, queryParameterKey, valueSource, uiHint);
    }

    private static ReportFilterDefinitionDTO toFilterDto(ReportFilterDescriptor descriptor) {
        ReportFilterDefinitionDTO dto = new ReportFilterDefinitionDTO();
        dto.setLabel(descriptor.label());
        dto.setQueryParameterKey(descriptor.queryParameterKey());
        dto.setValueSource(descriptor.valueSource());
        dto.setUiHint(descriptor.uiHint());
        return dto;
    }

    private static ReportFilterDefinition toFilterDefinition(ReportFilterDescriptor descriptor) {
        return new ReportFilterDefinition()
            .label(descriptor.label())
            .queryParameterKey(descriptor.queryParameterKey())
            .valueSource(descriptor.valueSource())
            .uiHint(descriptor.uiHint());
    }

    private record ReportFilterDescriptor(String label, String queryParameterKey, String valueSource, String uiHint) {}
}
