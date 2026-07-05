package io.github.erp.internal.service.rou;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.erp.service.dto.RouDepreciationRequestDTO;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ROUDepreciationEntryCompilationJobImplTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job rouDepreciationEntryPersistenceJob;

    @Mock
    private InternalRouDepreciationRequestService internalRouDepreciationRequestService;

    private ROUDepreciationEntryCompilationJobImpl job;

    @BeforeEach
    void setUp() {
        job = new ROUDepreciationEntryCompilationJobImpl(
            jobLauncher,
            rouDepreciationEntryPersistenceJob,
            internalRouDepreciationRequestService
        );
    }

    @Test
    void compileShouldLaunchBatchJobAndMarkRequestComplete() throws Exception {
        RouDepreciationRequestDTO requestDTO = new RouDepreciationRequestDTO();
        requestDTO.setId(15L);

        RouDepreciationRequestDTO requestWithIdentifier = new RouDepreciationRequestDTO();
        requestWithIdentifier.setId(requestDTO.getId());

        when(jobLauncher.run(eq(rouDepreciationEntryPersistenceJob), any(JobParameters.class)))
            .thenReturn(Mockito.mock(JobExecution.class));
        when(internalRouDepreciationRequestService.saveIdentifier(eq(requestDTO), any(UUID.class)))
            .thenAnswer(invocation -> {
                UUID identifier = invocation.getArgument(1);
                requestWithIdentifier.setBatchJobIdentifier(identifier);
                return requestWithIdentifier;
            });
        when(internalRouDepreciationRequestService.markRequestComplete(requestWithIdentifier))
            .thenReturn(requestWithIdentifier);

        job.compileROUDepreciationEntries(requestDTO);

        ArgumentCaptor<JobParameters> jobParametersCaptor = ArgumentCaptor.forClass(JobParameters.class);
        verify(jobLauncher).run(eq(rouDepreciationEntryPersistenceJob), jobParametersCaptor.capture());
        JobParameters jobParameters = jobParametersCaptor.getValue();

        assertThat(jobParameters.getLong("rouDepreciationRequestId")).isEqualTo(requestDTO.getId());
        String batchIdentifier = jobParameters.getString("batchJobIdentifier");
        assertThat(batchIdentifier).isNotBlank();
        assertThat(jobParameters.getString("jobToken")).isNotBlank();

        ArgumentCaptor<UUID> identifierCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(internalRouDepreciationRequestService).saveIdentifier(eq(requestDTO), identifierCaptor.capture());
        UUID generatedIdentifier = identifierCaptor.getValue();
        assertThat(generatedIdentifier).isEqualTo(UUID.fromString(batchIdentifier));

        verify(internalRouDepreciationRequestService).markRequestComplete(requestWithIdentifier);
    }

    @ParameterizedTest
    @MethodSource("jobLauncherExceptions")
    void compileShouldAbortWhenBatchJobFails(Exception exception) throws Exception {
        reset(internalRouDepreciationRequestService);
        RouDepreciationRequestDTO requestDTO = new RouDepreciationRequestDTO();
        requestDTO.setId(25L);

        when(jobLauncher.run(eq(rouDepreciationEntryPersistenceJob), any(JobParameters.class))).thenThrow(exception);

        job.compileROUDepreciationEntries(requestDTO);

        verify(internalRouDepreciationRequestService, never()).saveIdentifier(any(RouDepreciationRequestDTO.class), any(UUID.class));
        verify(internalRouDepreciationRequestService, never()).markRequestComplete(any(RouDepreciationRequestDTO.class));
    }

    private static Stream<Exception> jobLauncherExceptions() {
        return Stream.of(
            new JobExecutionAlreadyRunningException("running"),
            new JobRestartException("restart"),
            new JobInstanceAlreadyCompleteException("complete"),
            new JobParametersInvalidException("invalid"),
            new IllegalArgumentException("illegal")
        );
    }
}
