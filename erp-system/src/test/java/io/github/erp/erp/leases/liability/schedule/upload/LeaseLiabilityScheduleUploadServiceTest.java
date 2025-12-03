package io.github.erp.erp.leases.liability.schedule.upload;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import io.github.erp.domain.CsvFileUpload;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityScheduleFileUpload;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationScheduleService;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityCompilationService;
import io.github.erp.repository.CsvFileUploadRepository;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.LeaseLiabilityScheduleFileUploadRepository;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
import io.github.erp.service.mapper.LeaseLiabilityMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

class LeaseLiabilityScheduleUploadServiceTest {
//
//    @Mock
//    private FileStorageService storageService;
//
//    @Mock
//    private CsvFileUploadRepository csvFileUploadRepository;
//
//    @Mock
//    private LeaseLiabilityScheduleFileUploadRepository leaseLiabilityScheduleFileUploadRepository;
//
//    @Mock
//    private LeaseLiabilityRepository leaseLiabilityRepository;
//
//    @Mock
//    private InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService;
//
//    @Mock
//    private InternalLeaseAmortizationScheduleService leaseAmortizationScheduleService;
//
//    @Mock
//    private LeaseLiabilityMapper leaseLiabilityMapper;
//
//    @Mock
//    private IFRS16LeaseContractMapper ifrs16LeaseContractMapper;
//
//    @Mock
//    private LeaseLiabilityScheduleUploadJobLauncher jobLauncher;
//
//    private LeaseLiabilityScheduleUploadService leaseLiabilityScheduleUploadService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        leaseLiabilityScheduleUploadService = new LeaseLiabilityScheduleUploadService(
//            storageService,
//            csvFileUploadRepository,
//            leaseLiabilityScheduleFileUploadRepository,
//            leaseLiabilityRepository,
//            leaseLiabilityCompilationService,
//            leaseAmortizationScheduleService,
//            leaseLiabilityMapper,
//            ifrs16LeaseContractMapper,
//            jobLauncher,
//            "./build/tmp"
//        );
//    }
//
//    @Test
//    void shouldActivateGeneratedCompilationWhenCreatingSchedule() throws Exception {
//        LeaseLiabilityScheduleUploadRequest request = new LeaseLiabilityScheduleUploadRequest();
//        request.setLeaseLiabilityId(42L);
//        request.setLaunchBatchImmediately(false);
//
//        LeaseLiability leaseLiability = new LeaseLiability();
//        leaseLiability.setId(42L);
//        leaseLiability.setLeaseContract(new IFRS16LeaseContract());
//        when(leaseLiabilityRepository.findById(42L)).thenReturn(Optional.of(leaseLiability));
//
//        LeaseLiabilityDTO leaseLiabilityDTO = new LeaseLiabilityDTO();
//        when(leaseLiabilityMapper.toDto(leaseLiability)).thenReturn(leaseLiabilityDTO);
//        when(ifrs16LeaseContractMapper.toDto(any(IFRS16LeaseContract.class))).thenReturn(new IFRS16LeaseContractDTO());
//
//        when(csvFileUploadRepository.save(any(CsvFileUpload.class))).thenAnswer(invocation -> {
//            CsvFileUpload upload = invocation.getArgument(0);
//            upload.setId(1L);
//            return upload;
//        });
//        when(leaseLiabilityScheduleFileUploadRepository.save(any(LeaseLiabilityScheduleFileUpload.class))).thenAnswer(invocation -> {
//            LeaseLiabilityScheduleFileUpload upload = invocation.getArgument(0);
//            upload.setId(10L);
//            return upload;
//        });
//
//        LeaseLiabilityCompilationDTO compilationResult = new LeaseLiabilityCompilationDTO();
//        compilationResult.setId(7L);
//        ArgumentCaptor<LeaseLiabilityCompilationDTO> compilationCaptor = ArgumentCaptor.forClass(LeaseLiabilityCompilationDTO.class);
//        when(leaseLiabilityCompilationService.save(compilationCaptor.capture())).thenReturn(compilationResult);
//
//        LeaseAmortizationScheduleDTO scheduleResult = new LeaseAmortizationScheduleDTO();
//        scheduleResult.setId(9L);
//        scheduleResult.setActive(Boolean.TRUE);
//        when(leaseAmortizationScheduleService.save(any(LeaseAmortizationScheduleDTO.class))).thenReturn(scheduleResult);
//
//        doNothing().when(storageService).save(any(byte[].class), any(String.class));
//
//        MockMultipartFile multipartFile = new MockMultipartFile(
//            "file",
//            "schedule.csv",
//            "text/csv",
//            "header1,header2\nvalue1,value2".getBytes()
//        );
//
//        leaseLiabilityScheduleUploadService.handleUpload(request, multipartFile);
//
//        LeaseLiabilityCompilationDTO capturedCompilation = compilationCaptor.getValue();
//        assertThat(capturedCompilation.getActive()).isTrue();
//    }
}

