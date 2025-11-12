package io.github.erp.erp.assets.nbv;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.internal.repository.InternalNbvCompilationJobRepository;
import io.github.erp.internal.service.assets.InternalDepreciationPeriodService;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchSequenceNBVCompilationServiceImplTest {

    @Mock
    private NBVCalculatorService nbvCalculatorService;

    @Mock
    private AssetRegistrationRepository assetRegistrationRepository;

    @Mock
    private NetBookValueUpdateService netBookValueUpdateService;

    @Mock
    private InternalNbvCompilationJobRepository internalNbvCompilationJobRepository;

    @Mock
    private InternalDepreciationPeriodService internalDepreciationPeriodService;

    private BatchSequenceNBVCompilationServiceImpl batchSequenceNBVCompilationService;

    @BeforeEach
    void setUp() {
        batchSequenceNBVCompilationService = new BatchSequenceNBVCompilationServiceImpl(
            nbvCalculatorService,
            assetRegistrationRepository,
            netBookValueUpdateService,
            internalNbvCompilationJobRepository,
            internalDepreciationPeriodService
        );
    }

    @Test
    void compileProcessesEachAssetAndSavesCalculatedEntries() {
        Long jobId = 123L;
        Long periodId = 456L;
        Long firstAssetId = 1L;
        Long secondAssetId = 2L;

        NBVBatchMessage nbvBatchMessage = NBVBatchMessage
            .builder()
            .jobId(jobId)
            .batchId(99L)
            .sequenceNumber(5)
            .assetIds(Arrays.asList(firstAssetId, secondAssetId))
            .messageCorrelationId(UUID.randomUUID())
            .build();

        NbvCompilationJob compilationJob = new NbvCompilationJob();
        compilationJob.setId(jobId);
        DepreciationPeriod activePeriod = new DepreciationPeriod();
        activePeriod.setId(periodId);
        compilationJob.setActivePeriod(activePeriod);

        DepreciationPeriodDTO depreciationPeriodDTO = new DepreciationPeriodDTO();
        depreciationPeriodDTO.setId(periodId);

        AssetRegistration firstAsset = new AssetRegistration();
        firstAsset.setId(firstAssetId);
        AssetRegistration secondAsset = new AssetRegistration();
        secondAsset.setId(secondAssetId);

        NBVArtefact firstArtefact = NBVArtefact.builder().build();
        NBVArtefact secondArtefact = NBVArtefact.builder().build();

        NetBookValueEntryDTO firstEntry = new NetBookValueEntryDTO();
        firstEntry.setNbvIdentifier(UUID.randomUUID());
        NetBookValueEntryDTO secondEntry = new NetBookValueEntryDTO();
        secondEntry.setNbvIdentifier(UUID.randomUUID());

        when(internalNbvCompilationJobRepository.findOneWithEagerRelationships(jobId)).thenReturn(Optional.of(compilationJob));
        when(internalDepreciationPeriodService.findOne(periodId)).thenReturn(Optional.of(depreciationPeriodDTO));
        when(assetRegistrationRepository.findOneWithEagerRelationships(firstAssetId)).thenReturn(Optional.of(firstAsset));
        when(assetRegistrationRepository.findOneWithEagerRelationships(secondAssetId)).thenReturn(Optional.of(secondAsset));
        when(nbvCalculatorService.calculateNetBookValue(firstAsset, nbvBatchMessage, depreciationPeriodDTO)).thenReturn(firstArtefact);
        when(nbvCalculatorService.calculateNetBookValue(secondAsset, nbvBatchMessage, depreciationPeriodDTO)).thenReturn(secondArtefact);
        when(netBookValueUpdateService.netBookValueUpdate(firstAsset, nbvBatchMessage, compilationJob, firstArtefact)).thenReturn(firstEntry);
        when(netBookValueUpdateService.netBookValueUpdate(secondAsset, nbvBatchMessage, compilationJob, secondArtefact)).thenReturn(secondEntry);

        batchSequenceNBVCompilationService.compile(nbvBatchMessage);

        verify(nbvCalculatorService).calculateNetBookValue(firstAsset, nbvBatchMessage, depreciationPeriodDTO);
        verify(nbvCalculatorService).calculateNetBookValue(secondAsset, nbvBatchMessage, depreciationPeriodDTO);
        verify(netBookValueUpdateService).netBookValueUpdate(firstAsset, nbvBatchMessage, compilationJob, firstArtefact);
        verify(netBookValueUpdateService).netBookValueUpdate(secondAsset, nbvBatchMessage, compilationJob, secondArtefact);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<NetBookValueEntryDTO>> entriesCaptor = ArgumentCaptor.forClass(List.class);
        verify(netBookValueUpdateService).saveCalculatedEntries(entriesCaptor.capture());

        assertThat(entriesCaptor.getValue()).containsExactly(firstEntry, secondEntry);
    }

    @Test
    void compileExitsEarlyWhenDependenciesReturnEmpty() {
        Long jobId = 321L;

        NBVBatchMessage nbvBatchMessage = NBVBatchMessage
            .builder()
            .jobId(jobId)
            .assetIds(Arrays.asList(10L, 20L))
            .messageCorrelationId(UUID.randomUUID())
            .build();

        when(internalNbvCompilationJobRepository.findOneWithEagerRelationships(jobId)).thenReturn(Optional.empty());

        batchSequenceNBVCompilationService.compile(nbvBatchMessage);

        verify(internalDepreciationPeriodService, never()).findOne(anyLong());
        verify(assetRegistrationRepository, never()).findOneWithEagerRelationships(anyLong());
        verify(nbvCalculatorService, never()).calculateNetBookValue(any(), any(), any());
        verify(netBookValueUpdateService, never()).netBookValueUpdate(any(), any(), any(), any());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<NetBookValueEntryDTO>> entriesCaptor = ArgumentCaptor.forClass(List.class);
        verify(netBookValueUpdateService).saveCalculatedEntries(entriesCaptor.capture());
        assertThat(entriesCaptor.getValue()).isEmpty();
    }
}
