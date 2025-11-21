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

import io.github.erp.domain.CsvFileUpload;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityScheduleFileUpload;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationScheduleService;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityCompilationService;
import io.github.erp.repository.CsvFileUploadRepository;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.LeaseLiabilityScheduleFileUploadRepository;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
import io.github.erp.service.mapper.LeaseLiabilityMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.erp.internal.files.FileStorageService;

@Service
@Transactional
public class LeaseLiabilityScheduleUploadService {

    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleUploadService.class);

    private final FileStorageService storageService;
    private final CsvFileUploadRepository csvFileUploadRepository;
    private final LeaseLiabilityScheduleFileUploadRepository leaseLiabilityScheduleFileUploadRepository;
    private final LeaseLiabilityRepository leaseLiabilityRepository;
    private final InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService;
    private final InternalLeaseAmortizationScheduleService leaseAmortizationScheduleService;
    private final LeaseLiabilityMapper leaseLiabilityMapper;
    private final IFRS16LeaseContractMapper ifrs16LeaseContractMapper;
    private final LeaseLiabilityScheduleUploadJobLauncher jobLauncher;
    private final Path storageRoot;

    public LeaseLiabilityScheduleUploadService(
        @Qualifier("csvUploadFSStorageService") FileStorageService storageService,
        CsvFileUploadRepository csvFileUploadRepository,
        LeaseLiabilityScheduleFileUploadRepository leaseLiabilityScheduleFileUploadRepository,
        LeaseLiabilityRepository leaseLiabilityRepository,
        InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService,
        InternalLeaseAmortizationScheduleService leaseAmortizationScheduleService,
        LeaseLiabilityMapper leaseLiabilityMapper,
        IFRS16LeaseContractMapper ifrs16LeaseContractMapper,
        LeaseLiabilityScheduleUploadJobLauncher jobLauncher,
        @Value("${erp.csv-upload.storage-path:${java.io.tmpdir}/erp/csv-uploads}") String storagePath
    ) {
        this.storageService = storageService;
        this.csvFileUploadRepository = csvFileUploadRepository;
        this.leaseLiabilityScheduleFileUploadRepository = leaseLiabilityScheduleFileUploadRepository;
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseLiabilityCompilationService = leaseLiabilityCompilationService;
        this.leaseAmortizationScheduleService = leaseAmortizationScheduleService;
        this.leaseLiabilityMapper = leaseLiabilityMapper;
        this.ifrs16LeaseContractMapper = ifrs16LeaseContractMapper;
        this.jobLauncher = jobLauncher;
        this.storageRoot = Paths.get(storagePath);
    }

    public LeaseLiabilityScheduleUploadResponse handleUpload(LeaseLiabilityScheduleUploadRequest request, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A CSV file must be provided");
        }

        try {
            log.info("Storing lease liability schedule upload for liability {}", request.getLeaseLiabilityId());
            String storedFileName = buildStoredFileName(file.getOriginalFilename());
            byte[] fileContent = file.getBytes();
            storageService.save(fileContent, storedFileName);
            CsvFileUpload csvFileUpload = persistCsvMetadata(file, storedFileName, fileContent);

            LeaseLiability leaseLiability = resolveLeaseLiability(request.getLeaseLiabilityId());
            IFRS16LeaseContract leaseContract = resolveLeaseContract(leaseLiability);
            Long leaseLiabilityCompilationId = ensureLeaseLiabilityCompilationId(request.getLeaseLiabilityCompilationId());
            Long leaseAmortizationScheduleId = ensureLeaseAmortizationScheduleId(
                request.getLeaseAmortizationScheduleId(),
                leaseLiability,
                leaseContract,
                leaseLiabilityCompilationId
            );

            LeaseLiabilityScheduleFileUpload upload = new LeaseLiabilityScheduleFileUpload()
                .leaseLiabilityId(leaseLiability.getId())
                .leaseAmortizationScheduleId(leaseAmortizationScheduleId)
                .leaseLiabilityCompilationId(leaseLiabilityCompilationId)
                .uploadStatus("PENDING")
                .csvFileUpload(csvFileUpload);
            csvFileUpload.setLeaseLiabilityScheduleFileUpload(upload);

            upload = leaseLiabilityScheduleFileUploadRepository.save(upload);

            if (request.isLaunchBatchImmediately()) {
                launchJobAfterCommit(upload);
            }

            LeaseLiabilityScheduleUploadResponse response = new LeaseLiabilityScheduleUploadResponse();
            response.setUploadId(upload.getId());
            response.setCsvFileId(csvFileUpload.getId());
            response.setStoredFileName(csvFileUpload.getStoredFileName());
            response.setUploadStatus(upload.getUploadStatus());
            return response;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to process uploaded file", e);
        }
    }

    private CsvFileUpload persistCsvMetadata(MultipartFile file, String storedFileName, byte[] fileContent) {
        CsvFileUpload csvFileUpload = new CsvFileUpload();
        csvFileUpload
            .originalFileName(file.getOriginalFilename())
            .storedFileName(storedFileName)
            .filePath(storageRoot.resolve(storedFileName).toString())
            .fileSize((long) fileContent.length)
            .contentType(file.getContentType())
            .uploadedAt(Instant.now())
            .processed(Boolean.FALSE)
            .checksum(DigestUtils.md5DigestAsHex(fileContent));

        return csvFileUploadRepository.save(csvFileUpload);
    }

    private String buildStoredFileName(String originalFilename) {
        String cleanName = originalFilename == null ? "upload.csv" : originalFilename.replaceAll("\\s+", "-");
        return UUID.randomUUID() + "-" + cleanName;
    }

    private void launchJobAfterCommit(LeaseLiabilityScheduleFileUpload upload) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            LeaseLiabilityScheduleFileUpload jobUpload = upload;
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    jobLauncher.launch(jobUpload);
                }
            });
            return;
        }

        jobLauncher.launch(upload);
    }



    private LeaseLiability resolveLeaseLiability(Long leaseLiabilityId) {
        return leaseLiabilityRepository
            .findById(leaseLiabilityId)
            .orElseThrow(() -> new IllegalArgumentException("Lease liability id #" + leaseLiabilityId + " not found"));
    }

    private IFRS16LeaseContract resolveLeaseContract(LeaseLiability leaseLiability) {
        IFRS16LeaseContract contract = leaseLiability.getLeaseContract();
        if (contract == null) {
            throw new IllegalArgumentException("IFRS16 lease contract not linked to lease liability id #" + leaseLiability.getId());
        }
        return contract;
    }

    private Long ensureLeaseLiabilityCompilationId(Long compilationId) {
        if (compilationId != null) {
            return compilationId;
        }
        LeaseLiabilityCompilationDTO compilationDTO = new LeaseLiabilityCompilationDTO();
        compilationDTO.setRequestId(UUID.randomUUID());
        compilationDTO.setTimeOfRequest(ZonedDateTime.now());
        compilationDTO.setActive(Boolean.TRUE);
        return leaseLiabilityCompilationService.save(compilationDTO).getId();
    }

    private Long ensureLeaseAmortizationScheduleId(
        Long scheduleId,
        LeaseLiability leaseLiability,
        IFRS16LeaseContract leaseContract,
        Long leaseLiabilityCompilationId
    ) {
        if (scheduleId != null) {
            return scheduleId;
        }
        LeaseAmortizationScheduleDTO scheduleDTO = new LeaseAmortizationScheduleDTO();
        scheduleDTO.setIdentifier(UUID.randomUUID());
        scheduleDTO.setLeaseLiability(leaseLiabilityMapper.toDto(leaseLiability));
        scheduleDTO.setLeaseContract(ifrs16LeaseContractMapper.toDto(leaseContract));
        scheduleDTO.setActive(Boolean.TRUE);
        if (leaseLiabilityCompilationId != null) {
            LeaseLiabilityCompilationDTO compilationDTO = new LeaseLiabilityCompilationDTO();
            compilationDTO.setId(leaseLiabilityCompilationId);
            scheduleDTO.setLeaseLiabilityCompilation(compilationDTO);
        }
        return leaseAmortizationScheduleService.save(scheduleDTO).getId();
    }

}
