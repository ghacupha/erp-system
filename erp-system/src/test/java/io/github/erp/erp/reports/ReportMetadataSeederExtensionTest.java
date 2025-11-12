package io.github.erp.erp.reports;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.erp.domain.ReportFilterDefinition;
import io.github.erp.domain.ReportMetadata;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.service.ReportMetadataService;
import io.github.erp.service.dto.ReportFilterDefinitionDTO;
import io.github.erp.service.dto.ReportMetadataDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

@ExtendWith(MockitoExtension.class)
class ReportMetadataSeederExtensionTest {

    @Mock
    private ReportMetadataRepository reportMetadataRepository;

    @Mock
    private ReportMetadataService reportMetadataService;

    private ReportMetadataSeederExtension seeder;

    @BeforeEach
    void setUp() {
        seeder = new ReportMetadataSeederExtension(reportMetadataRepository, reportMetadataService);
    }

    @Test
    void shouldCreateReportMetadataWhenNoneExist() {
        when(reportMetadataRepository.findOneByPagePath(anyString())).thenReturn(Optional.empty());
        when(reportMetadataRepository.findOneByReportTitle(anyString())).thenReturn(Optional.empty());

        seeder.run(new DefaultApplicationArguments(new String[0]));

        ArgumentCaptor<ReportMetadataDTO> captor = ArgumentCaptor.forClass(ReportMetadataDTO.class);
        verify(reportMetadataService, atLeastOnce()).save(captor.capture());
        ReportMetadataDTO firstSeed = captor.getAllValues().get(0);

        assertThat(firstSeed.getPagePath()).isEqualTo("reports/view/lease-liability-by-account");
        assertThat(firstSeed.getBackendApi()).isEqualTo("api/leases/lease-liability-by-account-report-items");
        assertThat(firstSeed.getBackendApi()).doesNotStartWith("/");
        assertThat(firstSeed.getActive()).isTrue();
    }

    @Test
    void shouldUpdateExistingMetadataWhenAttributesDiffer() {
        ReportMetadata existing = new ReportMetadata();
        existing.setId(42L);
        existing.setReportTitle("Old Title");
        existing.setDescription("Old description");
        existing.setModule("Legacy");
        existing.setPagePath("reports/view/lease-liability-by-account");
        existing.setBackendApi("/legacy/path");
        existing.setActive(Boolean.FALSE);
        existing.setFilters(List.of(new ReportFilterDefinition().label("Old").queryParameterKey("old").valueSource("old").uiHint("old")));

        when(reportMetadataRepository.findOneByPagePath("reports/view/lease-liability-by-account"))
            .thenReturn(Optional.of(existing));
        when(reportMetadataRepository.findOneByPagePath(anyString())).thenReturn(Optional.empty());
        when(reportMetadataRepository.findOneByReportTitle(anyString())).thenReturn(Optional.empty());

        seeder.run(new DefaultApplicationArguments(new String[0]));

        ArgumentCaptor<ReportMetadataDTO> captor = ArgumentCaptor.forClass(ReportMetadataDTO.class);
        verify(reportMetadataService, atLeastOnce()).save(captor.capture());

        ReportMetadataDTO updated = captor.getAllValues().stream()
            .filter(dto -> "Lease Liability by Account".equals(dto.getReportTitle()))
            .findFirst()
            .orElseThrow();

        assertThat(updated.getId()).isEqualTo(42L);
        assertThat(updated.getReportTitle()).isEqualTo("Lease Liability by Account");
        assertThat(updated.getDescription())
            .isEqualTo("Aggregated lease liability positions grouped by GL account with the latest recognised balances.");
        assertThat(updated.getModule()).isEqualTo("Leases");
        assertThat(updated.getPagePath()).isEqualTo("reports/view/lease-liability-by-account");
        assertThat(updated.getBackendApi()).isEqualTo("api/leases/lease-liability-by-account-report-items");
        assertThat(updated.getActive()).isTrue();
        assertThat(updated.getFilters())
            .hasSize(1)
            .map(ReportFilterDefinitionDTO::getLabel)
            .containsExactly("Lease Period");
    }

    @Test
    void shouldNotPersistWhenExistingMetadataMatchesSeed() {
        when(reportMetadataRepository.findOneByPagePath(anyString())).thenAnswer(invocation -> {
            String pagePath = invocation.getArgument(0);
            SeedDefinition definition = SEEDS_BY_PAGE_PATH.get(pagePath);
            if (definition == null) {
                return Optional.empty();
            }
            return Optional.of(definition.toEntity());
        });
        when(reportMetadataRepository.findOneByReportTitle(anyString())).thenReturn(Optional.empty());

        seeder.run(new DefaultApplicationArguments(new String[0]));

        verifyNoInteractions(reportMetadataService);
    }

    private static final Map<String, SeedDefinition> SEEDS_BY_PAGE_PATH = Map.ofEntries(
        Map.entry(
            "reports/view/lease-liability-by-account",
            new SeedDefinition(
                "Lease Liability by Account",
                "Aggregated lease liability positions grouped by GL account with the latest recognised balances.",
                "Leases",
                "api/leases/lease-liability-by-account-report-items",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"))
            )
        ),
        Map.entry(
            "lease-liability-schedule-report/report-nav",
            new SeedDefinition(
                "Lease Liability Schedule Dashboard",
                "Detailed amortisation schedule rows for the selected IFRS16 lease contract, including cash and interest movements.",
                "Leases",
                "api/leases/lease-liability-schedule-items",
                true,
                List.of(new FilterDefinition("Lease contract", "leaseContractId.equals", "leaseContracts", "typeahead"))
            )
        ),
        Map.entry(
            "reports/view/rou-account-balance",
            new SeedDefinition(
                "ROU Asset Balance by Account",
                "Right-of-use asset positions summarised by asset account including net book values per fiscal period.",
                "Leases",
                "api/leases/rou-account-balance-report-items/reports/{leasePeriodId}",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId", "leasePeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/rou-depreciation-by-account",
            new SeedDefinition(
                "ROU Depreciation by Account",
                "Scheduled depreciation postings for right-of-use assets broken down by transaction account.",
                "Leases",
                "api/leases/rou-depreciation-posting-report-items",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/lease-liability-by-outlet",
            new SeedDefinition(
                "Lease Liability by Service Outlet",
                "Lease liability schedule totals grouped by service outlet for detailed operational analysis.",
                "Leases",
                "api/leases/lease-liability-schedule-report-items",
                false,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/lease-liability-by-dealer",
            new SeedDefinition(
                "Lease Liability by Dealer",
                "Comparative view of liability balances per dealer contract within a selected period.",
                "Leases",
                "api/leases/lease-liability-report-items",
                false,
                List.of(
                    new FilterDefinition("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"),
                    new FilterDefinition("Lease Contract", "leaseLiabilityId.equals", "leaseContracts", "typeahead")
                )
            )
        ),
        Map.entry(
            "reports/view/lease-interest-accrued",
            new SeedDefinition(
                "Lease Interest Accrued by Account",
                "Interest accrued on lease liabilities with drill-down by contra account for reconciliation.",
                "Leases",
                "api/leases/lease-liability-schedule-report-items/interest-expense-summary/{leasePeriodId}",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId", "leaseRepaymentPeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/lease-interest-paid-transfer",
            new SeedDefinition(
                "Lease Interest Paid Transfer",
                "Summary of reversing entries when lease interest payments are transferred between liabilities.",
                "Leases",
                "api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId", "leaseRepaymentPeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/lease-liability-outstanding",
            new SeedDefinition(
                "Lease Liability Outstanding",
                "Outstanding lease liability and interest payable balances grouped by contract for the selected period.",
                "Leases",
                "api/leases/lease-liability-schedule-report-items/liability-outstanding-summary/{leasePeriodId}",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId", "leaseRepaymentPeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/lease-liability-maturity",
            new SeedDefinition(
                "Lease Liability Maturity",
                "Bucketed maturity profile of outstanding lease principal and interest payable for the reporting period.",
                "Leases",
                "api/leases/lease-liability-schedule-report-items/liability-maturity-summary/{leasePeriodId}",
                true,
                List.of(new FilterDefinition("Lease Period", "leasePeriodId", "leaseRepaymentPeriods", "dropdown"))
            )
        ),
        Map.entry(
            "reports/view/lease-interest-payments",
            new SeedDefinition(
                "Lease Interest Payments by Liability",
                "Cash interest settlements grouped by lease liability and transaction account for a chosen period.",
                "Leases",
                "api/leases/lease-liability-schedule-report-items",
                false,
                List.of(
                    new FilterDefinition("Lease Period", "leasePeriodId.equals", "leasePeriods", "dropdown"),
                    new FilterDefinition("Lease Contract", "leaseLiabilityId.equals", "leaseContracts", "typeahead")
                )
            )
        ),
        Map.entry(
            "reports/view/prepayment-outstanding-overview",
            new SeedDefinition(
                "Prepayment Outstanding Overview",
                "Portfolio snapshot of outstanding prepayments grouped for financial reporting.",
                "Prepayments",
                "api/prepayments/prepayment-outstanding-overview-report-tuples",
                true,
                List.of(
                    new FilterDefinition("Reporting Month", "fiscalMonthId.equals", "fiscalMonths", "dropdown"),
                    new FilterDefinition("Prepayment Account", "prepaymentAccountId.equals", "prepaymentAccounts", "typeahead")
                )
            )
        ),
        Map.entry(
            "reports/view/prepayment-amortisation",
            new SeedDefinition(
                "Prepayment Amortisation Schedule",
                "Detail of prepayment amortisation entries scheduled for a selected period.",
                "Prepayments",
                "api/prepayment-amortizations",
                true,
                List.of(
                    new FilterDefinition("Schedule Period", "fiscalMonthId.equals", "fiscalMonths", "dropdown"),
                    new FilterDefinition("Prepayment Account", "prepaymentAccountId.equals", "prepaymentAccounts", "typeahead")
                )
            )
        ),
        Map.entry(
            "reports/view/amortisation-posting-summary",
            new SeedDefinition(
                "Amortisation Posting Summary",
                "Summary of amortisation postings grouped by recognition rule for reconciliation.",
                "Amortisation",
                "api/amortization-posting-reports",
                true,
                List.of(
                    new FilterDefinition("Posting Period", "fiscalMonthId.equals", "fiscalMonths", "dropdown"),
                    new FilterDefinition("Recognition Rule", "recognitionRuleId.equals", "amortizationRules", "dropdown")
                )
            )
        )
    );

    private record SeedDefinition(
        String reportTitle,
        String description,
        String module,
        String backendApi,
        boolean active,
        List<FilterDefinition> filters
    ) {
        ReportMetadata toEntity() {
            ReportMetadata entity = new ReportMetadata();
            entity.setReportTitle(reportTitle);
            entity.setDescription(description);
            entity.setModule(module);
            entity.setPagePath(SEEDS_BY_PAGE_PATH.entrySet().stream()
                .filter(entry -> entry.getValue() == this)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow());
            entity.setBackendApi(backendApi);
            entity.setActive(active);
            entity.setFilters(filters.stream().map(FilterDefinition::toEntity).collect(Collectors.toList()));
            return entity;
        }
    }

    private record FilterDefinition(String label, String queryParameterKey, String valueSource, String uiHint) {
        ReportFilterDefinition toEntity() {
            return new ReportFilterDefinition()
                .label(label)
                .queryParameterKey(queryParameterKey)
                .valueSource(valueSource)
                .uiHint(uiHint);
        }
    }
}
