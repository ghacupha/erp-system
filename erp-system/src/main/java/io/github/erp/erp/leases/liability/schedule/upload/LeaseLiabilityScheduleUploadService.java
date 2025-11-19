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
import io.github.erp.domain.LeaseLiabilityScheduleFileUpload;
import io.github.erp.repository.CsvFileUploadRepository;
import io.github.erp.repository.LeaseLiabilityScheduleFileUploadRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
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
    private final LeaseLiabilityScheduleUploadJobLauncher jobLauncher;
    private final Path storageRoot;

    public LeaseLiabilityScheduleUploadService(
        @Qualifier("csvUploadFSStorageService") FileStorageService storageService,
        CsvFileUploadRepository csvFileUploadRepository,
        LeaseLiabilityScheduleFileUploadRepository leaseLiabilityScheduleFileUploadRepository,
        LeaseLiabilityScheduleUploadJobLauncher jobLauncher,
        @Value("${erp.csv-upload.storage-path:${java.io.tmpdir}/erp/csv-uploads}") String storagePath
    ) {
        this.storageService = storageService;
        this.csvFileUploadRepository = csvFileUploadRepository;
        this.leaseLiabilityScheduleFileUploadRepository = leaseLiabilityScheduleFileUploadRepository;
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

            LeaseLiabilityScheduleFileUpload upload = new LeaseLiabilityScheduleFileUpload()
                .leaseLiabilityId(request.getLeaseLiabilityId())
                .leaseAmortizationScheduleId(request.getLeaseAmortizationScheduleId())
                .leaseLiabilityCompilationId(request.getLeaseLiabilityCompilationId())
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

}

