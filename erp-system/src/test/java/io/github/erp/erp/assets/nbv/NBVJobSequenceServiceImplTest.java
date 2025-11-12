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

import static io.github.erp.domain.enumeration.NVBCompilationStatus.COMPLETE;
import static io.github.erp.domain.enumeration.NVBCompilationStatus.ENQUEUED;
import static io.github.erp.domain.enumeration.NVBCompilationStatus.RUNNING;
import static io.github.erp.domain.enumeration.NVBCompilationStatus.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.domain.enumeration.NVBCompilationStatus;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.erp.assets.nbv.queue.NBVBatchProducer;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.service.NbvCompilationBatchService;
import io.github.erp.service.NbvCompilationJobService;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NBVJobSequenceServiceImplTest {

    @Mock
    private NbvCompilationJobService nbvCompilationJobService;

    @Mock
    private InternalAssetRegistrationRepository internalAssetRegistrationRepository;

    @Mock
    private NBVBatchProducer nbvBatchProducer;

    @Mock
    private NbvCompilationBatchService nbvCompilationBatchService;

    @Mock
    private BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor;

    private NBVJobSequenceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new NBVJobSequenceServiceImpl(
            nbvCompilationJobService,
            internalAssetRegistrationRepository,
            nbvBatchProducer,
            nbvCompilationBatchService,
            netBookValueEntryBufferedSinkProcessor
        );
    }

    @Test
    void triggerJobStart_enqueuesBatchesAndUpdatesStatus() {
        NbvCompilationJobDTO job = createJob(STARTED);
        List<Long> assetIds = Arrays.asList(10L, 11L, 12L);

        when(internalAssetRegistrationRepository.getAssetIdsByCapitalizationDateBefore(eq(job.getActivePeriod().getEndDate())))
            .thenReturn(assetIds);
        when(nbvCompilationBatchService.save(any(NbvCompilationBatchDTO.class)))
            .thenAnswer(invocation -> {
                NbvCompilationBatchDTO batch = invocation.getArgument(0);
                batch.setId(25L);
                return batch;
            });

        List<NVBCompilationStatus> savedStatuses = new ArrayList<>();
        doAnswer(invocation -> {
            NbvCompilationJobDTO savedJob = invocation.getArgument(0);
            savedStatuses.add(savedJob.getCompilationStatus());
            return savedJob;
        })
            .when(nbvCompilationJobService)
            .save(any(NbvCompilationJobDTO.class));

        service.triggerJobStart(job);

        InOrder sinkOrder = inOrder(netBookValueEntryBufferedSinkProcessor);
        sinkOrder.verify(netBookValueEntryBufferedSinkProcessor).startup();
        verify(netBookValueEntryBufferedSinkProcessor, never()).shutdown();

        verify(nbvCompilationJobService, times(2)).save(any(NbvCompilationJobDTO.class));
        assertThat(savedStatuses).containsExactly(RUNNING, ENQUEUED);

        ArgumentCaptor<NbvCompilationBatchDTO> batchCaptor = ArgumentCaptor.forClass(NbvCompilationBatchDTO.class);
        verify(nbvCompilationBatchService).save(batchCaptor.capture());
        NbvCompilationBatchDTO persistedBatch = batchCaptor.getValue();
        assertThat(persistedBatch.getStartIndex()).isEqualTo(0);
        assertThat(persistedBatch.getEndIndex()).isEqualTo(assetIds.size() - 1);
        assertThat(persistedBatch.getIsLastBatch()).isTrue();

        ArgumentCaptor<NBVBatchMessage> messageCaptor = ArgumentCaptor.forClass(NBVBatchMessage.class);
        verify(nbvBatchProducer).sendJobMessage(messageCaptor.capture());
        NBVBatchMessage message = messageCaptor.getValue();
        assertThat(message.getStartIndex()).isEqualTo(0);
        assertThat(message.getEndIndex()).isEqualTo(assetIds.size() - 1);
        assertThat(message.isLastBatch()).isTrue();
        assertThat(message.getSequenceNumber()).isEqualTo(1);
        assertThat(message.getNumberOfBatches()).isEqualTo(1);
        assertThat(message.getAssetIds()).containsExactlyElementsOf(assetIds);

        assertThat(job.getCompilationStatus()).isEqualTo(ENQUEUED);
    }

    @Test
    void triggerJobStart_whenJobAlreadyComplete_shutsDownBufferOnly() {
        NbvCompilationJobDTO job = createJob(COMPLETE);

        service.triggerJobStart(job);

        InOrder sinkOrder = inOrder(netBookValueEntryBufferedSinkProcessor);
        sinkOrder.verify(netBookValueEntryBufferedSinkProcessor).startup();
        sinkOrder.verify(netBookValueEntryBufferedSinkProcessor).flushStuckTaskComplete();
        sinkOrder.verify(netBookValueEntryBufferedSinkProcessor).shutdown();
        sinkOrder.verifyNoMoreInteractions();

        verifyNoInteractions(nbvCompilationJobService, internalAssetRegistrationRepository, nbvCompilationBatchService, nbvBatchProducer);
        assertThat(job.getCompilationStatus()).isEqualTo(COMPLETE);
    }

    @Test
    void triggerJobStart_whenNoAssetsFound_marksJobEnqueuedWithoutBatches() {
        NbvCompilationJobDTO job = createJob(STARTED);
        when(internalAssetRegistrationRepository.getAssetIdsByCapitalizationDateBefore(eq(job.getActivePeriod().getEndDate())))
            .thenReturn(Collections.emptyList());

        service.triggerJobStart(job);

        verify(nbvCompilationJobService, times(2)).save(any(NbvCompilationJobDTO.class));
        verify(nbvCompilationBatchService, never()).save(any(NbvCompilationBatchDTO.class));
        verify(nbvBatchProducer, never()).sendJobMessage(any(NBVBatchMessage.class));
        assertThat(job.getCompilationStatus()).isEqualTo(ENQUEUED);
    }

    private NbvCompilationJobDTO createJob(io.github.erp.domain.enumeration.NVBCompilationStatus status) {
        NbvCompilationJobDTO job = new NbvCompilationJobDTO();
        job.setId(1L);
        job.setCompilationJobIdentifier(UUID.randomUUID());
        job.setCompilationJobTimeOfRequest(ZonedDateTime.now());
        job.setCompilationStatus(status);
        job.setJobTitle("Test job");

        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setId(2L);
        period.setStartDate(LocalDate.now().minusMonths(1));
        period.setEndDate(LocalDate.now());
        period.setPeriodCode("PERIOD-001");

        FiscalMonthDTO fiscalMonth = new FiscalMonthDTO();
        fiscalMonth.setId(3L);
        fiscalMonth.setMonthNumber(1);
        fiscalMonth.setStartDate(period.getStartDate());
        fiscalMonth.setEndDate(period.getEndDate());
        fiscalMonth.setFiscalMonthCode("JAN-2024");

        period.setFiscalMonth(fiscalMonth);
        job.setActivePeriod(period);
        return job;
    }
}
