package io.github.erp.erp.assets.depreciation;

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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.erp.assets.depreciation.context.DepreciationJobContext;
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.internal.repository.InternalNetBookValueEntryRepository;
import io.github.erp.internal.service.assets.InternalDepreciationEntryService;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.FiscalQuarterDTO;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.dto.ServiceOutletDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@IntegrationTest
class BatchSequenceDepreciationServiceIT {

    @Autowired
    private BatchSequenceDepreciationService batchSequenceDepreciationService;

    @Autowired
    private DepreciationEntrySinkProcessor depreciationEntrySinkProcessor;

    @Autowired
    private DepreciationNetBookValueService depreciationNetBookValueService;

    @MockBean
    private InternalDepreciationEntryService internalDepreciationEntryService;

    @MockBean
    private InternalNetBookValueEntryRepository internalNetBookValueEntryRepository;

    @BeforeEach
    void resetBuffers() {
        depreciationEntrySinkProcessor.startup();
        depreciationNetBookValueService.startupBuffer();
        Mockito.reset(internalDepreciationEntryService, internalNetBookValueEntryRepository);
    }

    @Test
    void recordDepreciationEntryEnqueuesDepreciationAndNetBookValue() throws Exception {
        CountDownLatch depreciationLatch = new CountDownLatch(1);
        CountDownLatch nbvLatch = new CountDownLatch(1);

        AtomicReference<List<DepreciationEntry>> depreciationEntries = new AtomicReference<>();
        AtomicReference<List<NetBookValueEntry>> nbvEntries = new AtomicReference<>();

        doAnswer(invocation -> {
                List<DepreciationEntry> entries = invocation.getArgument(0);
                depreciationEntries.set(entries);
                depreciationLatch.countDown();
                return null;
            })
            .when(internalDepreciationEntryService)
            .saveAll(anyList());

        doAnswer(invocation -> {
                List<NetBookValueEntry> entries = invocation.getArgument(0);
                nbvEntries.set(entries);
                nbvLatch.countDown();
                return null;
            })
            .when(internalNetBookValueEntryRepository)
            .saveAll(anyList());

        DepreciationPeriodDTO depreciationPeriod = new DepreciationPeriodDTO();
        depreciationPeriod.setId(1L);
        depreciationPeriod.setStartDate(LocalDate.now().minusMonths(1));
        depreciationPeriod.setEndDate(LocalDate.now());
        depreciationPeriod.setPeriodCode("PERIOD-01");

        FiscalYearDTO fiscalYear = new FiscalYearDTO();
        fiscalYear.setId(2L);
        fiscalYear.setFiscalYearCode("FY-2024");

        FiscalQuarterDTO fiscalQuarter = new FiscalQuarterDTO();
        fiscalQuarter.setId(3L);
        fiscalQuarter.setFiscalQuarterCode("FQ-1");

        FiscalMonthDTO fiscalMonth = new FiscalMonthDTO();
        fiscalMonth.setId(4L);
        fiscalMonth.setMonthNumber(1);
        fiscalMonth.setStartDate(LocalDate.now().minusMonths(1));
        fiscalMonth.setEndDate(LocalDate.now());
        fiscalMonth.setFiscalMonthCode("JAN-2024");
        fiscalMonth.setFiscalYear(fiscalYear);
        fiscalMonth.setFiscalQuarter(fiscalQuarter);

        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setId(5L);
        depreciationMethod.setDepreciationMethodName("STRAIGHT_LINE");

        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setId(6L);
        assetCategory.setAssetCategoryName("COMPUTERS");
        assetCategory.setDepreciationMethod(depreciationMethod);

        ServiceOutletDTO serviceOutlet = new ServiceOutletDTO();
        serviceOutlet.setId(7L);
        serviceOutlet.setOutletCode("HQ");
        serviceOutlet.setOutletName("Headquarters");

        AssetRegistrationDTO assetRegistration = new AssetRegistrationDTO();
        assetRegistration.setId(8L);
        assetRegistration.setAssetNumber("1001");
        assetRegistration.setAssetTag("TAG-1001");
        assetRegistration.setAssetDetails("High-end workstation");
        assetRegistration.setAssetCost(new BigDecimal("10000"));
        assetRegistration.setHistoricalCost(new BigDecimal("10000"));
        assetRegistration.setCapitalizationDate(LocalDate.now().minusYears(1));
        assetRegistration.setMainServiceOutlet(serviceOutlet);
        assetRegistration.setAssetCategory(assetCategory);

        DepreciationJobDTO depreciationJob = new DepreciationJobDTO();
        depreciationJob.setId(9L);
        depreciationJob.setDescription("Monthly depreciation");
        depreciationJob.setTimeOfCommencement(ZonedDateTime.now());

        DepreciationBatchSequenceDTO batchSequence = new DepreciationBatchSequenceDTO();
        batchSequence.setId(10L);
        batchSequence.setDepreciationJobIdentifier(UUID.randomUUID());
        batchSequence.setDepreciationPeriodIdentifier(UUID.randomUUID());

        DepreciationArtefact artefact = DepreciationArtefact
            .builder()
            .depreciationAmount(new BigDecimal("100"))
            .elapsedMonths(12L)
            .priorMonths(11L)
            .usefulLifeYears(BigDecimal.valueOf(5))
            .nbvBeforeDepreciation(new BigDecimal("5000"))
            .nbv(new BigDecimal("4900"))
            .depreciationPeriodStartDate(depreciationPeriod.getStartDate())
            .depreciationPeriodEndDate(depreciationPeriod.getEndDate())
            .capitalizationDate(assetRegistration.getCapitalizationDate())
            .build();

        UUID countdownContextId = DepreciationJobContext.getInstance().createContext(1);

        ReflectionTestUtils.invokeMethod(
            batchSequenceDepreciationService,
            "recordDepreciationEntry",
            depreciationPeriod,
            fiscalMonth,
            assetRegistration,
            assetCategory,
            serviceOutlet,
            depreciationMethod,
            artefact,
            depreciationJob,
            batchSequence,
            countdownContextId
        );

        depreciationEntrySinkProcessor.flushRemainingItems(countdownContextId);
        depreciationNetBookValueService.flushPendingEntries();

        assertThat(depreciationLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(nbvLatch.await(5, TimeUnit.SECONDS)).isTrue();

        assertThat(depreciationEntries.get()).hasSize(1);
        assertThat(nbvEntries.get()).hasSize(1);

        DepreciationEntry persistedDepreciation = depreciationEntries.get().get(0);
        assertThat(persistedDepreciation.getNetBookValue()).isEqualByComparingTo(artefact.getNbv());
        assertThat(persistedDepreciation.getPreviousNBV()).isEqualByComparingTo(artefact.getNbvBeforeDepreciation());

        NetBookValueEntry persistedNetBookValue = nbvEntries.get().get(0);
        assertThat(persistedNetBookValue.getNetBookValueAmount())
            .isEqualByComparingTo(artefact.getNbv().setScale(2, RoundingMode.HALF_EVEN));
        assertThat(persistedNetBookValue.getPreviousNetBookValueAmount())
            .isEqualByComparingTo(artefact.getNbvBeforeDepreciation().setScale(2, RoundingMode.HALF_EVEN));

        DepreciationJobContext.getInstance().removeContext(countdownContextId);
    }
}
