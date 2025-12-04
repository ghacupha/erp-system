package io.github.erp.erp.leases.payments.upload;

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
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeasePaymentUpload;
import io.github.erp.erp.leases.payments.upload.queue.LeasePaymentReindexProducer;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.repository.CsvFileUploadRepository;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.LeasePaymentUploadRepository;
import io.github.erp.repository.search.LeasePaymentUploadSearchRepository;
import io.github.erp.service.dto.LeasePaymentUploadDTO;
import io.github.erp.service.mapper.LeasePaymentUploadMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LeasePaymentUploadService {

    private static final Logger log = LoggerFactory.getLogger(LeasePaymentUploadService.class);

    private final FileStorageService storageService;
    private final CsvFileUploadRepository csvFileUploadRepository;
    private final LeasePaymentUploadRepository leasePaymentUploadRepository;
    private final IFRS16LeaseContractRepository leaseContractRepository;
    private final LeasePaymentUploadMapper leasePaymentUploadMapper;
    private final LeasePaymentUploadSearchRepository leasePaymentUploadSearchRepository;
    private final LeasePaymentUploadJobLauncher jobLauncher;
    private final LeasePaymentRepository leasePaymentRepository;
    private final LeasePaymentReindexProducer leasePaymentReindexProducer;
    private final Path storageRoot;

    public LeasePaymentUploadService(
        @Qualifier("csvUploadFSStorageService") FileStorageService storageService,
        CsvFileUploadRepository csvFileUploadRepository,
        LeasePaymentUploadRepository leasePaymentUploadRepository,
        IFRS16LeaseContractRepository leaseContractRepository,
        LeasePaymentUploadMapper leasePaymentUploadMapper,
        LeasePaymentUploadSearchRepository leasePaymentUploadSearchRepository,
        LeasePaymentUploadJobLauncher jobLauncher,
        LeasePaymentRepository leasePaymentRepository,
        LeasePaymentReindexProducer leasePaymentReindexProducer,
        @Value("${erp.csv-upload.storage-path:${java.io.tmpdir}/erp/csv-uploads}") String storagePath
    ) {
        this.storageService = storageService;
        this.csvFileUploadRepository = csvFileUploadRepository;
        this.leasePaymentUploadRepository = leasePaymentUploadRepository;
        this.leaseContractRepository = leaseContractRepository;
        this.leasePaymentUploadMapper = leasePaymentUploadMapper;
        this.leasePaymentUploadSearchRepository = leasePaymentUploadSearchRepository;
        this.jobLauncher = jobLauncher;
        this.leasePaymentRepository = leasePaymentRepository;
        this.leasePaymentReindexProducer = leasePaymentReindexProducer;
        this.storageRoot = Paths.get(storagePath);
    }

    public LeasePaymentUploadResponse handleUpload(LeasePaymentUploadRequest request, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A CSV file must be provided");
        }

        try {
            log.info("Storing lease payment upload for contract {}", request.getLeaseContractId());
            String storedFileName = buildStoredFileName(file.getOriginalFilename());
            byte[] fileContent = file.getBytes();
            storageService.save(fileContent, storedFileName);
            CsvFileUpload csvFileUpload = persistCsvMetadata(file, storedFileName, fileContent);

            IFRS16LeaseContract contract = resolveLeaseContract(request.getLeaseContractId());

            LeasePaymentUpload upload = new LeasePaymentUpload()
                .leaseContract(contract)
                .csvFileUpload(csvFileUpload)
                .uploadStatus("PENDING")
                .active(Boolean.TRUE)
                .createdAt(Instant.now());
            csvFileUpload.setLeasePaymentUpload(upload);

            upload = leasePaymentUploadRepository.save(upload);
            leasePaymentUploadSearchRepository.save(upload);

            if (request.isLaunchBatchImmediately()) {
                launchJobAfterCommit(upload);
            }

            LeasePaymentUploadResponse response = new LeasePaymentUploadResponse();
            response.setUploadId(upload.getId());
            response.setCsvFileId(csvFileUpload.getId());
            response.setStoredFileName(csvFileUpload.getStoredFileName());
            response.setUploadStatus(upload.getUploadStatus());
            return response;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to process uploaded file", e);
        }
    }

    @Transactional(readOnly = true)
    public Page<LeasePaymentUploadDTO> findAll(Pageable pageable) {
        return leasePaymentUploadRepository.findAll(pageable).map(leasePaymentUploadMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<LeasePaymentUploadDTO> search(String query, Pageable pageable, Long leaseContractId) {
        return leasePaymentUploadSearchRepository.search(query, leaseContractId, pageable).map(leasePaymentUploadMapper::toDto);
    }

    public LeasePaymentUploadDTO deactivateUpload(Long uploadId) {
        return leasePaymentUploadRepository
            .findById(uploadId)
            .map(upload -> {
                List<LeasePayment> leasePayments = leasePaymentRepository.findAllByLeasePaymentUploadId(upload.getId());
                leasePayments.forEach(payment -> payment.setActive(Boolean.FALSE));
                List<LeasePayment> updatedPayments = leasePaymentRepository.saveAll(leasePayments);
                upload.setActive(Boolean.FALSE);
                upload.setUploadStatus("DEACTIVATED");
                LeasePaymentUpload savedUpload = leasePaymentUploadRepository.save(upload);
                leasePaymentUploadSearchRepository.save(savedUpload);
                LeasePaymentUploadDTO response = leasePaymentUploadMapper.toDto(savedUpload);
                reindexLeasePaymentsAfterCommit(updatedPayments, Boolean.FALSE);
                return response;
            })
            .orElseThrow(() -> new IllegalArgumentException("Lease payment upload id #" + uploadId + " not found"));
    }

    public LeasePaymentUploadDTO activateUpload(Long uploadId) {
        return leasePaymentUploadRepository
            .findById(uploadId)
            .map(upload -> {
                List<LeasePayment> leasePayments = leasePaymentRepository.findAllByLeasePaymentUploadId(upload.getId());
                leasePayments.forEach(payment -> payment.setActive(Boolean.TRUE));
                List<LeasePayment> updatedPayments = leasePaymentRepository.saveAll(leasePayments);
                upload.setActive(Boolean.TRUE);
                upload.setUploadStatus("ACTIVE");
                LeasePaymentUpload savedUpload = leasePaymentUploadRepository.save(upload);
                leasePaymentUploadSearchRepository.save(savedUpload);
                LeasePaymentUploadDTO response = leasePaymentUploadMapper.toDto(savedUpload);
                reindexLeasePaymentsAfterCommit(updatedPayments, Boolean.TRUE);
                return response;
            })
            .orElseThrow(() -> new IllegalArgumentException("Lease payment upload id #" + uploadId + " not found"));
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

    private void launchJobAfterCommit(LeasePaymentUpload upload) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            LeasePaymentUpload jobUpload = upload;
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

    private void reindexLeasePaymentsAfterCommit(List<LeasePayment> updatedPayments, boolean active) {
        if (updatedPayments.isEmpty()) {
            return;
        }

        List<Long> paymentIds = updatedPayments.stream().map(LeasePayment::getId).collect(Collectors.toList());

        Runnable dispatch = () -> leasePaymentReindexProducer.sendReindexMessage(paymentIds, active);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    dispatch.run();
                }
            });
            return;
        }

        dispatch.run();
    }

    private String buildStoredFileName(String originalFilename) {
        String cleanName = originalFilename == null ? "lease-payments.csv" : originalFilename.replaceAll("\\s+", "-");
        return UUID.randomUUID() + "-" + cleanName;
    }

    private IFRS16LeaseContract resolveLeaseContract(Long leaseContractId) {
        return leaseContractRepository
            .findById(leaseContractId)
            .orElseThrow(() -> new IllegalArgumentException("IFRS16 lease contract id #" + leaseContractId + " not found"));
    }
}
