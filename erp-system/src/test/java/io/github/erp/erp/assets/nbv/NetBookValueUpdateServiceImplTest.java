package io.github.erp.erp.assets.nbv;

import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.dto.ServiceOutletDTO;
import io.github.erp.service.mapper.AssetCategoryMapper;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import io.github.erp.service.mapper.DepreciationMethodMapper;
import io.github.erp.service.mapper.DepreciationPeriodMapper;
import io.github.erp.service.mapper.FiscalMonthMapper;
import io.github.erp.service.mapper.NetBookValueEntryMapper;
import io.github.erp.service.mapper.PlaceholderMapper;
import io.github.erp.service.mapper.ServiceOutletMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NetBookValueUpdateServiceImplTest {

    @Mock
    private FiscalMonthMapper fiscalMonthMapper;
    @Mock
    private DepreciationMethodMapper depreciationMethodMapper;
    @Mock
    private AssetRegistrationMapper assetRegistrationMapper;
    @Mock
    private AssetCategoryMapper assetCategoryMapper;
    @Mock
    private PlaceholderMapper placeholderMapper;
    @Mock
    private DepreciationPeriodMapper depreciationPeriodMapper;
    @Mock
    private BufferedSinkProcessor<NetBookValueEntry> bufferedSinkProcessor;
    @Mock
    private NetBookValueEntryMapper netBookValueEntryMapper;
    @Mock
    private ServiceOutletMapper serviceOutletMapper;

    private NetBookValueUpdateServiceImpl netBookValueUpdateService;

    @BeforeEach
    void setUp() {
        netBookValueUpdateService = new NetBookValueUpdateServiceImpl(
            fiscalMonthMapper,
            depreciationMethodMapper,
            assetRegistrationMapper,
            assetCategoryMapper,
            placeholderMapper,
            depreciationPeriodMapper,
            bufferedSinkProcessor,
            netBookValueEntryMapper,
            serviceOutletMapper
        );
    }

    @Test
    void shouldDefaultPreviousNetBookValueToAssetCostWhenMissing() {
        AssetRegistration assetRegistration = buildAssetRegistration(new BigDecimal("123.4567"));
        NBVBatchMessage batchMessage = buildBatchMessage();
        NbvCompilationJob compilationJob = buildCompilationJob();
        NBVArtefact artefact = buildArtefact(new BigDecimal("456.789"), null);

        stubMapperResponses(compilationJob, assetRegistration);

        NetBookValueEntryDTO dto = netBookValueUpdateService.netBookValueUpdate(assetRegistration, batchMessage, compilationJob, artefact);

        assertThat(dto.getPreviousNetBookValueAmount()).isEqualByComparingTo(new BigDecimal("123.46"));
        assertThat(dto.getPreviousNetBookValueAmount().scale()).isEqualTo(2);
        assertThat(dto.getNetBookValueAmount()).isEqualByComparingTo(new BigDecimal("456.79"));
        assertThat(dto.getCompilationJobIdentifier()).isEqualTo(compilationJob.getCompilationJobIdentifier());
        assertThat(dto.getCompilationBatchIdentifier()).isEqualTo(batchMessage.getMessageCorrelationId());
    }

    @Test
    void shouldUseRoundedPreviousNetBookValueWhenPresent() {
        AssetRegistration assetRegistration = buildAssetRegistration(new BigDecimal("100.000"));
        NBVBatchMessage batchMessage = buildBatchMessage();
        NbvCompilationJob compilationJob = buildCompilationJob();
        NBVArtefact artefact = buildArtefact(new BigDecimal("321.123"), new BigDecimal("200.555"));

        stubMapperResponses(compilationJob, assetRegistration);

        NetBookValueEntryDTO dto = netBookValueUpdateService.netBookValueUpdate(assetRegistration, batchMessage, compilationJob, artefact);

        assertThat(dto.getPreviousNetBookValueAmount()).isEqualByComparingTo(new BigDecimal("200.56"));
        assertThat(dto.getPreviousNetBookValueAmount().scale()).isEqualTo(2);
    }

    @Test
    void saveCalculatedEntriesShouldPushEachEntryToBufferedSink() {
        NetBookValueEntryDTO first = new NetBookValueEntryDTO();
        NetBookValueEntryDTO second = new NetBookValueEntryDTO();
        NetBookValueEntry firstEntity = new NetBookValueEntry();
        NetBookValueEntry secondEntity = new NetBookValueEntry();

        when(netBookValueEntryMapper.toEntity(first)).thenReturn(firstEntity);
        when(netBookValueEntryMapper.toEntity(second)).thenReturn(secondEntity);

        netBookValueUpdateService.saveCalculatedEntries(Arrays.asList(first, second));

        verify(bufferedSinkProcessor).addEntry(firstEntity);
        verify(bufferedSinkProcessor).addEntry(secondEntity);
        verifyNoMoreInteractions(bufferedSinkProcessor);
    }

    private AssetRegistration buildAssetRegistration(BigDecimal assetCost) {
        AssetRegistration assetRegistration = new AssetRegistration();
        assetRegistration.setAssetNumber("AN-001");
        assetRegistration.setAssetTag("TAG-001");
        assetRegistration.setAssetDetails("Laptop");
        assetRegistration.setAssetCost(assetCost);
        assetRegistration.setHistoricalCost(new BigDecimal("500.00"));

        AssetCategory assetCategory = new AssetCategory();
        assetCategory.setAssetCategoryName("Electronics");

        DepreciationMethod depreciationMethod = new DepreciationMethod();
        depreciationMethod.setDepreciationMethodName("Straight Line");
        assetCategory.setDepreciationMethod(depreciationMethod);
        assetRegistration.setAssetCategory(assetCategory);

        assetRegistration.setPlaceholders(new HashSet<>());

        ServiceOutlet serviceOutlet = new ServiceOutlet();
        serviceOutlet.setOutletCode("CODE");
        serviceOutlet.setOutletName("Main Outlet");
        assetRegistration.setMainServiceOutlet(serviceOutlet);

        return assetRegistration;
    }

    private NBVBatchMessage buildBatchMessage() {
        NBVBatchMessage batchMessage = new NBVBatchMessage();
        batchMessage.setMessageCorrelationId(UUID.randomUUID());
        return batchMessage;
    }

    private NbvCompilationJob buildCompilationJob() {
        NbvCompilationJob compilationJob = new NbvCompilationJob();
        compilationJob.setCompilationJobIdentifier(UUID.randomUUID());

        DepreciationPeriod depreciationPeriod = new DepreciationPeriod();
        depreciationPeriod.setStartDate(LocalDate.now().minusMonths(1));
        depreciationPeriod.setEndDate(LocalDate.now());
        FiscalMonth fiscalMonth = new FiscalMonth();
        fiscalMonth.setMonthNumber(1);
        fiscalMonth.setStartDate(LocalDate.now().withDayOfMonth(1));
        fiscalMonth.setEndDate(LocalDate.now().withDayOfMonth(28));
        fiscalMonth.setFiscalMonthCode("2024-01");
        depreciationPeriod.setFiscalMonth(fiscalMonth);
        compilationJob.setActivePeriod(depreciationPeriod);

        return compilationJob;
    }

    private NBVArtefact buildArtefact(BigDecimal netBookValueAmount, BigDecimal previousNetBookValueAmount) {
        NBVArtefact artefact = new NBVArtefact();
        artefact.setNetBookValueAmount(netBookValueAmount);
        artefact.setPreviousNetBookValueAmount(previousNetBookValueAmount);
        artefact.setElapsedMonths(12L);
        artefact.setPriorMonths(6L);
        artefact.setUsefulLifeYears(new BigDecimal("5"));
        artefact.setCapitalizationDate(LocalDate.now());
        return artefact;
    }

    private void stubMapperResponses(NbvCompilationJob compilationJob, AssetRegistration assetRegistration) {
        when(fiscalMonthMapper.toDto(compilationJob.getActivePeriod().getFiscalMonth())).thenReturn(new FiscalMonthDTO());
        when(depreciationMethodMapper.toDto(assetRegistration.getAssetCategory().getDepreciationMethod()))
            .thenReturn(new DepreciationMethodDTO());
        when(assetRegistrationMapper.toDto(assetRegistration)).thenReturn(new AssetRegistrationDTO());
        when(assetCategoryMapper.toDto(assetRegistration.getAssetCategory())).thenReturn(new AssetCategoryDTO());
        when(placeholderMapper.toDtoIdSet(assetRegistration.getPlaceholders())).thenReturn(new HashSet<>());
        when(depreciationPeriodMapper.toDto(compilationJob.getActivePeriod())).thenReturn(new DepreciationPeriodDTO());
        when(serviceOutletMapper.toDto(assetRegistration.getMainServiceOutlet())).thenReturn(new ServiceOutletDTO());
    }
}
